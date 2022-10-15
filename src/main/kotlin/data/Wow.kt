package data

import model.base.Location
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException
import java.net.URL
import java.util.regex.Pattern

@Throws(IOException::class)
fun sevenElevens(): ArrayList<Place> {
    val result = ArrayList<Place>()
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
                        line = br.readLine()
                        continue
                    }
                    val longitude = matcher.group(4).toDouble()
                    val place = Place(matcher.group(1), latitude, longitude, PlaceType.SEVEN_ELEVEN)
                    result.add(place)
                } // else?
                line = br.readLine()
            }
        }
    } catch (exception: NumberFormatException) {
        exception.printStackTrace()
    }
    // the buffered reader auto closes after this try
    return result
}

fun getURL(latitude: Double, longitude: Double): URL {
    return URL("https://google.com/maps/search/?api=1&query=%.9f,%.9f".format(latitude, longitude))
}