package model.base

open class Point(var x: Double = 0.0, var y: Double = 0.0) {
    constructor(p: Point) : this(p.x, p.y)
    constructor(arr: Array<Double>) : this(arr[0], arr[1])
    fun isEqualTo(p: Point) = x == p.x && y == p.y
    fun isInside(topLeft: Point, bottomRight: Point): Boolean {
        return topLeft.x <= x && x <= bottomRight.x
                && topLeft.y >= y && y >= bottomRight.y
    }
    override fun toString() = String.format("(%.5f, %.5f)", x, y)
    fun toArray() = arrayOf(x, y)

    fun offset(a: Array<Double>) = Point(x+a[0], y+a[1])

    infix operator fun minus(other: Point) = arrayOf(x - other.x, y - other.y)
}