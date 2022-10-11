package data

import model.base.Location
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException
import java.util.regex.Pattern

fun main() {
    try {
        val someData: ArrayList<Location> = convenienceStores
        print(someData)
    } catch (e: IOException) {
        throw RuntimeException(e)
    }
}

@get:Throws(IOException::class)
val convenienceStores: ArrayList<Location>
    get() {
        val result = ArrayList<Location>()
        val pattern =
            Pattern.compile(" showlocation\\(\"([\\w#.,'/@() \\-]+)\", \"([\\w#.,'/@() \\-]+)\", ([\\d.]+), ([\\d.]+)\\);")

        // load 7-11.txt
        try {
            BufferedReader(FileReader("data/7-11.txt")).use { br ->
                var line = br.readLine()
                while (line != null) {
                    val matcher = pattern.matcher(line)
                    if (matcher.matches()) {
                        val location =
                            Location(matcher.group(1), matcher.group(3).toDouble(), matcher.group(4).toDouble(), null)
                        result.add(location)
                    } else {
                        println("skill issue") // replace with error or better text
                    }
                    line = br.readLine()
                }
            }
        } catch (exception: NumberFormatException) {
            exception.printStackTrace()
        } /* finally {
            // finally!
        } */
        // the buffered reader auto closes after this try
        return result
    }