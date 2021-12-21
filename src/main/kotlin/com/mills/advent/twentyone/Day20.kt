package com.mills.advent.twentyone

import com.mills.advent.support.AdventOfCode
import com.mills.advent.support.Coord
import kotlin.math.pow

class Day20 : AdventOfCode {
    private fun getInputText(): String = Day20::class.java.getResource("day20.txt")?.readText()!!

    override fun day(): String = "20"

    override fun part1(): Int {
        return countPixelsAfterStep(2)
    }

    override fun part2(): Int {
        return countPixelsAfterStep(50)
    }

    private fun countPixelsAfterStep(steps: Int): Int {
        val (enhancement, imageLines) = getInputText().split("\n\n")
        val image = imageLines.split("\n").map { it.chunked(1).map { it[0] }.toMutableList() }
        val darkPixels =
            image.withIndex().flatMap { (y, row) -> row.withIndex().map { (x, cell) -> Coord(x, y) to (cell == '#') } }
                .filter { it.second }.map { it.first }.toSet()

        var outsideIsAllLight = false

        return (0..steps).fold(darkPixels) { acc, i ->
            val boundExtension = 1

            val minX = acc.minOf { it.x }
            val leftMost = minX - boundExtension
            val maxX = acc.maxOf { it.x }
            val rightMost = maxX + boundExtension
            val minY = acc.minOf { it.y }
            val topMost = minY - boundExtension
            val maxY = acc.maxOf { it.y }
            val bottomMost = maxY + boundExtension

            val ret = if (i == 0) {
                acc
            } else {
                if (enhancement[0] == '#') outsideIsAllLight = !outsideIsAllLight

                (leftMost..rightMost).flatMap { x ->
                    (topMost..bottomMost).map { y ->
                        Coord(x, y) to (enhancement[binaryToInt(
                            getNeighbours(
                                Coord(x, y)
                            ).map {
                                if (it in acc || ((it.x !in minX..maxX || it.y !in minY..maxY) && !outsideIsAllLight)) 1 else 0
                            })])
                    }
                }.filter { it.second == '#' }.map { it.first }.toSet()
            }

            ret
        }.size
    }

    private val neighbourCache = mutableMapOf<Coord, List<Coord>>()
    private fun getNeighbours(coord: Coord): List<Coord> = neighbourCache.computeIfAbsent(coord) { (-1..1).flatMap { y -> (-1..1).map { x -> Coord(x+coord.x, y+coord.y) } } }

    private fun binaryToInt(binary: List<Int>): Int {
        val lastIndex = binary.lastIndex
        return binary.withIndex().sumOf { (idx, value) ->
            value * (2.toDouble().pow(
                lastIndex-idx
            ))
        }.toInt()
    }
}

fun main() {
    Day20().run()
}
