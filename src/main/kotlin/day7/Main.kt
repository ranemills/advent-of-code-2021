package day7

import java.util.Collections.max
import java.util.Collections.min

const val DAY = "7"

class Main {
    private fun getInputText(): String = Main::class.java.getResource("input.txt")?.readText()!!

    fun part1(): Int {
        val crabs = getInputText().split(",").map {it.toInt()}
        return (min(crabs)..max(crabs)).minOf { position ->
            crabs.sumOf { if(position>it) position - it else it - position}
        }
    }

    fun part2(): Long {
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
    println("Day $DAY")
    println("Part 1")
    println(Main().part1())
    println("Part 2")
    println(Main().part2())
}
