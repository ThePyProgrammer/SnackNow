package model.algorithm

import model.util.replaceAllNulls
import java.util.*

open class SpatialNode(
    coords: Array<Float>, dimensions: Array<Float>, val leaf: Boolean = true, val children: LinkedList<SpatialNode> = LinkedList<SpatialNode>()
):
    Node<Argument>(
        Argument(coords.copyOf(coords.size).replaceAllNulls(0F), dimensions.copyOf(dimensions.size).replaceAllNulls(0F)),
        children.toArray(arrayOfNulls(children.size))
    ) {
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

    var parent: SpatialNode? = null

    fun duplicate() = SpatialNode(this.coords, this.dimensions, this.leaf).also {
        it.parent = this.parent
        if(it.parent != null) it.parent!!.children.add(it)
    }

}