package day10

import support.AdventOfCode
import kotlin.collections.ArrayDeque

const val DAY = "10"

class Day10 : AdventOfCode {
    private fun getInputText(): String = Day10::class.java.getResource("input.txt")?.readText()!!

    private val opening = setOf('(', '[', '{', '<')
    private val syntaxScores = mapOf(
        ')' to 3,
        ']' to 57,
        '}' to 1197,
        '>' to 25137
    )
    private val completionScores = mapOf(
        ')' to 1,
        ']' to 2,
        '}' to 3,
        '>' to 4
    )
    private val matchingFromClosing = mapOf(
        ')' to '(',
        ']' to '[',
        '}' to '{',
        '>' to '<'
    )
    private val matchingFromOpening = matchingFromClosing.entries.associateBy({ it.value }) { it.key }
    private val closing = setOf(')', ']', '}', '>')

    override fun part1(): Int {
        return getInputText().split("\n").sumOf { getSyntaxScore(it) }
    }

    override fun day(): String = "10"

    fun getSyntaxScore(line: String): Int {
        val stack: ArrayDeque<Char> = ArrayDeque()

        for (c in line) {
            if (c in opening) stack.add(c)
            if (c in closing) {
                if (stack.removeLast() != matchingFromClosing[c]) return syntaxScores[c]!!
            }
        }

        return 0
    }

    override fun part2(): Long {
        val scores = getInputText().split("\n")
            .filter { getSyntaxScore(it) == 0 }
            .map { line ->
                val stack: ArrayDeque<Char> = ArrayDeque()

                for (c in line) {
                    if (c in opening) stack.add(c)
                    if (c in closing) stack.removeLast()
                }

                var score = 0L

                while (!stack.isEmpty()) {
                    val c = stack.removeLast()
                    score *= 5
                    score += completionScores[matchingFromOpening[c]]!!
                }
                score
            }.sorted()

        println(scores)

        return scores[scores.size/2]
    }
}

fun main() {
    Day10().run()
}
