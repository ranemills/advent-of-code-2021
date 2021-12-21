package com.mills.advent.twentyone

import com.mills.advent.support.AdventOfCode
import java.util.Collections.max
import java.util.Collections.min

class Day7 : AdventOfCode {
    private fun getInputText(): String = Day7::class.java.getResource("day7.txt")?.readText()!!

    override fun part1(): Int {
        val crabs = getInputText().split(",").map {it.toInt()}
        return (min(crabs)..max(crabs)).minOf { position ->
            crabs.sumOf { if(position>it) position - it else it - position}
        }
    }

    override fun day(): String = "7"

    override fun part2(): Long {
        val crabs = getInputText().split(",").map {it.toLong()}
        return (min(crabs)..max(crabs)).minOf { position ->
            crabs.sumOf {
                val n = if(position>it) position - it else it - position
                n*(n+1)/2
            }
        }
    }
}

fun main() {
    Day7().run()
}
