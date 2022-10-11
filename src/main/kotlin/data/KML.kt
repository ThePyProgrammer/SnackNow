package data

import model.base.Location
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.lang.Double.parseDouble
import javax.xml.namespace.QName
import javax.xml.stream.XMLEventReader
import javax.xml.stream.XMLInputFactory
import javax.xml.stream.events.Attribute
import javax.xml.stream.events.EndElement
import javax.xml.stream.events.StartElement
import javax.xml.stream.events.XMLEvent


fun parseKML(filename: String): ArrayList<Location> {

    val whyDoesJavaHaveSoManyFactories: XMLInputFactory = XMLInputFactory.newInstance()
    val locations = ArrayList<Location>()
    try {

        val fis = FileInputStream(filename)
        val reader: XMLEventReader = whyDoesJavaHaveSoManyFactories.createXMLEventReader(fis)


        while (reader.hasNext()) {
            var nextEvent: XMLEvent = reader.nextEvent()
            var latitude = 0.0
            var longitude = 0.0
            var address = ""
            if (nextEvent.isStartElement) {
                val startElement: StartElement = nextEvent.asStartElement()
                when (startElement.name.localPart) {
                    "placemark" -> {
                        // start
                    }
                    "point" -> {
                        nextEvent = reader.nextEvent()
                        val point: String = nextEvent.asCharacters().data
                        val splitted = point.split(",")
                        latitude = parseDouble(splitted[0])
                        longitude = parseDouble(splitted[1])
                    }
                    "simpledata" -> {
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
                if (endElement.name.localPart.equals("placemark")) {
                    val location = Location(address, latitude, longitude, null)
                    locations.add(location)
                }
            }
        }
    } catch (e: FileNotFoundException) {
        e.printStackTrace()
    }

    return locations

}