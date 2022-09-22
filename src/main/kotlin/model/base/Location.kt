package model.base

import java.net.URL

data class Location(
    val name: String,
    val lat: Double, val lng: Double,
    val url: URL
)