package model.base

open class Point(public var x: Double = 0.0, public var y: Double = 0.0) {
    constructor(p: Point) : this(p.x, p.y)
    fun isEqualTo(p: Point) = x == p.x && y == p.y
    fun isInside(topLeft: Point, bottomRight: Point): Boolean {
        return topLeft.x <= x && x <= bottomRight.x
                && topLeft.y >= y && y >= bottomRight.y
    }
    override fun toString() = String.format("(%.5f, %.5f)", x, y)
}