package day13

const val DAY = "13"

class Main {
    private fun getInputText(): String = Main::class.java.getResource("input.txt")?.readText()!!

    fun part1(): Int {
        val coords = getInputText()
            .split("\n\n")[0]
            .split("\n")
            .map {
                Pair(
                    it.split(",")[0].toInt(),
                    it.split(",")[1].toInt()
                )
            }.toSet()

        val instructions = getInputText()
            .split("\n\n")[1]
            .split("\n")
            .map {
                Pair(
                    it.subSequence(11, 12),
                    (it.subSequence(13, it.length) as String).toInt()
                )
            }

        return instructions.subList(0, 1).fold(coords) { acc, instr ->
            val (dir, line) = instr
            acc.map {
                if (dir == "y" && it.second > line) {
                    Pair(it.first, 2 * line - it.second)
                } else if (dir == "x" && it.first > line) {
                    Pair(2 * line - it.first, it.second)
                } else {
                    it
                }
            }.toSet()
        }.size
    }

    fun part2() {
        val coords = getInputText()
            .split("\n\n")[0]
            .split("\n")
            .map {
                Pair(
                    it.split(",")[0].toInt(),
                    it.split(",")[1].toInt()
                )
            }.toSet()

        val instructions = getInputText()
            .split("\n\n")[1]
            .split("\n")
            .map {
                Pair(
                    it.subSequence(11, 12),
                    (it.subSequence(13, it.length) as String).toInt()
                )
            }

        val outGrid = mutableListOf<MutableList<Char>>()

        instructions.fold(coords) { acc, instr ->
            val (dir, line) = instr
            acc.map {
                if (dir == "y" && it.second > line) {
                    Pair(it.first, 2 * line - it.second)
                } else if (dir == "x" && it.first > line) {
                    Pair(2 * line - it.first, it.second)
                } else {
                    it
                }
            }.toSet()
        }.forEach { coord ->
            val (x, y) = coord
            outGrid.addAll(
                (outGrid.size..y).map { MutableList(x) { ' ' } }
            )
            outGrid[y].addAll(
                (outGrid[y].size..x).map { ' ' }
            )
            outGrid[y][x] = '#'
        }

        outGrid.forEach { rows ->
            println(rows)
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
