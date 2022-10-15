// unfortunately, there is no better name for this file. wow!
// contains miscellaneous functions (some of them related to ?SV file loading)

package data

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import model.base.Location
import java.io.BufferedReader
import java.io.File
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

private val postalCodeThingy = mutableMapOf<Int, Location>()

fun initPostalCodeThingy() {
    try {
        val file = File("data/database.csv")
        val rows: List<List<String>> = csvReader().readAll(file)
        for (line: List<String> in rows) {
            val address = line[0]
            if (address == "ADDRESS") {
                continue
            }
            val longitude = line[4].toDouble()
            val latitude = line[3].toDouble()
            val postalCode = line[5].toInt()
            postalCodeThingy[postalCode] = Location(address, latitude, longitude, getURL(latitude, longitude))
        }
    } catch (exception: NumberFormatException) {
        exception.printStackTrace()
    }
}

fun getLocationFromPostalCode(postalCode: String): Location? {
    return try {
        postalCodeThingy[postalCode.toInt()]
    } catch (e: Exception) {
        null
    }
}

// [                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               ]

fun getPrices(): ArrayList<FakeDataItem> {
    val result = ArrayList<FakeDataItem>()
    try {
        val file = File("data/prices.csv")
        val rows: List<List<String>> = csvReader().readAll(file)
        for (line: List<String> in rows) {
            val year = line[0]
            if (year != "2020") {
                continue
            }
            val name = line[1]
            val price = line[2].toDouble()
            result.add(FakeDataItem(name, price))
        }
    } catch (exception: NumberFormatException) {
        exception.printStackTrace()
    }
    return result
}