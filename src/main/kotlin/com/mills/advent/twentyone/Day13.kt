package com.mills.advent.twentyone

import com.mills.advent.support.AdventOfCode

class Day13 : AdventOfCode {
    private fun getInputText(): String = Day13::class.java.getResource("day13.txt")?.readText()!!

    override fun part1(): Int {
        val coords = getInputText()
            .split("\n\n")[0]
            .split("\n")
            .map {
                Pair(
                    it.split(",")[0].toInt(),
                    it.split(",")[1].toInt()
                )
            }.toSet()

        val instructions = getInputText()
            .split("\n\n")[1]
            .split("\n")
            .map {
                Pair(
                    it.subSequence(11, 12),
                    (it.subSequence(13, it.length) as String).toInt()
                )
            }

        return instructions.subList(0, 1).fold(coords) { acc, instr ->
            val (dir, line) = instr
            acc.map {
                if (dir == "y" && it.second > line) {
                    Pair(it.first, 2 * line - it.second)
                } else if (dir == "x" && it.first > line) {
                    Pair(2 * line - it.first, it.second)
                } else {
                    it
                }
            }.toSet()
        }.size
    }

    override fun day(): String = "13"

    override fun part2() {
        val coords = getInputText()
            .split("\n\n")[0]
            .split("\n")
            .map {
                Pair(
                    it.split(",")[0].toInt(),
                    it.split(",")[1].toInt()
                )
            }.toSet()

        val instructions = getInputText()
            .split("\n\n")[1]
            .split("\n")
            .map {
                Pair(
                    it.subSequence(11, 12),
                    (it.subSequence(13, it.length) as String).toInt()
                )
            }

        val outGrid = mutableListOf<MutableList<Char>>()

        instructions.fold(coords) { acc, instr ->
            val (dir, line) = instr
            acc.map {
                if (dir == "y" && it.second > line) {
                    Pair(it.first, 2 * line - it.second)
                } else if (dir == "x" && it.first > line) {
                    Pair(2 * line - it.first, it.second)
                } else {
                    it
                }
            }.toSet()
        }.forEach { coord ->
            val (x, y) = coord
            outGrid.addAll(
                (outGrid.size..y).map { MutableList(x) { ' ' } }
            )
            outGrid[y].addAll(
                (outGrid[y].size..x).map { ' ' }
            )
            outGrid[y][x] = '#'
        }

        outGrid.forEach { rows ->
            println(rows)
        }
    }
}

fun main() {
    Day13().run()
}
