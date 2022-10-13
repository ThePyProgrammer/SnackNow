package model.algorithm.rtree

import util.*
import java.util.*
import kotlin.math.abs
import kotlin.math.roundToInt
import kotlin.math.sqrt


class RTree<T>(
    val maxEntries: Int = 50, val minEntries: Int = 2,
    val numDims: Int = 2, val seedPicker: SeedPicker = SeedPicker.LINEAR
) {
    enum class SeedPicker { LINEAR, QUADRATIC }

    final val FUDGE_FACTOR = 1.001f

    @Volatile
    var size = 0

    fun buildRoot(asLeaf: Boolean) = SpatialNode(
        arrayOfNulls<Float>(numDims).replaceAllNulls(sqrt(Float.MAX_VALUE)),
        arrayOfNulls<Float>(numDims).replaceAllNulls(-2.0f * sqrt(Float.MAX_VALUE)),
        asLeaf
    )

    var root = buildRoot(true)

    val pointDims = arrayOfNulls<Float>(numDims).replaceAllNulls(0F)


    fun isOverlap(
        scoords: Array<Float>, sdimensions: Array<Float>,
        coords: Array<Float>, dimensions: Array<Float>
    ): Boolean {
        for (i in scoords.indices) {
            var overlapInThisDimension = false
            if (scoords[i] == coords[i]) overlapInThisDimension = true
            else if (scoords[i] < coords[i] && scoords[i] + FUDGE_FACTOR * sdimensions[i] >= coords[i]) overlapInThisDimension = true
            else if (scoords[i] > coords[i] && coords[i] + FUDGE_FACTOR * dimensions[i] >= scoords[i]) overlapInThisDimension = true
            if (!overlapInThisDimension) return false
        }

        return true
    }


    fun search(
        coords: Array<Float>, dimensions: Array<Float>,
        n: SpatialNode = root, results: LinkedList<T> = LinkedList<T>()
    ): List<T> {
        if(n.leaf) {
            n.children.filter {
                isOverlap(coords, dimensions, it.coords, it.dimensions)
            }.forEach {
                results.add((it as Entry<T>).entry)
            }
        } else {
            n.children.filter {
                isOverlap(coords, dimensions, it.coords, it.dimensions)
            }.forEach {
                search(coords, dimensions, it, results)
            }
        }
        return results
    }

    fun getArea(dimensions: Array<Float>) = dimensions.fold(1.0f) { a, b -> a*b }

    fun getRequiredExpansion(
        coords: Array<Float>, dimensions: Array<Float>, e: SpatialNode
    ) = run {
        val area = getArea(dimensions)

        val deltas = coords.zip(dimensions).mapIndexed { i, pair ->
            if(pair.first + pair.second <= e.coords[i] + e.dimensions[i])
                (e.coords[i] + e.dimensions[i] - pair.first - pair.second)
            else pair.first - e.coords[i]
        }.toTypedArray()

        dimensions.addEach(deltas).fold(1.0f) { a, b -> a*b } - area
    }

    fun chooseLeaf(n: SpatialNode, e: Entry<T>): SpatialNode {
        if(n.leaf) return n
        var minInc = Float.MAX_VALUE
        lateinit var next: SpatialNode

        n.children.forEach {
            val inc = getRequiredExpansion(it.coords, it.dimensions, e)
            if(inc < minInc) {
                minInc = inc
                next = it
            } else if(inc == minInc) {
                if(next.dimensions.fold(1.0f) { a, b -> a*b } < it.dimensions.fold(1.0f) { a, b -> a*b }) {
                    next = it
                }
            }
        }

        return chooseLeaf(next, e)
    }

    fun tighten(vararg nodes: SpatialNode) {
        assert(nodes.isNotEmpty()) { "Pass some nodes to tighten!" }
        nodes.forEach {
            assert(it.children.isNotEmpty()) { "tighten() called on empty node!" }
            val minCoords = arrayOfNulls<Float>(numDims).replaceAllNulls(Float.MAX_VALUE)
            var maxCoords = arrayOfNulls<Float>(numDims).replaceAllNulls(-1.0f * Float.MAX_VALUE)

            for (i in 0 until numDims) {
                for (c in it.children) {
                    c.parent = it
                    if (c.coords[i] < minCoords[i]) minCoords[i] = c.coords[i]
                    if (c.coords[i] + c.dimensions[i] > maxCoords[i]) maxCoords[i] = c.coords[i] + c.dimensions[i]
                }
            }

            maxCoords = maxCoords.subtractEach(minCoords)

            System.arraycopy(minCoords, 0, it.coords, 0, numDims)
            System.arraycopy(maxCoords, 0, it.dimensions, 0, numDims)
        }
    }

    fun linearPickNext(cc: LinkedList<SpatialNode>) = cc.pop()

    fun linearPickSeeds(nn: LinkedList<SpatialNode>): Array<SpatialNode> {
        @SuppressWarnings("unchecked")
        var bestPair: Array<SpatialNode> = arrayOf(nn[0], nn[1])
        var bestSep = 0.0f

        for(i in 0 until numDims) {
            val dimLb = nn.minOfOrNull { it.coords[i] } ?: nn[0].coords[i]
            val dimUb = nn.maxOfOrNull { it.coords[i] + it.dimensions[i] } ?: (nn[0].coords[i] + nn[0].dimensions[i])
            val nMaxLb: SpatialNode = nn.maxByOrNull { it.coords[i]  }?: nn[0]
            val nMinUb: SpatialNode = nn.minByOrNull { it.coords[i] + it.dimensions[i] }?: nn[0]
            val dimMinUb = nMinUb.coords[i] + nMinUb.dimensions[i]
            val dimMaxLb = nMaxLb.coords[i]
            val sep = if(nMaxLb == nMinUb) -1.0f
                else abs((dimMinUb - dimMaxLb) / (dimUb - dimLb))
            if(sep >= bestSep) {
                bestPair = arrayOf(nMaxLb, nMinUb)
                bestSep = sep
            }
        }
        nn.removeAll(bestPair.toSet())
        return bestPair
    }

    fun quadraticPickNext(cc: LinkedList<SpatialNode>, nn: Array<SpatialNode>): SpatialNode {
        val nextC: SpatialNode? = cc.maxByOrNull {
            getRequiredExpansion(nn[1].coords, nn[1].dimensions, it)
            - getRequiredExpansion(nn[0].coords, nn[0].dimensions, it)
        }
        assert(nextC != null) { "No node selected from qPickNext" }
        cc.remove(nextC)
        return nextC!!
    }

    fun quadraticPickSeeds(nn: LinkedList<SpatialNode>): Array<SpatialNode> {
        val bestPair: Array<SpatialNode> = (nn.getPairs().maxByOrNull { pair ->
            val n1 = pair.first
            val n2 = pair.second
            n1.coords.minEach(n2.coords).subtractEach(
                n1.coords.addEach(n1.dimensions).maxEach(n2.coords.addEach(n2.dimensions))
            ).fold(1.0f) { a, b -> a*b } - getArea(n1.dimensions) - getArea(n2.dimensions)
        } ?: (nn[0] to nn[1])).toArray()

        nn.removeAll(bestPair)
        return bestPair
    }

    fun splitNode(n: SpatialNode): Array<SpatialNode> {
        val n2 = n.duplicate()
        val cc = LinkedList(n.children)
        n.children.clear()
        val ss: Array<SpatialNode> = if(seedPicker == SeedPicker.LINEAR) linearPickSeeds(cc) else quadraticPickSeeds(cc)

        n.children.add(ss[0])
        n2.children.add(ss[1])
        tighten(n, n2)

        while(!cc.isEmpty()) {
            if((n.children.size >= minEntries) && (n2.children.size + cc.size == minEntries)) {
                n2.children.addAll(cc)
                cc.clear()
                tighten(n, n2)
                break
            } else if((n2.children.size >= minEntries) && (n.children.size + cc.size == minEntries)) {
                n.children.addAll(cc)
                cc.clear()
                tighten(n, n2)
                return arrayOf(n, n2)
            }
            val c = if(seedPicker == SeedPicker.LINEAR) linearPickNext(cc) else quadraticPickNext(cc, arrayOf(n, n2))
            val e0 = getRequiredExpansion(n.coords, n.dimensions, c)
            val e1 = getRequiredExpansion(n2.coords, n2.dimensions, c)
            val preferred = when {
                e0 < e1 -> n
                e1 < e0 -> n2
                n.children.size < n2.children.size -> n
                n.children.size > n2.children.size -> n2
                else -> if(Math.random().roundToInt() == 1) n2 else n
            }.apply { children.add(c) }
            tighten(preferred)
        }
        return arrayOf(n, n2)
    }

    fun adjustTree(n: SpatialNode, nn: SpatialNode?) {
        if(n == root) {
            if(nn != null) {
                root = buildRoot(false).apply {
                    children.addAll(listOf(n, nn).map { it.also { it.parent = this@apply } })
                }
            }
            tighten(root)
            return
        }
        tighten(n)
        if(nn != null) {
            tighten(nn)
            if(n.parent!!.children.size > maxEntries) {
                val splits = splitNode(n.parent!!)
                adjustTree(splits[0], splits[1])
            }
        }
        if(n.parent != null) {
            adjustTree(n.parent!!, null)
        }
    }

    fun visit(nvisit: (Int, Array<Float>, Array<Float>, T?) -> Unit) {
        val coordBuf = arrayOfNulls<Float>(numDims).replaceAllNulls(0.0F)
        val dimBuf = arrayOfNulls<Float>(numDims).replaceAllNulls(0.0F)
        val toVisit: Queue<SpatialNode> = LinkedList<SpatialNode>().apply { add(root) }
        val nodeDepths: MutableMap<SpatialNode, Int> = hashMapOf(root to 0)
        while(!toVisit.isEmpty()) {
            val currentNode = toVisit.remove()
            if(currentNode.parent != null) nodeDepths[currentNode] = (nodeDepths[currentNode.parent] ?: 0) + 1
            System.arraycopy(currentNode.coords, 0, coordBuf, 0, numDims)
            System.arraycopy(currentNode.dimensions, 0, dimBuf, 0, numDims)
            if(currentNode is Entry<*>) nvisit(nodeDepths[currentNode]?:0, coordBuf, dimBuf, currentNode.entry as T)
            else nvisit(nodeDepths[currentNode]?:0, coordBuf, dimBuf, null as T?)
            toVisit.addAll(currentNode.children)
        }
    }

    fun insert(coords: Array<Float>, dimensions: Array<Float> = pointDims, entry: T) {
        assert (coords.size == numDims);
        assert (dimensions.size == numDims);
        val e = Entry(coords, dimensions, entry)
        val l = chooseLeaf(root, e).apply { children.add(e) }
        size++
        e.parent = l

        if(l.children.size > maxEntries) splitNode(l).apply { adjustTree(this[0], this[1]) }
        else adjustTree(l, null)
    }

    fun clear() { root = buildRoot(true) }

    fun condenseTree(n: SpatialNode) {
        var node = n;
        val q = HashSet<SpatialNode>()
        while(node != root) {
            if(node.leaf && (node.children.size < minEntries)) {
                q.addAll(node.children)
                node.parent?.children?.remove(node)
            } else if(!node.leaf && node.children.size < minEntries) {
                val toVisit = LinkedList<SpatialNode>(node.children)
                while(toVisit.isNotEmpty()) {
                    val c = toVisit.pop()
                    if(c.leaf) q.addAll(c.children)
                    else toVisit.addAll(c.children)
                }
                node.parent?.children?.remove(node)
            } else tighten(node)
            node = node.parent!!
        }
        if(root.children.size == 0) root = buildRoot(true)
        else if((root.children.size == 1) && !root.leaf)
            root = root.children[0].apply { parent = null }
        else tighten(root)

        for(ne in q) {
            (ne as Entry<*>).apply {
                insert(coords, dimensions, entry as T)
            }
        }
        size -= q.size
    }

    fun findLeaf(n: SpatialNode, coords: Array<Float>, dimensions: Array<Float>, entry: T): SpatialNode? {
        if(n.leaf) {
            for(c in n.children) {
                if(((c as Entry<*>).entry as T)?.equals(entry) == true) return c
            }
            return null
        } else {
            for(c in n.children) {
                if(isOverlap(c.coords, c.dimensions, coords, dimensions)) {
                    val result = findLeaf(c, coords, dimensions, entry)
                    if(result != null) return result
                }
            }
            return null
        }
    }

    fun delete(coords: Array<Float>, dimensions: Array<Float> = pointDims, entry: T): Boolean {
        assert(coords.size == numDims)
        assert(dimensions.size == numDims)
        val l: SpatialNode = findLeaf(root, coords, dimensions, entry) ?: error("Could not find leaf for entry to delete")
        // If the above were actually null, then screw the program

        assert(l.leaf) { "Entry is not found at leaf?!?" }

        val li = l.children.listIterator()
        var removed: T? = null

        while(li.hasNext()) {
            val e = li.next() as Entry<*>
            if((e.entry as T)?.equals(entry) == true) {
                removed = e.entry
                li.remove()
                break
            }
        }

        if(removed != null) {
            condenseTree(l)
            size--
        }

        if(size == 0) root = buildRoot(true)

        return removed != null
    }
}