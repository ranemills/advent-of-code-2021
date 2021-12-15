package day2

import support.AdventOfCode

const val DAY = "2"

class Day2 : AdventOfCode {
    private fun getInputText(): String = Day2::class.java.getResource("input.txt")?.readText()!!

    override fun day(): String = "2"

    override fun part1(): Int {
        val commands = getInputText().split("\n")
        val coords = commands.fold(Coordinates(0, 0)) { coords, command ->
            val (direction, number) = command.split(" ")
            when (direction) {
                "up" -> coords.copy(depth = coords.depth - number.toInt())
                "down" -> coords.copy(depth = coords.depth + number.toInt())
                "forward" -> coords.copy(length = coords.length + number.toInt())
                else -> coords.copy()
            }
        }
        return coords.depth * coords.length
    }

    override fun part2(): Int {
        val commands = getInputText().split("\n")
        val coords = commands.fold(CoordinatesWithAim(0, 0, 0)) { coords, command ->
            val (direction, number) = command.split(" ")
            when (direction) {
                "up" -> coords.copy(aim = coords.aim - number.toInt())
                "down" -> coords.copy(aim = coords.aim + number.toInt())
                "forward" -> coords.copy(
                    length = coords.length + number.toInt(),
                    depth = coords.depth + (coords.aim * number.toInt())
                )
                else -> coords.copy()
            }
        }
        return coords.depth * coords.length
    }
}

data class Coordinates(
    val depth: Int,
    val length: Int
)

data class CoordinatesWithAim(
    val depth: Int,
    val length: Int,
    val aim: Int
)

fun main() {
    Day2().run()
}
