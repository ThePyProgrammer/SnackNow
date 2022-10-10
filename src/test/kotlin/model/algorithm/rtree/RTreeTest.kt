package model.algorithm.rtree

fun fillCoords(x: Float, y: Float = x) = arrayOf(x, y)

val ZEROS = fillCoords(0.0f)
val ONES = fillCoords(1.0f)
val POINT_FIVES = fillCoords(0.5f)
val NEG_ONES = fillCoords(-1.0f)
val NEG_POINT_FIVES = fillCoords(-0.5f)

fun testCreation() {
    val rt = RTree<Float>()
}

fun testInsertPoint() {
    val rt = RTree<Float>()
    val price = 3.5f
    rt.insert(ZEROS, ZEROS, price)
    val results: List<Float> = rt.search(NEG_ONES, fillCoords(2.0f))
    for(result in results) print("$result ")
    println()
}

fun testInsertRect() {
    val rt = RTree<Float>()
    val price = 3.5f
    rt.insert(ZEROS, ONES, price)
    val results: List<Float> = rt.search(NEG_ONES, fillCoords(3.0f))
    for(result in results) print("$result ")
    println()
}

fun testInsertNegativeCoords() {
    val rt = RTree<Float>().apply {
        insert(NEG_ONES, POINT_FIVES, 5.5F)
        insert(POINT_FIVES, ONES, 3.5F)
    }
    var results: List<Float> = rt.search(NEG_POINT_FIVES, ZEROS)
    for(result in results) print("$result ")
    println()
    results = rt.search(POINT_FIVES, ZEROS)
    for(result in results) print("$result ")
    println()
}

fun testEmpty() {
    val rt = RTree<Float>().apply { insert(ZEROS, ZEROS, 4.5F) }
    val results: List<Float> = rt.search(fillCoords(-1.0f), fillCoords(0.5f))
    println(results.size)
}

fun expSetUp(seedPicker: RTree.SeedPicker = RTree.SeedPicker.LINEAR) = RTree<Int>(2, 1, 2, seedPicker).apply {
    for(i in 0..3) {
        insert(fillCoords(i.toFloat()), fillCoords(0.5f), i)
    }
}

fun testSplitNodeSmall() {
    val rt = expSetUp(RTree.SeedPicker.QUADRATIC)
    val results: List<Int> = rt.search(fillCoords(2.0f), fillCoords(0.5f))
    for(result in results) print("$result ")
    println()
}

fun testRemoveAll() {
    val rt = expSetUp()
    val sCoords = fillCoords(-0.5f * Float.MAX_VALUE)
    val sDims = fillCoords(Float.MAX_VALUE)
    val results: List<Int> = rt.search(sCoords, sDims)
}



fun main() {
    println("========= TEST CREATION =========")
    testCreation()
    println("====== TEST POINT INSERTION =====")
    testInsertPoint()
    println("====== TEST POINT RECTANGLE =====")
    testInsertRect()
    println("====== TEST NEGATIVE POINTS =====")
    testInsertNegativeCoords()
    println("======= TEST EMPTY RESULTS ======")
    testEmpty()
    println("======== TEST SPLIT NODES =======")
    testSplitNodeSmall()

}


