package model.algorithm.quadtree

import model.algorithm.Item
import model.algorithm.QuadTree
import model.algorithm.SuperStore
import model.base.Point
import java.io.File


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

fun test(test: Testcase) {
    val tree = QuadTree<SuperStore, Item>(test.bounds[0],test.bounds[1])
    test.inserts.forEach { insert ->
        tree.insert(SuperStore().apply {
              addItem(Item("","",insert,1.0))
          }, insert)
    }

    var count = 1;
    test.queries.forEach { query ->
        val result = tree.rangeQuery(query[0], query[1]).map { Point(it.x,it.y) }
        println("${count++}) Queried ${query[0].toString()} to ${query[1].toString()} and received ${result.size} result(s):")
        println(result)
    }
}

//fun sampleTest() {
//
//    val tree = QuadTree<SuperStore, Item>(Point(0.0, 0.01), Point(0.01, 0.0))
//    // Note that it's x, y, which means that the top left and bottom right are defined a little unintuitively
//
//    val store1 = SuperStore().apply {
//        addItem(Item("Cocaine", "Outside", Point(0.003, 0.003), 50.0))
//    }
//    val store2 = SuperStore().apply {
//        addItem(Item("Cocaine", "Outside v2", Point(0.008, 0.004), 100.0))
//        addItem(Item("Heroin", "Outside v2", Point(0.008, 0.004), 20.0))
//    }
//
//    // Note that while superstores have a position, they are implicitly min-size quads
//    tree.insert(store1, Point(0.003, 0.003))
//    tree.insert(store2, Point(0.008, 0.004))
//
//    println(tree.rangeQuery(Point(0.0, 0.01), Point(0.01, 0.0)).toString())
//}



fun main() {
    val rootpath = "src/test/kotlin/model/algorithm/quadtree/testdata"
    println("========= SAMPLE TEST CASE =========")
    println("========= TEST GENERIC CASE =========")
    test(readCSV("${rootpath}/simple.csv"))
    println("========= TEST LARGE CASE =========")
    test(readCSV("${rootpath}/grid.csv"))


}