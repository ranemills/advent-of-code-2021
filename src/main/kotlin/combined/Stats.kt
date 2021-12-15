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
import support.AdventOfCode
import kotlin.system.measureNanoTime

fun main() {
    listOf(
        Day1::class,
        Day2::class,
        Day3::class,
        Day4::class,
        Day5::class,
        Day6::class,
        Day7::class,
        Day8::class,
        Day9::class,
        Day10::class,
        Day11::class,
        Day12::class,
        Day13::class,
        Day14::class,
        Day15::class,
        Day16::class,
        Day17::class,
        Day18::class,
        Day19::class,
        Day20::class,
        Day21::class,
        Day22::class,
        Day23::class,
        Day24::class
    ).joinToString("\n") {
        val instance: AdventOfCode = it.constructors.first().call()
        "${instance.day()}, ${measureNanoTime { instance.part1() } / 1000000.toDouble()}, ${measureNanoTime { instance.part2() } / 1000000.toDouble()}"
    }.also {
        println("Day, Part 1, Part 2")
        println(it)
    }
}
