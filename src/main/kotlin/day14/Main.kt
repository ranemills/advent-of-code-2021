package day14

const val DAY = "14"

class Main {
    private fun getInputText(): String = Main::class.java.getResource("input.txt")?.readText()!!

    val polyMappings = getInputText().split("\n\n")[1].split("\n").associate {
        val (pair, result) = it.split(" -> ")
        Pair(Pair(pair[0], pair[1]), result[0])
    }

    val pairCache: MutableMap<Pair<Pair<Char, Char>, Int>, Map<Char, Long>> = mutableMapOf()

    fun part1(): Int {
        val (startingPolymer, mappings) = getInputText().split("\n\n")
        val polyMappings = mappings.split("\n").associate {
            val (pair, result) = it.split(" -> ")
            Pair(Pair(pair[0], pair[1]), result)
        }

        val newPolymer = (1..10).fold(startingPolymer) { polymer, step ->
            polymer.indices.map { index ->
                if (index == 0) {
                    polymer[0]
                } else {
                    val window = Pair(polymer[index - 1], polymer[index])
                    if (polyMappings.containsKey(window)) {
                        listOf(polyMappings.get(window), polymer[index]).joinToString("")
                    } else {
                        polymer[index]
                    }
                }
            }.joinToString("")
        }

        val chars = newPolymer.chunked(1).groupBy { it }
            .mapValues { entry -> entry.value.size }.entries.sortedByDescending { it.value }
        println(chars)

        return chars.first().value - chars.last().value
    }

    fun part2(): Long {
        val startingPolymer = getInputText().split("\n\n")[0]

        // Hack to count the last element, since we only count the first element of each pair at the leaf node to avoid duplication
        var outMap = mapOf<Char, Long>(
            startingPolymer.last() to 1
        )

        (1..startingPolymer.length - 1).map {
            outMap = mergeMaps(outMap, countChars(Pair(startingPolymer[it - 1], startingPolymer[it]), 1))
        }

        val chars = outMap.entries.sortedByDescending { it.value }
        println(chars)
        return chars.first().value - chars.last().value
    }

    fun countChars(polymer: Pair<Char, Char>, step: Int): Map<Char, Long> {
        if (step > 40 || polyMappings[polymer] == null) {
            return mapOf(
                polymer.first to 1L
            )
        }

        val newChar = polyMappings[polymer]!!

        val cacheKey = Pair(polymer, step)
        if (pairCache.containsKey(cacheKey)) {
            return pairCache[cacheKey]!!
        }

        val retMap = mergeMaps(
            countChars(Pair(polymer.first, newChar), step + 1),
            countChars(Pair(newChar, polymer.second), step + 1)
        )

        pairCache.put(cacheKey, retMap)

        return retMap
    }

    fun mergeMaps(map1: Map<Char, Long>, map2: Map<Char, Long>): Map<Char, Long> {
        val outMap = map1.toMutableMap()
        map2.forEach { (k, v) ->
            outMap.computeIfAbsent(k) { 0 }
            outMap[k] = outMap[k]!! + v
        }

        return outMap
    }
}

fun main() {
    println("Day $DAY")
    println("Part 1")
    println(Main().part1())
    println("Part 2")
    println(Main().part2())
}
