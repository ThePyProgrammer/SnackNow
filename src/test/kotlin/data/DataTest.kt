package data

import java.io.IOException

fun main() {

    val allData = ArrayList<Place>()

    val someData: ArrayList<Place> = parseKML("data/hawker-centres.kml")
    for (place in someData) {
        allData.add(place)
    }

    val someData2: ArrayList<Place> = parseKML("data/supermarkets.kml")
    for (place in someData2) {
        allData.add(place)
    }

    try {
        val someMoreData: ArrayList<Place> = sevenElevens()
        for (place in someMoreData) {
            allData.add(place)
        }
    } catch (e: IOException) {
        throw RuntimeException(e)
    }

    for (place in allData) {
        println(place.toString())
    }

    println("Total number of locations: %d".format(allData.size))

}