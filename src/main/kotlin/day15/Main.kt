package day15

const val DAY = "15"

typealias Coord = Pair<Int, Int>

fun Coord.nextX() = Coord(first + 1, second)
fun Coord.nextY() = Coord(first, second + 1)
fun Coord.previousX() = Coord(first - 1, second)
fun Coord.previousY() = Coord(first, second - 1)
fun Coord.x() = first
fun Coord.y() = second

const val MAX_VALUE = 1000000

class Main {
    private fun getInputText(): String = Main::class.java.getResource("input.txt")?.readText()!!

    fun part1(): Int {
        val cave: List<List<Int>> = getInputText().split("\n").map { it.chunked(1).map { it.toInt() }.toMutableList() }
        return findLowestRiskPath(cave)
    }

    fun findLowestRiskPath(cave: List<List<Int>>): Int {
        val unvisitedNodes = (cave.indices).flatMap { y -> List(cave[0].size) { x -> Coord(x, y) } }.toMutableSet()
        val distances: MutableMap<Coord, Int> = unvisitedNodes.associateWith { MAX_VALUE }.toMutableMap()

        distances[Coord(0, 0)] = 0

        var currentNode = Coord(0, 0)

        val finalCoord = Coord(cave[0].size - 1, cave.size - 1)

        while (currentNode != finalCoord) {
            val currentDistance = distances[currentNode]!!
            listOf(currentNode.previousX(), currentNode.nextX(), currentNode.previousY(), currentNode.nextY()).forEach {
                if(it in unvisitedNodes) {
                    val neighbourWeight = cave[it.y()][it.x()]
                    val neighbourDistance = distances[it]!!
                    if (currentDistance + neighbourWeight < neighbourDistance) {
                        distances[it] = currentDistance + neighbourWeight
                    }
                }
            }
            unvisitedNodes.remove(currentNode)

            currentNode = unvisitedNodes.minByOrNull { distances[it]!! }!!

//            println(unvisitedNodes.size)
        }

        return distances[finalCoord]!!
    }

    fun part2(): Int {
        val originalCave: List<List<Int>> = getInputText().split("\n").map { it.chunked(1).map { it.toInt() }.toMutableList() }

        val cave: MutableList<MutableList<Int>> = mutableListOf()

        for (repeat in (0..4)) {
            cave.addAll(originalCave.map { it.map { if (it + repeat > 9) it + repeat - 9 else it + repeat }.toMutableList() })
        }

        for (i in cave.indices) {
            val savedRow = cave[i].toList()
            for (repeat in (1..4)) {
                cave[i].addAll(savedRow.map { if (it + repeat > 9) it + repeat - 9 else it + repeat })
            }
        }

//        cave.forEach { println(it.joinToString("")) }

        return findLowestRiskPath(cave)
    }
}

fun main() {
    println("Day $DAY")
    println("Part 1")
    println(Main().part1())
    println("Part 2")
    println(Main().part2())
}
