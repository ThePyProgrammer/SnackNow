package data

import model.base.Location
import java.io.IOException

fun main() {

    val allData = ArrayList<Location>()

    val someData: ArrayList<Location> = parseKML("data/hawker-centres.kml")
    for (location in someData) {
        allData.add(location)
    }

    val someData2: ArrayList<Location> = parseKML("data/supermarkets.kml")
    for (location in someData2) {
        allData.add(location)
    }

    try {
        val someMoreData: ArrayList<Location> = sevenElevens()
        for (location in someMoreData) {
            allData.add(location)
        }
    } catch (e: IOException) {
        throw RuntimeException(e)
    }

    for (location in allData) {
        println(location.toString())
    }

    println("Total number of locations: %d".format(allData.size))

}