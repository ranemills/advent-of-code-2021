package com.mills.advent.twentyone

import com.mills.advent.support.AdventOfCode

val surrounding = listOf(
    Pair(1, -1),
    Pair(1, 0),
    Pair(1, 1),
    Pair(0, -1),
    Pair(0, 1),
    Pair(-1, -1),
    Pair(-1, 0),
    Pair(-1, 1)
)

class Day11 : AdventOfCode {
    private fun getInputText(): String = Day11::class.java.getResource("day11.txt")?.readText()!!

    override fun part1(): Int {
        val octopusGrid = getInputText().split("\n").map { it.chunked(1).map { it.toInt() }.toMutableList() }
        val allCoords = octopusGrid.indices.flatMap { y -> List(octopusGrid[0].size) { i -> Pair(y, i) } }
        return (1..100).sumOf { step ->
//            println("Step $step")
            val flashed = mutableSetOf<Pair<Int, Int>>()

            // increment
            allCoords.forEach { pair ->
                val (x, y) = pair
                octopusGrid[x][y]++
            }

//            for(row in octopusGrid) {
//                println(row)
//            }
//            println()

            // keep checking for nines
            while (allCoords.any { octopusGrid[it.first][it.second] > 9 && !flashed.contains(it) }) {
                allCoords.filter { octopusGrid[it.first][it.second] > 9 && !flashed.contains(it) }.forEach { pair ->
                    flashed.add(pair)
//                    print("Flashed $pair")
                    octopusGrid[pair.first][pair.second] = 0
                    surrounding.forEach { modifier ->
                        val x = pair.first + modifier.first
                        val y = pair.second + modifier.second
                        if (x in octopusGrid.indices && y in octopusGrid[0].indices && !flashed.contains(Pair(x, y))) octopusGrid[x][y]++
                    }
                }
            }

//            for (row in octopusGrid) {
//                println(row)
//            }
            flashed.size
        }
    }

    override fun part2(): Int {
        val octopusGrid = getInputText().split("\n").map { it.chunked(1).map { it.toInt() }.toMutableList() }
        val gridSize = octopusGrid.size * octopusGrid[0].size
        val allCoords = octopusGrid.indices.flatMap { y -> List(octopusGrid[0].size) { i -> Pair(y, i) } }
        for (step in 1..1000) {
            val flashed = mutableSetOf<Pair<Int, Int>>()

            // increment
            allCoords.forEach { pair ->
                val (x, y) = pair
                octopusGrid[x][y]++
            }

            // keep checking for nines
            while (allCoords.any { octopusGrid[it.first][it.second] > 9 && !flashed.contains(it) }) {
                allCoords.filter { octopusGrid[it.first][it.second] > 9 && !flashed.contains(it) }.forEach { pair ->
                    flashed.add(pair)
                    octopusGrid[pair.first][pair.second] = 0
                    surrounding.forEach { modifier ->
                        val x = pair.first + modifier.first
                        val y = pair.second + modifier.second
                        if (x in octopusGrid.indices && y in octopusGrid[0].indices && !flashed.contains(Pair(x, y))) octopusGrid[x][y]++
                    }
                }
            }

            if (flashed.size == gridSize) {
                return step
            }
        }
        return -1
    }

    override fun day(): String = "11"
}

fun main() {
    Day11().run()
}
