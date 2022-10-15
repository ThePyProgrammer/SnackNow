package util

import data.hawkerCentres
import data.sevenElevens
import data.supermarkets
import me.xdrop.fuzzywuzzy.FuzzySearch
import model.algorithm.Item
import model.algorithm.QuadTree
import model.algorithm.SuperStore
import model.base.Location
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
private const val SEARCH_THRESHOLD = 75 // percent
private val tree: QuadTree<SuperStore, Item> = QuadTree(
    Point(SINGAPORE_RECTANGLE[0], SINGAPORE_RECTANGLE[3]),
    Point(SINGAPORE_RECTANGLE[2], SINGAPORE_RECTANGLE[1])
)
private val locations = ArrayList<Location>()
private var firstSearch = true

fun getData() {
    try {
        locations.addAll(hawkerCentres())
        locations.addAll(supermarkets())
        locations.addAll(sevenElevens())
    } catch (e: IOException) {
        throw RuntimeException(e)
    }
}

fun initialize() {

    getData()
    for (loc: Location in locations) {
        val superstore = SuperStore()
        val point = Point(loc.lng, loc.lat) // x, y
        superstore.addItem(Item("default item", loc.name, point, 1.23))
        tree.insert(superstore, point)
    }

}

fun itemToResult(item: Item): Result {
    return Result(item.itemName, item.price, item.address)
}

fun search(value: String): ArrayList<Result> {
    if (firstSearch) {
        initialize()
        firstSearch = false
    }
    val result = ArrayList<Result>()
    val strings = listOf("random", "hello", "test", "item", "default", "wow", "search", "key", "a very long string, hopefully this matches stuff", "")
    for (s in strings) {
        val searchRatio = FuzzySearch.partialRatio(value, s)
        if (searchRatio > SEARCH_THRESHOLD) {
            result.add(itemToResult(Item(s, "some random address", Point(0.0, 0.0), 0.00)))
        }
    }
    return result
}