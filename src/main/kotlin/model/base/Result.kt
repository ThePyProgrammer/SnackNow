package model.base

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import org.jetbrains.skia.Image
import java.io.ByteArrayOutputStream
import java.net.HttpURLConnection
import java.net.URL
import javax.imageio.ImageIO

data class Result(val name: String, val price: Double, val location: String) {
//    Insert api key
   val key = "pkdoteyJ1IjoibnVsbHN0ZWxsZW5zYXR6IiwiYSI6ImNsOGxmbnM1eTAxNWEzb21lbXQxcDN6ODAifQdotfNfcF1kzz9OSd0gxVMk7Ag".replace("dot",".")
    private fun loadNetworkImage(link: String): ImageBitmap {
        val url = URL(link)
        val connection = url.openConnection() as HttpURLConnection
        connection.connect()

        val inputStream = connection.inputStream
        val bufferedImage = ImageIO.read(inputStream)

        val stream = ByteArrayOutputStream()
        ImageIO.write(bufferedImage, "png", stream)
        val byteArray = stream.toByteArray()

        return Image.makeFromEncoded(byteArray).toComposeImageBitmap()
    }
    fun getImage(long: Double = -122.4241,lat: Double = 37.78): ImageBitmap {
        return loadNetworkImage("https://api.mapbox.com/styles/v1/mapbox/streets-v11/static/${long},${lat},15.25,0,60/400x400?access_token=${key}"
        )
    }
}