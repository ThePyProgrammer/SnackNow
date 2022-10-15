package util

import java.lang.Double.max
import java.lang.Double.min

inline fun <reified T> Array<T?>.replaceAllNulls(replacement: T): Array<T> = this.map { it ?: replacement }.toTypedArray()

inline fun <reified T, reified S, reified R> Array<T>.mapEach(other: Array<S>, f: (T, S) -> R) = this.zip(other).map { f(it.first, it.second) }.toTypedArray()

inline fun Array<Double>.addEach(other: Array<Double>) = this.mapEach(other) { first, second -> first + second }

inline fun Array<Double>.subtractEach(other: Array<Double>) = this.mapEach(other) { first, second -> first - second }

inline fun Array<Double>.minEach(other: Array<Double>) = this.mapEach(other) { first, second -> min(first, second) }

inline fun Array<Double>.maxEach(other: Array<Double>) = this.mapEach(other) { first, second -> max(first, second) }

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

fun Array<Double>.product() = fold(1.0) { a, b -> a*b }

data class Quartet<P, Q, R, S>(var first: P, var second: Q, var third: R, var fourth: S)

fun <P, Q, R, S> quartetZip(first: Array<P>, second: Array<Q>, third: Array<R>, fourth: Array<S>) =
    first.zip(second).zip(third.zip(fourth)).map { Quartet(it.first.first, it.first.second, it.second.first, it.second.second) }

fun <T, S: Comparable<S>> Array<T>.minOfAndByOrNull(func: (T) -> S): Pair<T, S>? = map { it to func(it) }.minByOrNull { it.second }

fun <T, S: Comparable<S>> Array<T>.maxOfAndByOrNull(func: (T) -> S): Pair<T, S>? = map { it to func(it) }.maxByOrNull { it.second }

fun zeros(dim: Int) = arrayOfNulls<Double>(dim).replaceAllNulls(0.0)