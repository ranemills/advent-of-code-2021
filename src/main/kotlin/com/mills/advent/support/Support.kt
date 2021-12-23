package com.mills.advent.support

import kotlin.system.measureNanoTime

data class Coord(val x: Int, val y: Int) {
    constructor(pair: Pair<Int, Int>) : this(pair.first, pair.second)
    fun nextX() = Coord(x + 1, y)
    fun nextY() = Coord(x, y + 1)
    fun previousX() = Coord(x - 1, y)
    fun previousY() = Coord(x, y - 1)
    infix operator fun plus(other: Coord) = Coord(this.x+other.x, this.y+other.y)
    infix operator fun minus(other: Coord) = Coord(this.x-other.x, this.y-other.y)
}

typealias Grid<T> = List<List<T>>

fun String.toGrid(): Grid<Int> = this.split("\n").map { it.chunked(1).map { it.toInt() }.toMutableList() }
fun <T> Grid<T>.getCoord(x: Int, y: Int): T = this[y][x]
fun <T> Grid<T>.getCoord(coord: Coord): T = this[coord.y][coord.x]
fun Grid<*>.validCoord(coord: Coord) = coord.x in this[0].indices && coord.y in this.indices

fun allPairs(xSize: Int, ySize: Int) = allPairs(0, xSize, 0, ySize)
fun allPairs(xLowerBound: Int, xUpperBound: Int, yLowerBound: Int, yUpperBound: Int) =
    (yLowerBound..yUpperBound).flatMap { y -> (xLowerBound..xUpperBound).map { x -> Pair(x,y) } }

interface AdventOfCode {
    fun part1(): Any
    fun part2(): Any
    fun day(): String

    fun run() {
        println("Day ${day()}")
        val part1Time = measureNanoTime {
            println("Part 1")
            println(part1())
        }
        println("${part1Time / 1000000.toDouble()} ms")
        val part2Time = measureNanoTime {
            println("Part 2")
            println(part2())
        }
        println("${part2Time / 1000000.toDouble()} ms")
    }
}
