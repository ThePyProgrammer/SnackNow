package model.algorithm.quadtree

import model.algorithm.Item
import model.algorithm.QuadTree
import model.algorithm.SuperStore
import model.base.Point


fun sampleTest() {
    val tree = QuadTree<SuperStore, Item>(Point(0.0, 0.01), Point(0.01, 0.0))
    // Note that it's x, y, which means that the top left and bottom right are defined a little unintuitively
    val store1 = SuperStore().apply {
        addItem(Item("Cocaine", "Outside", Point(0.003, 0.003), 50.0))
    }
    val store2 = SuperStore().apply {
        addItem(Item("Cocaine", "Outside v2", Point(0.008, 0.004), 100.0))
        addItem(Item("Heroin", "Outside v2", Point(0.008, 0.004), 20.0))
    }

    // Note that while superstores have a position, they are implicitly min-size quads
    tree.insert(store1, Point(0.003, 0.003))
    tree.insert(store2, Point(0.008, 0.004))

    println(tree.rangeQuery(Point(0.0, 0.01), Point(0.01, 0.0)).toString());
}

fun main() {
    println("========= SAMPLE TEST CASE =========")
    sampleTest()
}