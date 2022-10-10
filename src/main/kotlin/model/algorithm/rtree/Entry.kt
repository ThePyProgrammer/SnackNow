package model.algorithm.rtree

class Entry<T>(
    coords: Array<Float>, dimensions: Array<Float>,
    val entry: T
): SpatialNode(coords, dimensions) {
    override fun toString() = "Entry: $entry"
}