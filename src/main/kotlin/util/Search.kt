package util

import data.*
import data.Place
import me.xdrop.fuzzywuzzy.FuzzySearch
import model.algorithm.Item
import model.algorithm.QuadTree
import model.algorithm.SuperStore
import model.base.Location
import model.base.Point
import model.base.Result
import java.io.IOException
import kotlin.math.cos

private val SINGAPORE_RECTANGLE = listOf(
    103.470200, // left
    1.143773, // bottom
    104.167488, // right
    1.498674, // top
)
private const val SEARCH_THRESHOLD = 80 // percent
private val tree: QuadTree<SuperStore, Item> = QuadTree(
    Point(SINGAPORE_RECTANGLE[0], SINGAPORE_RECTANGLE[3]),
    Point(SINGAPORE_RECTANGLE[2], SINGAPORE_RECTANGLE[1])
)
private val places = ArrayList<Place>()

// initialize stuff

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
    initPostalCodeThingy()
    for (place: Place in places) {
        val superstore = SuperStore()
        superstore.addItems(place.items)
        tree.insert(superstore, place.point)
    }

}

// stuff to make the search function work

fun itemToResult(item: Item): Result {
    return Result(item.itemName, item.price, item.address, item)
}

fun bound(x: Double, min: Double, max: Double) =
    if(x < min) min
    else if(x > max) max
    else x

fun km2latlng(x: Double, y: Double): Array<Double> {
    val lat = (x / 110.574)
    val long = y / 111.320 / cos(lat)
    return arrayOf(long, lat)
}

fun Point.offsetBounded(offset: Array<Double>) = Point(
    bound(x + offset[0], SINGAPORE_RECTANGLE[0], SINGAPORE_RECTANGLE[2]),
    bound(y + offset[1], SINGAPORE_RECTANGLE[1], SINGAPORE_RECTANGLE[3])
)

data class Triplet<P, Q, R>(val first: P, val second: Q, val third: R)

infix fun <T, S, R> Pair<T, S>.with(other: R) = Triplet(first, second, other)

inline operator fun Array<Double>.unaryMinus() = map { -it }

// main search function
fun search(searchValue: String, userLocation: Location, numQueries: Int, distancePriority: String): ArrayList<Result> {

    val userPoint = Point(userLocation.lng, userLocation.lat)

    val km = if (distancePriority == "High") 2.0 else if (distancePriority == "Medium") 5.0 else 10.0

    val queryItems = tree.rangeQuery(
        userPoint.offsetBounded(km2latlng(km, -km)),
        userPoint.offsetBounded(km2latlng(-km, km))
    )

    val result = ArrayList<Result>()
    for (item in queryItems) {
        val searchRatio = FuzzySearch.partialRatio(searchValue, item.itemName)
        if (searchRatio > SEARCH_THRESHOLD) {
            result.add(itemToResult(item))
        }
    }

    if (result.size == 0) return result

    val FINAL_ARRAY = ArrayList<Result>(numQueries)
    for (iter in 1..numQueries) {
        var localMinimum: Result = result[0]
        var localMinimumIdx = 0
        for (idx in 1 until result.size) {
            if (result[idx].price < localMinimum.price) {
                localMinimum = result[idx]
                localMinimumIdx = idx
            }
        }
        result[localMinimumIdx] = Result("", Double.MAX_VALUE, "")
        FINAL_ARRAY.add(localMinimum)
    }

    return FINAL_ARRAY
}