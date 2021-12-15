package day15

import support.AdventOfCode
import support.Coord
import support.Grid
import support.getCoord
import support.toGrid
import support.validCoord

const val MAX_VALUE = 1000000

class Day15 : AdventOfCode {
    private fun getInputText(): String = Day15::class.java.getResource("input.txt")?.readText()!!

    override fun day() = "15"

    override fun part1(): Int {
        val cave: List<List<Int>> = getInputText().toGrid()
        return findLowestRiskPath(cave)
    }

    fun findLowestRiskPath(cave: Grid<Int>): Int {
        val unvisitedNodes: MutableSet<Coord> = mutableSetOf()
        val distances: MutableMap<Coord, Int> = mutableMapOf()
        val visitedNodes: MutableSet<Coord> = mutableSetOf()

        distances[Coord(0, 0)] = 0

        var currentNode = Coord(0, 0)

        val finalCoord = Coord(cave[0].size - 1, cave.size - 1)

        while (currentNode != finalCoord) {
            val currentDistance = distances[currentNode]!!
            listOf(currentNode.previousX(), currentNode.nextX(), currentNode.previousY(), currentNode.nextY()).forEach {
                if (!visitedNodes.contains(it) && cave.validCoord(it)) {
                    val neighbourWeight = cave.getCoord(it)
                    val neighbourDistance = distances[it]
                    if (neighbourDistance == null || currentDistance + neighbourWeight < neighbourDistance) {
                        distances[it] = currentDistance + neighbourWeight
                    }
                    unvisitedNodes.add(it)
                }
            }
            unvisitedNodes.remove(currentNode)
            visitedNodes.add(currentNode)

            currentNode = unvisitedNodes.minByOrNull { distances[it] ?: MAX_VALUE }!!
        }

        return distances[finalCoord]!!
    }

    override fun part2(): Int {
        val originalCave: Grid<Int> = getInputText().toGrid()

        val cave: MutableList<MutableList<Int>> = mutableListOf()

        for (repeat in (0..4)) {
            cave.addAll(originalCave.map { row -> row.map { if (it + repeat > 9) it + repeat - 9 else it + repeat }.toMutableList() })
        }

        for (i in cave.indices) {
            val savedRow = cave[i].toList()
            for (repeat in (1..4)) {
                cave[i].addAll(savedRow.map { if (it + repeat > 9) it + repeat - 9 else it + repeat })
            }
        }

//        cave.forEach { println(it.joinToString("")) }

        return findLowestRiskPath(cave)
    }
}

fun main() {
    Day15().run()
}
