package com.mills.advent.twentyone

import com.mills.advent.support.AdventOfCode
import com.mills.advent.support.Coord
import com.mills.advent.support.allPairs
import kotlin.math.max

// Example
//val targetX = 20..30
//val targetY = -10..-5

// Actual
val targetX = 32..65
val targetY = -225..-177

val initialCoord = Coord(0, 0)

class Day17 : AdventOfCode {

    override fun day(): String = "17"

    override fun part1(): Int {
        return allPairs(0, 200, -500, 500).map {
            runSimulation(initialCoord, Coord(it), 0)
        }.filter {
            it.first
        }.maxByOrNull {
            it.second
        }!!.second
    }

    tailrec fun runSimulation(position: Coord, velocity: Coord, maxY: Int): Pair<Boolean, Int> {
        if (position.y < targetY.first || position.x > targetX.last) return Pair(false, 0)
        if (position.x in targetX && position.y in targetY) return Pair(true, maxY)
        return runSimulation(
            position + velocity,
            Coord(
                if (velocity.x > 0) velocity.x - 1 else velocity.x,
                velocity.y - 1
            ),
            max(maxY, position.y)
        )
    }

    override fun part2(): Int {
        return allPairs(0, 200, -500, 500).map {
            runSimulation(initialCoord, Coord(it), 0)
        }.filter {
            it.first
        }.size
    }
}

fun main() {
    Day17().run()
}
