package data

import model.base.Location
import java.io.FileInputStream
import java.io.FileNotFoundException
import javax.xml.namespace.QName
import javax.xml.stream.XMLEventReader
import javax.xml.stream.XMLInputFactory
import javax.xml.stream.events.Attribute
import javax.xml.stream.events.EndElement
import javax.xml.stream.events.StartElement
import javax.xml.stream.events.XMLEvent


// returns a list of locations
fun parseKML(filename: String): ArrayList<Location> {

    val whyDoesJavaHaveSoManyFactories: XMLInputFactory = XMLInputFactory.newInstance()
    val locations = ArrayList<Location>()
    var latitude = 0.0
    var longitude = 0.0
    var address = ""
    try {
        val fis = FileInputStream(filename)
        val reader: XMLEventReader = whyDoesJavaHaveSoManyFactories.createXMLEventReader(fis)
        while (reader.hasNext()) {
            var nextEvent: XMLEvent = reader.nextEvent()
            if (nextEvent.isStartElement) {
                val startElement: StartElement = nextEvent.asStartElement()
                // print(startElement.name.localPart)
                when (startElement.name.localPart) {
                    "Placemark" -> {
                        // start
                    }
                    "coordinates" -> {
                        nextEvent = reader.nextEvent()
                        val point: String = nextEvent.asCharacters().data
                        val splitted = point.split(",")
                        latitude = splitted[0].toDouble()
                        longitude = splitted[1].toDouble()
                    }
                    "SimpleData" -> {
                        val name: Attribute? = startElement.getAttributeByName(QName("name"))
                        if (name != null) {
                            if (name.value.equals("ADDRESS_MYENV")) {
                                nextEvent = reader.nextEvent()
                                address = nextEvent.asCharacters().data
                            }
                        }
                    }
                }
            }
            if (nextEvent.isEndElement) {
                val endElement: EndElement = nextEvent.asEndElement()
                when (endElement.name.localPart) {
                    "Placemark" -> {
                        // end
                        val location = Location(address, latitude, longitude, null)
                        locations.add(location)
                    }
                }
            }
        }
    } catch (e: FileNotFoundException) {
        e.printStackTrace()
    } catch (e: NumberFormatException) {
        e.printStackTrace()
    }

    return locations

}