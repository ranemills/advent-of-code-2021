package day6

const val DAY = "6"

class Main {
    private fun getInputText(): String = Main::class.java.getResource("input.txt")?.readText()!!

    fun part1(): Int {
        var lanternFish = getInputText().split(",").map { it.toInt() }
        for (day in 1..256) {
            lanternFish = lanternFish.flatMap { if (it == 0) listOf(6, 8) else listOf(it - 1) }
//            println("Day $day: $lanternFish")
        }
        return lanternFish.size
    }

    fun part2(): Long {
        return (1..256).fold(
            getInputText()
                .split(",")
                .map { it.toInt() }
                .groupBy { it }
                .mapValues { it.value.size.toLong() }
        ) { acc, _ ->
            mapOf(
                0 to acc.getOrDefault(1, 0),
                1 to acc.getOrDefault(2, 0),
                2 to acc.getOrDefault(3, 0),
                3 to acc.getOrDefault(4, 0),
                4 to acc.getOrDefault(5, 0),
                5 to acc.getOrDefault(6, 0),
                6 to acc.getOrDefault(7, 0) + acc.getOrDefault(0, 0),
                7 to acc.getOrDefault(8, 0),
                8 to acc.getOrDefault(0, 0)
            )
        }.values.sum()
    }
}

fun main() {
    println("Day $DAY")
    println("Part 1")
//    println(Main().part1())
    println("Part 2")
    println(Main().part2())
}
