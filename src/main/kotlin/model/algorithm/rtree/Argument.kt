package model.algorithm.rtree

data class Argument(var coords: Array<Float>, var dimensions: Array<Float>) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Argument) return false

        if (!coords.contentEquals(other.coords)) return false
        if (!dimensions.contentEquals(other.dimensions)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = coords.contentHashCode()
        result = 31 * result + dimensions.contentHashCode()
        return result
    }
}