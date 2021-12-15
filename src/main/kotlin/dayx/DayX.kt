package dayx

import support.AdventOfCode

const val DAY = "X"

class DayX : AdventOfCode {
    private fun getInputText(): String = DayX::class.java.getResource("input.txt")?.readText()!!

    override fun day(): String = "X"

    override fun part1() {
    }

    override fun part2() {
    }
}

fun main() {
    DayX().run()
}
