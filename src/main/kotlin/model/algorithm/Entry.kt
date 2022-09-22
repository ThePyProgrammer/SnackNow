package model.algorithm

class Entry<T>(
    coords: Array<Float>, dimensions: Array<Float>,
    val entry: T
): SpatialNode(coords, dimensions) {
    override fun toString() = "Entry: $entry"
}

interface NodeVisitor<V> {
    fun visit(depth: Int, coords: Array<Float>, dimensions: Array<Float>, value: V?)
}