package model.algorithm.rtree

import model.base.Point

fun fillCoords(x: Double, y: Double = x) = arrayOf(x, y)

val ZEROS = fillCoords(0.0)
val ONES = fillCoords(1.0)
val POINT_FIVES = fillCoords(0.5)
val NEG_ONES = fillCoords(-1.0)
val NEG_POINT_FIVES = fillCoords(-0.5)


fun testCreation() {
    val rt = RTree<Float>()
}

fun testInsertPoint() {
    val rt = RTree<Float>()
    val price = 3.5f
    rt.insert(Point(), ZEROS, price)
    val results: List<Float> = rt.search(Point(NEG_ONES), fillCoords(2.0))
    for(result in results) print("$result ")
    println()
}

fun testInsertRect() {
    val rt = RTree<Float>()
    val price = 3.5f
    rt.insert(Point(), ONES, price)
    val results: List<Float> = rt.search(Point(NEG_ONES), fillCoords(3.0))
    for(result in results) print("$result ")
    println()
}

fun testInsertNegativeCoords() {
    val rt = RTree<Double>().apply {
        insert(Point(NEG_ONES), POINT_FIVES, 5.5)
        insert(Point(POINT_FIVES), ONES, 3.5)
    }
    var results: List<Double> = rt.search(Point(NEG_POINT_FIVES), ZEROS)
    for(result in results) print("$result ")
    println()
    results = rt.search(Point(POINT_FIVES), ZEROS)
    for(result in results) print("$result ")
    println()
}

fun testEmpty() {
    val rt = RTree<Double>().apply { insert(Point(), ZEROS, 4.5) }
    val results: List<Double> = rt.search(Point(fillCoords(-1.0)), fillCoords(0.5))
    println(results.size)
}

fun expSetUp(seedPicker: RTree.SeedPicker = RTree.SeedPicker.LINEAR) = RTree<Int>(2, 1, 2, seedPicker).apply {
    for(i in 0..3) {
        insert(Point(fillCoords(i.toDouble())), fillCoords(0.5), i)
    }
}

fun testSplitNodeSmall() {
    val rt = expSetUp(RTree.SeedPicker.QUADRATIC)
    val results: List<Int> = rt.search(Point(fillCoords(2.0)), fillCoords(0.5))
    for(result in results) print("$result ")
    println()
}

fun testRemoveAll() {
    val rt = expSetUp()
    val sCoords = fillCoords(-0.5 * Double.MAX_VALUE)
    val sDims = fillCoords(Double.MAX_VALUE)
    val results: List<Int> = rt.search(Point(sCoords), sDims)
}



fun main() {
    println("========= TEST CREATION =========")
    testCreation()
    println("====== TEST POINT INSERTION =====")
    testInsertPoint()
    println("====== TEST POINT RECTANGLE =====")
    testInsertRect()
    println("====== TEST NEGATIVE POINTS =====")
    testInsertNegativeCoords()
    println("======= TEST EMPTY RESULTS ======")
    testEmpty()
    println("======== TEST SPLIT NODES =======")
    testSplitNodeSmall()

}


