package model.algorithm.rtree

import model.base.Point
import util.*
import java.util.*
import kotlin.math.*


class RTree<T>(
    val maxEntries: Int = 50, val minEntries: Int = 2,
    val numDims: Int = 2, val seedPicker: SeedPicker = SeedPicker.LINEAR
) {
    enum class SeedPicker { LINEAR, QUADRATIC }

    final val FUDGE_FACTOR = 1.001f

    @Volatile
    var size = 0

    fun buildRoot(asLeaf: Boolean) = SpatialNode(
        Point(sqrt(Double.MAX_VALUE), sqrt(Double.MAX_VALUE)),
        arrayOfNulls<Double>(numDims).replaceAllNulls(-2.0 * sqrt(Double.MAX_VALUE)),
        asLeaf
    )

    var root = buildRoot(true)

    val pointDims = arrayOfNulls<Double>(numDims).replaceAllNulls(0.0)


    /**
     * Do the coordinates overlap in both dimensions? :thinking:
     *
     */
    fun isOverlap(
        scoords: Point, sdimensions: Array<Double>,
        coords: Point, dimensions: Array<Double>
    )  =  ((scoords.x == coords.x ||
            (scoords.x < coords.x && coords.x + FUDGE_FACTOR * sdimensions[0] >= coords.x) ||
            (scoords.x > coords.x && coords.x + FUDGE_FACTOR * dimensions[0] >= scoords.x)
           ) &&
           (scoords.y == coords.y ||
            (scoords.y < coords.y && coords.y + FUDGE_FACTOR * sdimensions[1] >= coords.y) ||
            (scoords.y > coords.y && coords.y + FUDGE_FACTOR * dimensions[1] >= scoords.y)
           )
        )


    fun search(
        coords: Point, dimensions: Array<Double>,
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

    fun getArea(dimensions: Array<Double>) = dimensions.fold(1.0) { a, b -> a*b }

    fun getRequiredExpansion(
        coords: Point, dimensions: Array<Double>, e: SpatialNode
    ): Double = run {
        val area = getArea(dimensions)
        val deltas = (coords - e.coords).zip(dimensions.subtractEach(e.dimensions)).map {
            if(it.first + it.second <= 0) it.first + it.second else it.first
        }.toTypedArray()

        dimensions.addEach(deltas).product() - area
    }

    fun chooseLeaf(n: SpatialNode, e: Entry<T>): SpatialNode {
        if(n.leaf) return n
        var minInc = Double.MAX_VALUE
        lateinit var next: SpatialNode

        n.children.forEach {
            val inc = getRequiredExpansion(it.coords, it.dimensions, e)
            if(inc < minInc) {
                minInc = inc
                next = it
            } else if(inc == minInc) {
                if(next.dimensions.product() < it.dimensions.product()) next = it
            }
        }

        return chooseLeaf(next, e)
    }

    fun tighten(vararg nodes: SpatialNode) {
        assert(nodes.isNotEmpty()) { "Pass some nodes to tighten!" }
        nodes.forEach {
            assert(it.children.isNotEmpty()) { "tighten() called on empty node!" }
            it.children.map { child -> child.parent = it }
            val minCoords = Point(it.children.minOfOrNull { child -> child.coords.x }!!, it.children.minOfOrNull { child -> child.coords.y }!!)
            val maxCoords = Point(it.children.maxOfOrNull { child -> child.coords.x + child.dimensions[0] }!!, it.children.maxOfOrNull { child -> child.coords.y + child.dimensions[1] }!!)
            val maxDims = maxCoords - minCoords
            it.coords = minCoords
            System.arraycopy(maxDims, 0, it.dimensions, 0, numDims)
        }
    }

    fun linearPickNext(cc: LinkedList<SpatialNode>) = cc.pop()

    fun linearPickSeeds(nn: LinkedList<SpatialNode>): Array<SpatialNode> {
        @SuppressWarnings("unchecked")
        var bestPair: Array<SpatialNode> = arrayOf(nn[0], nn[1])
        var bestSep = 0.0

        val nnArr = nn.toTypedArray()

        val dimLowerMinX = nnArr.minOfOrNull { it.coords.x } ?: (nn[0].coords.x)
        val (nodeLowerMaxX, dimLowerMaxX) = nnArr.maxOfAndByOrNull { it.coords.x } ?: (nn[0] to nn[0].coords.x)
        val (nodeUpperMinX, dimUpperMinX) = nnArr.minOfAndByOrNull { it.maxCoords.x } ?: (nn[0] to nn[0].maxCoords.x)
        val dimUpperMaxX = nnArr.maxOfOrNull { it.maxCoords.x } ?: (nn[0].maxCoords.x)
        var sep = abs((dimUpperMinX - dimLowerMaxX)/(dimUpperMaxX - dimLowerMinX))
        if(nodeLowerMaxX != nodeUpperMinX && sep >= bestSep) {
            bestSep = sep
            bestPair = arrayOf(nodeLowerMaxX, nodeUpperMinX)
        }

        val dimLowerMinY = nnArr.minOfOrNull { it.coords.y } ?: (nn[0].coords.y)
        val (nodeLowerMaxY, dimLowerMaxY) = nnArr.maxOfAndByOrNull { it.coords.y } ?: (nn[0] to nn[0].coords.y)
        val (nodeUpperMinY, dimUpperMinY) = nnArr.minOfAndByOrNull { it.maxCoords.y } ?: (nn[0] to nn[0].maxCoords.y)
        val dimUpperMaxY = nnArr.maxOfOrNull { it.maxCoords.y } ?: (nn[0].maxCoords.y)
        sep = abs((dimUpperMinY - dimLowerMaxY)/(dimUpperMaxY - dimLowerMinY))
        if(nodeLowerMaxY != nodeUpperMinY && sep >= bestSep) {
            bestSep = sep
            bestPair = arrayOf(nodeLowerMaxY, nodeUpperMinY)
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

            n1.coords.toArray().minEach(n2.coords.toArray()).subtractEach(
                n1.coords.offset(n1.dimensions).toArray().maxEach(n2.coords.offset(n2.dimensions).toArray())
            ).product() - getArea(n1.dimensions) - getArea(n2.dimensions)
        } ?: (nn[0] to nn[1])).toArray()

        nn.removeAll(bestPair.toSet())
        return bestPair
    }

    fun splitNode(n: SpatialNode): Array<SpatialNode> {
        val n2 = n.duplicate()
        val cc = LinkedList(n.children.toList())
        n.clearChildren()
        val ss: Array<SpatialNode> = if(seedPicker == SeedPicker.LINEAR) linearPickSeeds(cc) else quadraticPickSeeds(cc)

        n.addChild(ss[0])
        n2.addChild(ss[1])
        tighten(n, n2)

        while(!cc.isEmpty()) {
            if((n.children.size >= minEntries) && (n2.children.size + cc.size == minEntries)) {
                n2.addChildren(cc)
                cc.clear()
                tighten(n, n2)
                break
            } else if((n2.children.size >= minEntries) && (n.children.size + cc.size == minEntries)) {
                n.addChildren(cc)
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
            }.apply { addChild(c) }
            tighten(preferred)
        }
        return arrayOf(n, n2)
    }

    fun adjustTree(n: SpatialNode, nn: SpatialNode?) {
        if(n == root) {
            if(nn != null) {
                root = buildRoot(false).apply {
                    addChildren(listOf(n, nn).map { it.also { it.parent = this@apply } })
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

    fun visit(nvisit: (Int, Array<Double>, Array<Double>, T?) -> Unit) {
        val coordBuf = zeros(numDims)
        val dimBuf = zeros(numDims)
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

    fun insert(coords: Point, dimensions: Array<Double> = pointDims, entry: T) {
        val e = Entry(coords, dimensions, entry)
        val l = chooseLeaf(root, e).apply { addChild(e) }
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
                node.parent?.removeChild(node)
            } else if(!node.leaf && node.children.size < minEntries) {
                val toVisit = LinkedList(node.children.toList())
                while(toVisit.isNotEmpty()) {
                    val c = toVisit.pop()
                    if(c.leaf) q.addAll(c.children)
                    else toVisit.addAll(c.children)
                }
                node.parent?.removeChild(node)
            } else tighten(node)
            node = node.parent!!
        }
        if(root.children.isEmpty()) root = buildRoot(true)
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

    fun findLeaf(n: SpatialNode, coords: Point, dimensions: Array<Double>, entry: T): SpatialNode? {
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

    fun delete(coords: Point, dimensions: Array<Double> = pointDims, entry: T): Boolean {
        val l: SpatialNode = findLeaf(root, coords, dimensions, entry) ?: error("Could not find leaf for entry to delete")
        // If the above were actually null, then screw the program

        assert(l.leaf) { "Entry is not found at leaf?!?" }

        val li = l.children.iterator()
        var removed: T? = null

        while(li.hasNext()) {
            val e = li.next() as Entry<*>
            if((e.entry as T)?.equals(entry) == true) {
                removed = e.entry
                l.removeChild(e)
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