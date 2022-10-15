package data

data class FakeDataItem(
    val name: String,
    val price: Double,
    val chance: Double = 0.90, // the chance this item would appear in a store
    val uncertainty: Double = price * 0.1, // the fake price variation between places
    val step: Double = 0.10, // the smallest interval of price variation (round value)
)