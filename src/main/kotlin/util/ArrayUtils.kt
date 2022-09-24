package util

import java.lang.Float.max
import java.lang.Float.min

inline fun <reified T> Array<T?>.replaceAllNulls(replacement: T): Array<T> = this.map { it ?: replacement }.toTypedArray()

inline fun <reified T, reified S, reified R> Array<T>.mapEach(other: Array<S>, f: (T, S) -> R) = this.zip(other).map { f(it.first, it.second) }.toTypedArray()

inline fun Array<Float>.addEach(other: Array<Float>) = this.mapEach(other) { first, second -> first + second }

inline fun Array<Float>.subtractEach(other: Array<Float>) = this.mapEach(other) { first, second -> first - second }

inline fun Array<Float>.minEach(other: Array<Float>) = this.mapEach(other) { first, second -> min(first, second) }

inline fun Array<Float>.maxEach(other: Array<Float>) = this.mapEach(other) { first, second -> max(first, second) }

inline fun <reified T, reified S> Array<Pair<T, S>>.split(): Pair<Array<T>, Array<S>> = this.map { it.first }.toTypedArray() to this.map { it.second }.toTypedArray()

inline fun <reified T> Array<T>.getPairs(): Array<Pair<T, T>> = run {
    map { n1 ->
        this.filter { n1 != it }.map { n2 -> n1 to n2 }.toTypedArray()
    }.toTypedArray().fold(arrayOf()) { a, b -> a + b }
}

inline fun <reified T> List<T>.getPairs(): List<Pair<T, T>> = run {
    map { n1 ->
        this.filter { n1 != it }.map { n2 -> n1 to n2 }
    }.fold(listOf()) { a, b -> a + b }
}

inline fun <reified T> Pair<T,T>.toArray(): Array<T> {
    return arrayOf(first, second)
}