package model.algorithm.rtree

import model.base.Point

class Entry<T>(
    coords: Point, dimensions: Array<Double>,
    val entry: T
): SpatialNode(coords, dimensions) {
    override fun toString() = "Entry: $entry"
}