package day12

const val DAY = "12"

class Main {
    private fun getInputText(): String = Main::class.java.getResource("input.txt")?.readText()!!

    fun part1(): Int {
        val smallCaves: MutableSet<String> = mutableSetOf()
        val bigCaves: MutableSet<String> = mutableSetOf()
        val links: MutableMap<String, MutableSet<String>> = mutableMapOf()

        getInputText().split("\n").forEach {
            val (start, end) = it.split("-")
            if (start.uppercase() == start) bigCaves.add(start) else smallCaves.add(start)
            if (end.uppercase() == end) bigCaves.add(end) else smallCaves.add(end)

            links.computeIfAbsent(start) { mutableSetOf() }.add(end)
            links.computeIfAbsent(end) { mutableSetOf() }.add(start)
        }

        fun exploreCaves(currentCave: String, visitedCaves: Set<String>): Int {
            if (currentCave == "end") {
//                println(visitedCaves)
                return 1
            }
            if (currentCave.isLowercase() && visitedCaves.contains(currentCave)) return 0

            return links.getOrDefault(currentCave, setOf()).sumOf {
                exploreCaves(it, visitedCaves + currentCave)
            }
        }

        return exploreCaves("start", setOf())
    }

    fun String.isUppercase() = this.uppercase() == this
    fun String.isLowercase() = this.lowercase() == this

    fun part2():Int {
        val smallCaves: MutableSet<String> = mutableSetOf()
        val bigCaves: MutableSet<String> = mutableSetOf()
        val links: MutableMap<String, MutableSet<String>> = mutableMapOf()

        getInputText().split("\n").forEach {
            val (start, end) = it.split("-")
            if (start.uppercase() == start) bigCaves.add(start) else smallCaves.add(start)
            if (end.uppercase() == end) bigCaves.add(end) else smallCaves.add(end)

            if(end != "start") links.computeIfAbsent(start) { mutableSetOf() }.add(end)
            if(start != "start") links.computeIfAbsent(end) { mutableSetOf() }.add(start)
        }

        fun exploreCaves(currentCave: String, visitedCaves: List<String>, doubleVisitedCave: String?): Int {
//            println(visitedCaves + currentCave)
            var myDoubleVisitedCave: String? = null
            if (currentCave == "end") {
                println(visitedCaves + currentCave)
                return 1
            }
            if (currentCave.isLowercase() && visitedCaves.contains(currentCave)) {
                if(doubleVisitedCave != null) return 0 else myDoubleVisitedCave = currentCave
            }

            return links.getOrDefault(currentCave, setOf()).sumOf {
                exploreCaves(it, visitedCaves + currentCave, myDoubleVisitedCave ?: doubleVisitedCave)
            }
        }

        return exploreCaves("start", listOf(), null)
    }
}

fun main() {
    println("Day $DAY")
    println("Part 1")
    println(Main().part1())
    println("Part 2")
    println(Main().part2())
}
