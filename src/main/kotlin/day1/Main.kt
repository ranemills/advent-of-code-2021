package day1

class Main {
    fun part1(): Int {
        val inputTxt: String = Main::class.java.getResource("input.txt")?.readText()!!
        val inputValues: List<String> = inputTxt.split("\n")
        return inputValues.fold(Int.MAX_VALUE to 0) { p: Pair<Int, Int>, v: String ->
            v.toInt() to if (p.first < v.toInt()) {
                (p.second + 1)
            } else {
                p.second
            }
        }.second
    }

    fun part2(): Int {
        val inputTxt: String = Main::class.java.getResource("input.txt")?.readText()!!
        val inputValues: List<Int> = inputTxt.split("\n").map { it.toInt() }
        return inputValues.subList(3, inputValues.size).fold(
            inputValues.subList(0, 3) to 0
        ) { w: Pair<List<Int>, Int>, v: Int ->

            val previousWindowSum: Int = w.first.sum()
            val newWindow: List<Int> = listOf(
                w.first[1],
                w.first[2],
                v
            )

            newWindow to if (newWindow.sum()> previousWindowSum) (w.second + 1) else w.second
        }.second
    }
}

fun main() {
    println("Day 1")
    println("Part 1")
    println(Main().part1())
    println("Part 2")
    println(Main().part2())
}
