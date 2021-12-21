package com.mills.advent.twentyone

import com.mills.advent.support.AdventOfCode
import java.util.regex.Pattern
import kotlin.collections.ArrayList

class Day8 : AdventOfCode {
    private fun getInputText(): String = Day8::class.java.getResource("day8.txt")?.readText()!!

    override fun part1(): Int {
        return getInputText().split("\n").sumOf { line ->
            val selected = line.split("|")[1].trim().split(Pattern.compile("\\s+")).filter { listOf(2, 4, 3, 7).contains(it.length) }

            selected.size
        }
    }

    override fun part2(): Int {
        return getInputText().split("\n").sumOf { line ->
            val outputValues = line.split("|")[1].trim().split(Pattern.compile("\\s+"))
            val allValues = line.split("|").map { it.trim().split(Pattern.compile(" ")) }.flatten()
            val numberMapping: MutableMap<Int, List<Char>> = mutableMapOf()
            allValues.forEach {
                val number = when (it.length) {
                    2 -> 1
                    4 -> 4
                    3 -> 7
                    7 -> 8
                    else -> -1
                }
                numberMapping.put(number, it.toList().sorted())
            }
            numberMapping.remove(-1)

            val segmentMapping: MutableMap<Char, Char> = mutableMapOf()

            // Figure out segment A
            segmentMapping.put('A', (numberMapping.getNotNull(7) subtract numberMapping.getNotNull(1)).first())

            // Figure out segments C and F
            val oneChars = numberMapping.getNotNull(1)
            numberMapping[6] = allValues.filter { it.length == 6 }.filter { it.contains(oneChars.first()) xor it.contains(oneChars.last()) }.first().toList().sorted()
            segmentMapping.put('F', if (numberMapping.getNotNull(6).contains(oneChars.first())) oneChars.first() else oneChars.last())
            segmentMapping.put('C', if (numberMapping.getNotNull(6).contains(oneChars.first())) oneChars.last() else oneChars.first())

            // Figure out segments B and D
            val bdOptions: Set<Char> = numberMapping.getNotNull(4) subtract numberMapping.getNotNull(1)
            numberMapping[0] = allValues.filter { it.length == 6 }.filter { it.contains(bdOptions.first()) xor it.contains(bdOptions.last()) }.first().toList().sorted()
            segmentMapping.put('B', if (numberMapping.getNotNull(0).contains(bdOptions.first())) bdOptions.first() else bdOptions.last())
            segmentMapping.put('D', if (numberMapping.getNotNull(0).contains(bdOptions.first())) bdOptions.last() else bdOptions.first())

            // Figure out segment G
            numberMapping[9] = allValues.filter { it.length == 6 }.filter {
                it.toList().sorted() != numberMapping[0] && it.toList().sorted() != numberMapping[6]
            }.first().toList().sorted()
            segmentMapping['G'] = numberMapping.getNotNull(9).filter {
                !segmentMapping.containsValue(it)
            }.first()

            // Figure out segment E
            segmentMapping['E'] = numberMapping.getNotNull(8).filter {
                !segmentMapping.containsValue(it)
            }.first()

            // Add number values we haven't got already
            numberMapping[2] = listOf(
                segmentMapping['A']!!, segmentMapping['C']!!, segmentMapping['D']!!, segmentMapping['E']!!, segmentMapping['G']!!
            ).sorted()
            numberMapping[3] = listOf(
                segmentMapping['A']!!, segmentMapping['C']!!, segmentMapping['D']!!, segmentMapping['F']!!, segmentMapping['G']!!
            ).sorted()
            numberMapping[5] = listOf(
                segmentMapping['A']!!, segmentMapping['B']!!, segmentMapping['D']!!, segmentMapping['F']!!, segmentMapping['G']!!
            ).sorted()

            // Compute output value
            val inverseNumberMapping = numberMapping.entries.associateBy({ it.value }) { it.key }

//            println(inverseNumberMapping)
//            println(numberMapping)
//            println(segmentMapping)

            outputValues.map {
                inverseNumberMapping.get(it.toList().sorted())
            }.joinToString("").toInt()
        }
    }

    override fun day(): String = "8"

    fun Map<Int, List<Char>>.getNotNull(key: Int): List<Char> {
        return getOrDefault(key, ArrayList())
    }
}

fun main() {
    Day8().run()
}
