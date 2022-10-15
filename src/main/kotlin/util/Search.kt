package util

import data.*
import data.Place
import me.xdrop.fuzzywuzzy.FuzzySearch
import model.algorithm.Item
import model.algorithm.QuadTree
import model.algorithm.SuperStore
import model.base.Point
import model.base.Result
import java.io.IOException

private val SINGAPORE_RECTANGLE_ALL_WRONG = listOf(
    103.6920359, // left
    104.0120359, // right
    1.4504753, // top
    1.1304753 // bottom
)
private val SINGAPORE_RECTANGLE = listOf(
    103.470200, // left
    1.143773, // bottom
    104.167488, // right
    1.498674, // top
)
private const val SEARCH_THRESHOLD = 60 // percent
private val tree: QuadTree<SuperStore, Item> = QuadTree(
    Point(SINGAPORE_RECTANGLE[0], SINGAPORE_RECTANGLE[3]),
    Point(SINGAPORE_RECTANGLE[2], SINGAPORE_RECTANGLE[1])
)
private val places = ArrayList<Place>()
private var firstSearch = true

private fun getData() {
    try {
        places.addAll(hawkerCentres())
        places.addAll(supermarkets())
        places.addAll(sevenElevens())
    } catch (e: IOException) {
        throw RuntimeException(e)
    }
}

fun initialize() {

    getData()
    initFakeData(places)
    for (place: Place in places) {
        val superstore = SuperStore()
        superstore.addItems(place.items)
        tree.insert(superstore, place.point)
    }

}

fun itemToResult(item: Item): Result {
    return Result(item.itemName, item.price, item.address, item)
}

// main search function
fun search(value: String): ArrayList<Result> {
    if (firstSearch) {
        initialize()
        firstSearch = false
    }
    val queryItems = tree.rangeQuery(
        Point(SINGAPORE_RECTANGLE[0], SINGAPORE_RECTANGLE[3]),
        Point(SINGAPORE_RECTANGLE[2], SINGAPORE_RECTANGLE[1])
    )
    val result = ArrayList<Result>()
    // val strings = listOf("random", "hello", "test", "item", "default", "wow", "search", "key", "a very long string, hopefully this matches stuff", "")
    for (item in queryItems) {
        val searchRatio = FuzzySearch.partialRatio(value, item.itemName)
        if (searchRatio > SEARCH_THRESHOLD) {
            result.add(itemToResult(item))
        }
    }
    return result
}