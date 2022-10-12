package data

import model.base.Location
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException
import java.net.URL
import java.util.regex.Pattern

fun main() {
    try {
        val someData: ArrayList<Location> = sevenElevens()
        for (location in someData) {
            println(location.toString())
        }
    } catch (e: IOException) {
        throw RuntimeException(e)
    }
}

@Throws(IOException::class)
fun sevenElevens(): ArrayList<Location> {
    val result = ArrayList<Location>()
    val pattern =
        Pattern.compile("showlocation\\(\"([\\w#.,'/@() \\-]+)\", \"([\\w#.,'/@() \\-]+)\", ([\\d.]+), ([\\d.]+)\\);")

    // load 7-11.txt
    try {
        BufferedReader(FileReader("data/7-11.txt")).use { br ->
            var line = br.readLine()
            while (line != null) {
                val matcher = pattern.matcher(line)
                if (matcher.matches()) {
                    val latitude = matcher.group(3).toDouble()
                    if (latitude == 0.0) {
                        // check to remove funny africa point
                        continue
                    }
                    val longitude = matcher.group(4).toDouble()
                    val location =
                        Location(matcher.group(1), latitude, longitude, URL("https://google.com/maps/search/?api=1&query=%.9f,%.9f".format(latitude, longitude)))
                    result.add(location)
                } else {
                    println("skill issue") // replace with error or better text
                }
                line = br.readLine()
            }
        }
    } catch (exception: NumberFormatException) {
        exception.printStackTrace()
    }
    // the buffered reader auto closes after this try
    return result
}