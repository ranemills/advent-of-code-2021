package day9

const val DAY = "9"

class Main {
    private fun getInputText(): String = Main::class.java.getResource("input.txt")?.readText()!!

    private val cave = getInputText().split("\n").map { it.chunked(1).map { it.toInt() } }

    fun part1(): Int {
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

    fun part2(): Int {
        val basinRoots = cave.indices.flatMap { y -> List(cave[0].size) { i -> Pair(y, i) } }.map {
            Coord(it)
        }.filter {
            val (x, y) = it
            val square = cave[x][y]
            val up = if (y == 0) Int.MAX_VALUE else cave[x][y - 1]
            val down = if (y == cave[0].size - 1) Int.MAX_VALUE else cave[x][y + 1]
            val left = if (x == 0) Int.MAX_VALUE else cave[x - 1][y]
            val right = if (x == cave.size - 1) Int.MAX_VALUE else cave[x + 1][y]

            square < up && square < down && square < left && square < right
        }

        val basinSizes = mutableListOf<Int>()
        val visitedCoords = mutableSetOf<Coord>()

        for (basinRoot in basinRoots) {
            val basinCoords: Set<Coord> = getBasinCoords(basinRoot.x, basinRoot.y, setOf(), visitedCoords)
            visitedCoords.addAll(basinCoords)
            basinSizes.add(basinCoords.size)
        }
//            return basinRoots.map {
//                getBasinCoords(it.x, it.y, setOf(),)
//            }
//            .also {
//                println(it)
//            }
//            .sortedByDescending {
//                it.size
//            }
        return basinSizes.sortedByDescending { it }.take(3)
            .fold(1) { acc, size -> acc * size }
    }

    fun getBasinCoords(x: Int, y: Int, visitedCoords: Set<Coord>, alreadyVisitedCoords: Set<Coord>): Set<Coord> {
        if (cave[x][y] == 9) return emptySet()

        val nowVisitedCoords = (visitedCoords + Coord(x, y))

        val toVisit = (
            setOf(
                Coord(x to y - 1),
                Coord(x to y + 1),
                Coord(x + 1 to y),
                Coord(x - 1 to y)
            ) - visitedCoords - alreadyVisitedCoords
            ).filter {
            it.x >= 0 && it.x < cave.size && it.y >= 0 && it.y < cave[0].size && cave[it.x][it.y] != 9
        }

        return nowVisitedCoords + toVisit.map {
            getBasinCoords(it.x, it.y, nowVisitedCoords, alreadyVisitedCoords)
        }.flatten().toSet()
    }
}

data class Coord(
    val x: Int,
    val y: Int
) {
    constructor(pair: Pair<Int, Int>) : this(pair.first, pair.second)
}

fun main() {
    println("Day $DAY")
    println("Part 1")
    println(Main().part1())
    println("Part 2")
    println(Main().part2())
}
