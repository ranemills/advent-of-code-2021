package combined

import day1.Day1
import day10.Day10
import day11.Day11
import day12.Day12
import day13.Day13
import day14.Day14
import day15.Day15
import day16.Day16
import day17.Day17
import day18.Day18
import day19.Day19
import day2.Day2
import day20.Day20
import day21.Day21
import day22.Day22
import day23.Day23
import day24.Day24
import day3.Day3
import day4.Day4
import day5.Day5
import day6.Day6
import day7.Day7
import day8.Day8
import day9.Day9
import kotlin.system.measureNanoTime

fun main() {
    listOf(
        Day1(),
        Day2(),
        Day3(),
        Day4(),
        Day5(),
        Day6(),
        Day7(),
        Day8(),
        Day9(),
        Day10(),
        Day11(),
        Day12(),
        Day13(),
        Day14(),
        Day15(),
        Day16(),
        Day17(),
        Day18(),
        Day19(),
        Day20(),
        Day21(),
        Day22(),
        Day23(),
        Day24()
    ).joinToString("\n") {
        "${it.day()}, ${measureNanoTime { it.part1() } / 1000000.toDouble()}, ${measureNanoTime { it.part2() } / 1000000.toDouble()}"
    }.also {
        println("Day, Part 1, Part 2")
        println(it)
    }
}
