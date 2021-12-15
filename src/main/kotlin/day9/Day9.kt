package day9

import support.AdventOfCode

const val DAY = "9"

class Day9 : AdventOfCode {
    private fun getInputText(): String = Day9::class.java.getResource("input.txt")?.readText()!!

    private val cave = getInputText().split("\n").map { it.chunked(1).map { it.toInt() } }
    private val caveXSize = cave.size
    private val caveYSize = cave[0].size

    override fun day(): String = "9"

    override fun part1(): Int {
        val cave = getInputText().split("\n").map { it.chunked(1).map { it.toInt() } }
        return cave.indices.flatMap { y -> List(cave[0].size) { i -> Pair(y, i) } }.sumOf {
            val (x, y) = it
            val square = cave[x][y]
            val up = if (y == 0) Int.MAX_VALUE else cave[x][y - 1]
            val down = if (y == cave[0].size - 1) Int.MAX_VALUE else cave[x][y + 1]
            val left = if (x == 0) Int.MAX_VALUE else cave[x - 1][y]
            val right = if (x == cave.size - 1) Int.MAX_VALUE else cave[x + 1][y]

            if (square < up && square < down && square < left && square < right) {
                cave[x][y] + 1
            } else {
                0
            }
        }
    }

    override fun part2(): Int = cave.indices
        .flatMap { y -> List(cave[0].size) { i -> Coord(y, i) } }
        .filter {
            val (x, y) = it
            val square = cave[x][y]
            val up = if (y == 0) Int.MAX_VALUE else cave[x][y - 1]
            val down = if (y == cave[0].size - 1) Int.MAX_VALUE else cave[x][y + 1]
            val left = if (x == 0) Int.MAX_VALUE else cave[x - 1][y]
            val right = if (x == cave.size - 1) Int.MAX_VALUE else cave[x + 1][y]

            square < up && square < down && square < left && square < right
        }
        .map { basinRoot -> getBasinCoords(basinRoot.x, basinRoot.y, setOf()).size }
        .sortedDescending()
        .take(3)
        .fold(1) { acc, size -> acc * size }

    private fun getBasinCoords(x: Int, y: Int, visitedCoords: Set<Coord>): Set<Coord> =
        setOf(
            Coord(x to y - 1),
            Coord(x to y + 1),
            Coord(x + 1 to y),
            Coord(x - 1 to y)
        ).filter {
            !visitedCoords.contains(it) && it.x in 0 until caveXSize && it.y in 0 until caveYSize && cave[it.x][it.y] != 9
        }.fold((visitedCoords + Coord(x, y))) { acc, coord ->
            acc + getBasinCoords(coord.x, coord.y, acc)
        }
}

data class Coord(
    val x: Int,
    val y: Int
) {
    constructor(pair: Pair<Int, Int>) : this(pair.first, pair.second)
}

fun main() {
    Day9().run()
}
