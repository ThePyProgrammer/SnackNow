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
fun parseKML(filename: String): ArrayList<Place> {

    val whyDoesJavaHaveSoManyFactories: XMLInputFactory = XMLInputFactory.newInstance()
    val places = ArrayList<Place>()
    var latitude = 0.0
    var longitude = 0.0
    var address = ""
    var type: PlaceType = PlaceType.NONE
    try {
        val fis = FileInputStream(filename)
        val reader: XMLEventReader = whyDoesJavaHaveSoManyFactories.createXMLEventReader(fis)
        var ended = false
        while (reader.hasNext() && !ended) {
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
                        longitude = splitted[0].toDouble()
                        latitude = splitted[1].toDouble()
                    }
                    "SimpleData" -> {
                        val name: Attribute? = startElement.getAttributeByName(QName("name"))
                        if (name != null) {
                            when (name.value) {
                                // hawker-centres.kml
                                "ADDRESS_MYENV" -> {
                                    nextEvent = reader.nextEvent()
                                    address = nextEvent.asCharacters().data
                                    type = PlaceType.HAWKER_CENTRE
                                }
                                // supermarkets.kml
                                "BLK_HOUSE" -> {
                                    nextEvent = reader.nextEvent()
                                    address = nextEvent.asCharacters().data
                                    type = PlaceType.SUPERMARKET
                                }
                                "STR_NAME" -> {
                                    nextEvent = reader.nextEvent()
                                    address += " ${nextEvent.asCharacters().data}"
                                }
                                "UNIT_NO" -> { // to remove
                                    nextEvent = reader.nextEvent()
                                    address += " #01-${nextEvent.asCharacters().data}"
                                }
                                "POSTCODE" -> {
                                    nextEvent = reader.nextEvent()
                                    address += ", Singapore ${nextEvent.asCharacters().data}"
                                }
                            }
                        }
                    }
                }
            }
            if (nextEvent.isEndElement) {
                val endElement: EndElement = nextEvent.asEndElement()
                when (endElement.name.localPart) {
                    "Placemark" -> {
                        // end of one location
                        val place = Place(address, latitude, longitude, type)
                        places.add(place)
                    }
                    "kml" -> {
                        // end of everything
                        ended = true
                    }
                }
            }
        }
    } catch (e: FileNotFoundException) {
        e.printStackTrace()
    } catch (e: NumberFormatException) {
        e.printStackTrace()
    }

    return places

}

fun hawkerCentres(): ArrayList<Place> {
    return parseKML("data/hawker-centres.kml")
}

fun supermarkets(): ArrayList<Place> {
    return parseKML("data/supermarkets.kml")
}