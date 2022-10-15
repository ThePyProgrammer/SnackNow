import data.getLocationFromPostalCode
import model.base.Location
import util.initialize
import util.search

fun main() {
    initialize()
    val result = search("Chip",
        getLocationFromPostalCode("238801")!!,
        10, "High")

    println(result)
}