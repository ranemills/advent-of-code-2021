package com.mills.advent

import com.mills.advent.support.AdventOfCode

const val DAY = "X"

class DayX : AdventOfCode {
    private fun getInputText(): String = DayX::class.java.getResource("dayX.txt")?.readText()!!

    override fun day(): String = "X"

    override fun part1() {
    }

    override fun part2() {
    }
}

fun main() {
    DayX().run()
}
