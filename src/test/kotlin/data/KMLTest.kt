package data

fun main() {
    for (location in parseKML("data/hawker-centres.kml")) {
        println(location.toString())
    }
}