package day15

import java.util.Collections.min

const val DAY = "15"

typealias Coord = Pair<Int, Int>

fun Coord.nextX() = Coord(first + 1, second)
fun Coord.nextY() = Coord(first, second + 1)
fun Coord.x() = first
fun Coord.y() = second

class Main {
    private fun getInputText(): String = Main::class.java.getResource("input.txt")?.readText()!!

    val routeCache: MutableMap<Coord, Int> = mutableMapOf()

    fun part1(): Int {
        val cave: List<List<Int>> = getInputText().split("\n").map { it.chunked(1).map { it.toInt() }.toMutableList() }
        return findLowestRiskPath(cave, Coord(0, 0)) - cave[0][0]
    }

    fun findLowestRiskPath(cave: List<List<Int>>, point: Coord): Int {
        val riskValue = cave[point.second][point.first]

        return routeCache.getOrPut(point) {
            when {
                (point.x() == cave[0].size - 1 && point.y() == cave.size - 1) -> riskValue
                (point.x() == cave[0].size - 1) -> riskValue + findLowestRiskPath(cave, point.nextY())
                (point.y() == cave.size - 1) -> riskValue + findLowestRiskPath(cave, point.nextX())
                else -> riskValue + min(
                    listOf(
                        findLowestRiskPath(cave, point.nextX()),
                        findLowestRiskPath(cave, point.nextY())
                    )
                )
            }
        }
    }

    fun part2(): Int {
        routeCache.clear()
        val originalCave: List<List<Int>> = getInputText().split("\n").map { it.chunked(1).map { it.toInt() }.toMutableList() }

        val cave: MutableList<MutableList<Int>> = mutableListOf()

        for (repeat in (0..4)) {
            cave.addAll(originalCave.map { it.map { if (it + repeat > 9) it + repeat - 9 else it + repeat }.toMutableList() })
        }

        for (i in cave.indices) {
            val savedRow = cave[i].toList()
            for (repeat in (1..4)) {
                cave[i].addAll(savedRow.map { if (it + repeat > 9) it + repeat - 9 else it + repeat })
            }
        }

//        cave.forEach { println(it.joinToString("")) }
        println(cave[0][0])
        println(cave[100][0])
        println(cave[200][0])
        println(cave[300][0])
        println(cave[400][0])
        println()

        println(cave[0][100])
        println(cave[100][100])
        println(cave[200][100])
        println(cave[300][100])
        println(cave[400][100])
        println()

        println(cave[0][200])
        println(cave[100][200])
        println(cave[200][200])
        println(cave[300][200])
        println(cave[400][200])
        println()

        println(cave[0][300])
        println(cave[100][300])
        println(cave[200][300])
        println(cave[300][300])
        println(cave[400][300])
        println()

        println(cave[0][400])
        println(cave[100][400])
        println(cave[200][400])
        println(cave[300][400])
        println(cave[400][400])
        println()


        val retVal = findLowestRiskPath(cave, Coord(0, 0)) - cave[0][0]
        return retVal
    }
}

fun main() {
    println("Day $DAY")
    println("Part 1")
    println(Main().part1())
    println("Part 2")
    println(Main().part2())
}
