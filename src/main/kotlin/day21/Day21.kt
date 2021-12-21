package day21

import support.AdventOfCode
import java.lang.Math.max

class Day21 : AdventOfCode {
    private fun getInputText(): String = Day21::class.java.getResource("input.txt")?.readText()!!

    override fun day(): String = "21"

    override fun part1(): Long {
        val playerStarts = getInputText().split("\n").map { it.last().toString().toInt()}.withIndex().associate { (idx, it) -> Pair(idx,it) }

        val playerPositions = playerStarts.toMutableMap()
        val playerScores = mutableMapOf( 0 to 0L, 1 to 0L)

        val dieRollValues = 1..100
        var dieRollIterator = dieRollValues.iterator()

        fun rollDice() = if(dieRollIterator.hasNext())
            dieRollIterator.next()
        else {
            dieRollIterator = dieRollValues.iterator()
            dieRollIterator.next()
        }


        var countDieRolls = 0

        while(true) {

            for(playerNo in (0..1)) {
                countDieRolls += 3
                val dieRolls = (1..3).map { rollDice() }
                val newPosition = (playerPositions[playerNo]!!+dieRolls.sum()-1)%10+1
                playerScores[playerNo] = playerScores[playerNo]!! + newPosition
                playerPositions[playerNo] = newPosition

//                println("$playerNo, Position ${playerPositions[playerNo]}, Score ${playerScores[playerNo]}")

                if(playerScores[playerNo]!! >= 1000) {
                    return playerScores[(playerNo+1) % 2]!!*countDieRolls
                }
            }
        }
    }

    val quantumCache = mutableMapOf<Triple<Pair<Int, Long>, Pair<Int, Long>, Long>, Pair<Long,Long>>()

    fun rollQuantumDice(currentPlayer: Pair<Int, Long>, nextPlayer: Pair<Int, Long>, turn: Long): Pair<Long, Long> {
        val allRolls = (1..3).flatMap { i -> (1..3).flatMap { j -> (1..3).map { k -> i+j+k} } }
        return allRolls.fold(0L to 0L) { acc, dieRoll ->
            val newPosition = (currentPlayer.first+dieRoll-1)%10+1
            val newScore = (currentPlayer.second) + newPosition
            val newPair = newPosition to newScore

            val ret = if(newScore >= 21) {
                acc.first + 1 to acc.second
            } else {
                var res = quantumCache[Triple(nextPlayer, newPair, turn+1)]
                if(res == null) {
                    res =  rollQuantumDice(
                            nextPlayer,
                            newPair,
                            turn + 1
                        )
                    quantumCache[Triple(nextPlayer, newPair, turn+1)] = res
                }
                acc.first + res.second to acc.second + res.first
            }

            ret
        }
    }

    override fun part2(): Long {
        val playerStarts = getInputText().split("\n").map { it.last().toString().toInt()}.withIndex().associate { (idx, it) -> Pair(idx,it) }

        val output = rollQuantumDice(
            Pair(playerStarts[0]!!, 0L),
            Pair(playerStarts[1]!!, 0L),
            1
        )
        return max(output.first, output.second)
    }
}

fun main() {
    Day21().run()
}
