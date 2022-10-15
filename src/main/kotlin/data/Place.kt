package data

import model.algorithm.Item
import model.base.Location
import model.base.Point

class Place(
    val address: String = "",
    val longitude: Double = 0.0,
    val latitude: Double = 0.0,
    val type: PlaceType = PlaceType.NONE, // extra thing I need
) {

    val location = Location(address, latitude, longitude, getURL(latitude, longitude))
    val point = Point(longitude, latitude)
    val items = ArrayList<Item>() // extra thing I need

}

enum class PlaceType {
    NONE,
    SEVEN_ELEVEN,
    HAWKER_CENTRE,
    SUPERMARKET,
}