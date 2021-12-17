package day17

import support.AdventOfCode
import support.Coord
import support.allPairs

// Example
//val targetX = 20..30
//val targetY = -10..-5

// Actual
val targetX = 32..65
val targetY = -225..-177

class Day17 : AdventOfCode {

    override fun day(): String = "17"

    override fun part1(): Int {
        return allPairs(0, 200, -500, 500).map {
            runSimulation(Coord(it))
        }.filter {
            it.first
        }.maxByOrNull {
            it.second
        }!!.second
    }

    fun runSimulation(initialVelocity: Coord): Pair<Boolean, Int> {
        var velocity = initialVelocity.copy()
        var position = Coord(0, 0)
        var maxY = 0

        while (
            !(position.x in targetX && position.y in targetY)
        ) {
            // have we gone past it?
            if (position.y < targetY.first || position.x > targetX.last) {
//                println("Velocity $initialVelocity gone past it")
                return Pair(false, maxY)
            }

            if (position.y > maxY) maxY = position.y

            position = Coord(position.x + velocity.x, position.y + velocity.y)
            velocity = Coord(
                if (velocity.x > 0) velocity.x - 1 else velocity.x,
                velocity.y - 1
            )

//            println(position)

        }
        return Pair(true, maxY)
    }

    override fun part2(): Int {
        return allPairs(0, 200, -500, 500).map {
            runSimulation(Coord(it))
        }.filter {
            it.first
        }.size
    }
}

fun main() {
    Day17().run()
}
