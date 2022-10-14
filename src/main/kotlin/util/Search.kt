package util

import me.xdrop.fuzzywuzzy.FuzzySearch
import model.algorithm.Item
import model.algorithm.QuadTree
import model.algorithm.SuperStore
import model.base.Point

private val SINGAPORE_RECTANGLE = listOf(
    1.1304753, // left
    1.4504753, // right
    104.0120359, // top
    103.6920359, // bottom
)
private val tree: QuadTree<SuperStore, Item> = QuadTree(
    Point(SINGAPORE_RECTANGLE[0], SINGAPORE_RECTANGLE[2]),
    Point(SINGAPORE_RECTANGLE[1], SINGAPORE_RECTANGLE[3])
)

fun initializeData() {

    

}

fun initializeTree() {



}

fun search(value: String): ArrayList<Item> {
    val result = ArrayList<Item>()
    val s = "random"
    if (FuzzySearch.ratio(value, s) > 75) {
        result.add(Item(s, "some random address", Point(0.0, 0.0), 0.00))
    }
    return result
}