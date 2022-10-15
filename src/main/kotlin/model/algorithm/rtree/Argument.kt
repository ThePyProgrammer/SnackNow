package model.algorithm.rtree

import model.base.Point

data class Argument(var coords: Point, var dimensions: Array<Double>) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Argument) return false
        if(!coords.isEqualTo(other.coords)) return false
        if (!dimensions.contentEquals(other.dimensions)) return false
        return true
    }

    override fun hashCode(): Int {
        var result = (coords.x * coords.y).toUInt().toInt()
        result = 31 * result + dimensions.contentHashCode()
        return result
    }
}