package data

// list structure: [
//     ["name", price, include chance, fake price variation between places (optional, default is 10% of price), smallest interval of variation (optional, default 10)]
// ]

private val FAKE_7_11 = listOf(

    listOf("Cheesy Beef Burger", 2.90, 0.90, 0.20),
    listOf("Cheesy Chicken Burger", 2.90, 0.90, 0.30),
    listOf("Spicy Chicken Burger", 2.90, 0.90, 0.40),
    listOf("Fish Burger", 2.90, 0.90, 0.50),
    listOf("Chocolate Lava Cake", 4.00, 0.90, 1.00),
    // sand (the only thing I can find prices for, and the prices are real)
    listOf("Chicken Ham with Cheese Sandwich", 3.40, 0.95, 0.00),
    listOf("Club Sandwich", 3.60, 0.95, 0.00),
    listOf("Double Combo Sandwich (Chicken Ham with Cheese + Egg with Cheese)", 3.70, 0.95, 0.00),
    listOf("Double Combo Sandwich (Chicken Katsu + Egg and Potato Salad)", 3.70, 0.95, 0.00),
    listOf("Egg and Cheese Sandwich", 3.20, 0.95, 0.00),
    listOf("Honey Mustard Roasted Chicken Sandwich", 3.70, 0.95, 0.00),
    listOf("Prawn Salad Sandwich", 3.60, 0.95, 0.00),
    listOf("Triple Combo Sandwich (Tuna + Chicken Ham with Cheese + Egg Mayo)", 3.90, 0.95, 0.00),
    listOf("Tuna Sandwich", 3.40, 0.95, 0.00),
    listOf("Slurpee", 1.60, 1.00, 0.20),
    listOf("Mr. Softee", 1.50, 0.90, 0.20),

    // totally fake things
    listOf("Coffee", 1.50, 0.50, 0.01), // for fun, price is not real
    listOf("Tea", 1.50, 0.50, 0.02), // T
    listOf("Coffee and Tea", 3.00, 2.00, 0.03), // test
    listOf("Among Us Sandwich", 1000.00, 0.10, 999.99, 0.01),
    listOf("7-SELECT 100% FRESH AIR", 10.00, 0.50, 0.00),
    listOf("Black Body Radiation", 0.01, 0.10, 0.00),

)

fun fakeData() {



}