package model.algorithm.rtree

import model.base.Point
import java.io.File

fun fillCoords(x: Double, y: Double = x) = arrayOf(x, y)

val ZEROS = fillCoords(0.0)
val ONES = fillCoords(1.0)
val POINT_FIVES = fillCoords(0.5)
val NEG_ONES = fillCoords(-1.0)
val NEG_POINT_FIVES = fillCoords(-0.5)


class Testcase(val bounds: List<Point>, val inserts: List<Point>, val queries: List<List<Point>>)

fun readCSV(filename: String) : Testcase {
    val file = File(filename)
    val lines = file.readLines()
    val tmp = lines[0].split(",").map { it.toDouble() }
    val bounds = listOf(Point(tmp[0], tmp[1]), Point(tmp[2], tmp[3]))
    val n = lines[1].split(",")[0].toInt()
    val inserts = lines.subList(2, n + 2)
        .map { it.split(",") }
        .map { Point(it[0].toDouble(), it[1].toDouble()) }
    val queries = lines.subList(n + 2, lines.size)
        .map { it.split(",") }
        .map { it.map{ it.toDouble() } }
        .map { listOf(Point(it[0], it[1]), Point(it[2], it[3])) }
    return Testcase(bounds, inserts, queries)
}


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

fun testGrid() {
    val testcase = readCSV("src/test/kotlin/model/algorithm/rtree/testdata/grid.csv")
    val rt = RTree<Double>();

    var count = 1.0
    for(insert in testcase.inserts) {
        rt.insert(Point(insert.x,insert.y), arrayOf( insert.x,insert.y), count++)
    }
    for(query in testcase.queries) {
        val results: List<Double> = rt.search(Point(query[0].x, query[0].y), arrayOf(query[1].x, query[1].y))

        println("Found ${results.size} matches")
        for(result in results.sorted()) print("$result ")
        println()
    }

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
    println("======== TEST GRID =======")
    testGrid()


}


