package day13

const val DAY = "13"

class Main {
    private fun getInputText(): String = Main::class.java.getResource("input.txt")?.readText()!!

    fun part1(): Int {
        val coords = getInputText().split("\n\n")[0].split("\n").map { Pair(it.split(",")[0].toInt(), it.split(",")[1].toInt()) }.toSet()
        val instructions = getInputText().split("\n\n")[1].split("\n")

        // Just the first instructions
        val dirs = instructions.map{it.subSequence(11, 12)}
        val lines = instructions.map { it.subSequence(13, it.length).toString().toInt() }

//        println(coords)

       return (0..0).fold(coords) { acc, instrNo ->
            val dir = dirs[instrNo]
            val line = lines[instrNo]

//           println("fold along $dir=$line")

            val ret = acc.map {
                if (dir == "y" && it.second > line) {
                    Pair(it.first, 2*line - it.second)
                } else if(dir == "x" && it.first > line) {
                    Pair(2*line - it.first, it.second)
                } else {
                    it
                }
            }.toSet()

//           println(ret)

           ret
        }.size
    }

    fun part2() {
        val coords = getInputText().split("\n\n")[0].split("\n").map { Pair(it.split(",")[0].toInt(), it.split(",")[1].toInt()) }.toSet()
        val instructions = getInputText().split("\n\n")[1].split("\n")

        // Just the first instructions
        val dirs = instructions.map{it.subSequence(11, 12)}
        val lines = instructions.map { it.subSequence(13, it.length).toString().toInt() }

//        println(coords)

        val newCoords = (0..11).fold(coords) { acc, instrNo ->
            val dir = dirs[instrNo]
            val line = lines[instrNo]

//            println("fold along $dir=$line")

            val ret = acc.map {
                if (dir == "y" && it.second > line) {
                    Pair(it.first, 2*line - it.second)
                } else if(dir == "x" && it.first > line) {
                    Pair(2*line - it.first, it.second)
                } else {
                    it
                }
            }.toSet()

//            println(ret)

            ret
        }

        val outGrid = mutableListOf<MutableList<Char>>()

        for(coord in newCoords) {
            while(outGrid.size <= coord.second) {
                outGrid.add(mutableListOf())
            }
            while (outGrid[coord.second].size <= coord.first) {
                outGrid[coord.second].add(' ')
            }
            outGrid[coord.second][coord.first] = '#'
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
