package day5

import support.AdventOfCode

class Day5 : AdventOfCode {
    private fun getInputText(): String = Day5::class.java.getResource("input.txt")?.readText()!!

    override fun day(): String = "5"

    override fun part1(): Int {
        val grid: MutableList<MutableList<Int>> = mutableListOf()
        for (line in getInputText().split("\n")) {
            val (x1, y1, x2, y2) = line.split(" -> ").map { it.split(",") }.flatten().map { it.toInt() }
            if (x1 != x2 && y1 != y2) continue

            for (y in if (y2> y1) (y1..y2) else (y2..y1)) {
                while (grid.size <= y) { grid.add(mutableListOf()) }
                for (x in if (x2> x1) (x1..x2) else (x2..x1)) {
                    while (grid[y].size <= x) { grid[y].add(0) }
                    grid[y][x] = grid[y][x] + 1
                }
            }
        }
        var total = 0
        for (y in grid.indices) {
            for (x in grid[y].indices) {
//                print(grid[y][x])
                total += if (grid[y][x] > 1) 1 else 0
            }
//            println()
        }

        return total
    }

    override fun part2(): Int {
        val grid: MutableList<MutableList<Int>> = mutableListOf()
        for (line in getInputText().split("\n")) {
            val (x1, y1, x2, y2) = line.split(" -> ").map { it.split(",") }.flatten().map { it.toInt() }

            if (x1 != x2 && y1 != y2) {
                val xValues = (if (x1 > x2) (x1 downTo x2) else (x1..x2)).toList()
                val yValues = (if (y1 > y2) (y1 downTo y2) else (y1..y2)).toList()

                for (i in xValues.indices) {
                    val currentX = xValues[i]
                    val currentY = yValues[i]

                    while (grid.size <= currentY) { grid.add(mutableListOf()) }
                    while (grid[currentY].size <= currentX) { grid[currentY].add(0) }
                    grid[currentY][currentX] = grid[currentY][currentX] + 1
                }
            } else {
                for (y in if (y2 > y1) (y1..y2) else (y2..y1)) {
                    while (grid.size <= y) { grid.add(mutableListOf()) }
                    for (x in if (x2 > x1) (x1..x2) else (x2..x1)) {
                        while (grid[y].size <= x) { grid[y].add(0) }
                        grid[y][x] = grid[y][x] + 1
                    }
                }
            }
        }
        var total = 0
        val size = grid.size - 1
        return (0..size).flatMap { y -> List(size) { i -> Pair(y, i) } }.sumOf {
            val value = grid.getOrNull(it.first)?.getOrNull(it.second) ?: 0
            if (value > 1) 1 else 0 as Int
        }
    }
}

fun main() {
    Day5().run()
}
