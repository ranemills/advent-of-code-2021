package com.mills.advent.twentyone

import com.mills.advent.support.AdventOfCode
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
        val instance: AdventOfCode = it.constructors.first().call() as AdventOfCode
        "${instance.day()}, ${measureNanoTime { instance.part1() } / 1000000.toDouble()}, ${measureNanoTime { instance.part2() } / 1000000.toDouble()}"
    }.also {
        println("Day, Part 1, Part 2")
        println(it)
    }
}
