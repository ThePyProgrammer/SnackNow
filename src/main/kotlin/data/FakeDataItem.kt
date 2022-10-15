package data

data class FakeDataItem(
    val name: String,
    val price: Double,
    val chance: Double = 0.90,
    val uncertainty: Double = price * 0.1,
    val step: Double = 0.10,
)