package data

import model.algorithm.Item
import model.base.Location
import kotlin.math.round
import kotlin.random.Random

// list structure: [
//     ["name", price, include chance, fake price variation between places (optional, default is 10% of price), smallest interval of variation (optional, default 10)]
// ]

private const val SEED = 123456789
private val random = Random(SEED)

private val FAKE_7_11 = mutableListOf(

    FakeDataItem("Cheesy Beef Burger", 2.90, 0.90, 0.20),
    FakeDataItem("Cheesy Chicken Burger", 2.90, 0.90, 0.30),
    FakeDataItem("Spicy Chicken Burger", 2.90, 0.90, 0.40),
    FakeDataItem("Fish Burger", 2.90, 0.90, 0.50),
    FakeDataItem("Chocolate Lava Cake", 4.00, 0.90, 1.00),

    // which sands? (the only thing I can find prices for, and the prices are real)
    FakeDataItem("Chicken Ham with Cheese Sandwich", 3.40, 0.95, 0.00),
    FakeDataItem("Club Sandwich", 3.60, 0.95, 0.00),
    FakeDataItem("Double Combo Sandwich (Chicken Ham with Cheese + Egg with Cheese)", 3.70, 0.95, 0.00),
    FakeDataItem("Double Combo Sandwich (Chicken Katsu + Egg and Potato Salad)", 3.70, 0.95, 0.00),
    FakeDataItem("Egg and Cheese Sandwich", 3.20, 0.95, 0.00),
    FakeDataItem("Honey Mustard Roasted Chicken Sandwich", 3.70, 0.95, 0.00),
    FakeDataItem("Prawn Salad Sandwich", 3.60, 0.95, 0.00),
    FakeDataItem("Triple Combo Sandwich (Tuna + Chicken Ham with Cheese + Egg Mayo)", 3.90, 0.95, 0.00),
    FakeDataItem("Tuna Sandwich", 3.40, 0.95, 0.00),
    FakeDataItem("Slurpee", 1.60, 1.00, 0.20),
    FakeDataItem("Mr. Softee", 1.50, 0.90, 0.20),

    // totally fake things
    FakeDataItem("Coffee", 1.50, 0.50, 0.01), // for fun, price is not real
    FakeDataItem("Tea", 1.50, 0.50, 0.02), // T
    FakeDataItem("Coffee and Tea", 3.00, 2.00, 0.03), // test
    FakeDataItem("Some Food", 1.00, 0.15, 0.00),
    FakeDataItem("More Food", 1.00, 0.15, 0.00),
    FakeDataItem("Even More Food", 1.00, 0.15, 0.00),
    FakeDataItem("Snack Now!", 0.50, 0.25, 0.10),
    FakeDataItem("Snake Snack 🐍", 15.00, 0.10, 1.00, 0.01),
    FakeDataItem("Among Us Sandwich", 1000.00, 0.10, 999.99, 0.01),
    FakeDataItem("7-SELECT 100% FRESH AIR", 10.00, 0.50, 0.00),
    FakeDataItem("Black Body Radiation", 0.01, 0.10, 0.00),

)

private val FAKE_SUPERMARKET = mutableListOf(

    FakeDataItem("Generic Plastic Bag", 0.10, 0.50, 0.00),

)

/*
// set default values for list (never mind, I created a data class for it)
private fun defaultList(list: MutableList<MutableList<Any>>) {
    for (listitem in list) {
        if (listitem.size < 2) {
            print("error! item ${listitem[0]} has less than 2 'parameters'")
        } else if (listitem.size < 3) {
            listitem.addAll(
                0.90,
                listitem[3] = listitem[1] * 0.1,
                0.10,
            )
        }
    }
}
 */

fun insertFakeData(place: Place, dataList: MutableList<FakeDataItem>) {

    for (item in dataList) {
        if (item.chance > random.nextDouble()) {
            val price = if (item.uncertainty <= 0.0) {
                item.price
            } else {
                round(random.nextDouble(item.price - item.uncertainty, item.price + item.uncertainty) / item.step) * item.step
            }
            place.items.add(Item(item.name, place.address, place.point, price))
        }
    }

}

fun initFakeData(places: ArrayList<Place>) {

    for (place: Place in places) {
        when (place.type) {
            PlaceType.SEVEN_ELEVEN -> {
                insertFakeData(place, FAKE_7_11)
            }
            PlaceType.HAWKER_CENTRE -> {

            }
            PlaceType.SUPERMARKET -> {
                insertFakeData(place, FAKE_SUPERMARKET)
            }
        }
    }

}