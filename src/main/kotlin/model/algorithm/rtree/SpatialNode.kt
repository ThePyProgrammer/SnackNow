package model.algorithm.rtree

import model.base.Node
import model.base.Point
import java.util.*



open class SpatialNode(
    coords: Point, dimensions: Array<Double>, val leaf: Boolean = true, children: LinkedList<SpatialNode> = LinkedList<SpatialNode>()
):
    Node<Argument>(
        Argument(Point(coords), dimensions.copyOf()),
        children.toArray(arrayOfNulls(children.size))
    ) {

    /**
     * This determines the location of the initial point of the object
     * in N-dimensional space
     */
    var coords
        get() = item.coords
        set(value) {
            item.coords = value
        }
    var dimensions
        get() = item.dimensions
        set(value) {
            item.dimensions = value
        }

    val maxCoords get() = coords.offset(dimensions)

    var children = arrayOf<SpatialNode>()

    fun addChild(node: SpatialNode) {
        children += arrayOf(node)
    }

    fun addChildren(nodes: Iterable<SpatialNode>) {
        nodes.forEach { addChild(it) }
    }

    var parent: SpatialNode? = null

    fun duplicate() = SpatialNode(this.coords, this.dimensions, this.leaf).also {
        it.parent = this.parent
        if(it.parent != null) it.parent!!.addChild(it)
    }

    fun clearChildren() {
        children = arrayOf()
    }

    fun removeChild(node: SpatialNode) {
        children = (children.toSet() - setOf(node)).toTypedArray()
    }

}