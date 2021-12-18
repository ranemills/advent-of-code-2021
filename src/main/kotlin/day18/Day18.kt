package day18

import support.AdventOfCode
import java.util.regex.Pattern
import kotlin.math.ceil
import kotlin.math.floor

interface TreeNode

class LeafNode (
    val value: Int
): TreeNode

class Node(
    val left: TreeNode,
    val right: TreeNode
) : TreeNode

class Day18 : AdventOfCode {
    private fun getInputText(): String = Day18::class.java.getResource("input.txt")?.readText()!!

    override fun day(): String = "18"

    override fun part1(): Int {
        val input = getInputText().split("\n")

        var result = reduceNumber(input[0])

        for(i in input.subList(1, input.size)) {
            result = reduceNumber(addNumbers(result, reduceNumber(i)))
        }

        return computeMagnitude(result)
    }

    private fun computeMagnitude(input: String): Int {
        var processed = input
        while(processed.contains("[")) {
            var chosenPair = ""
            // find a pair
            for(c in processed) {
                chosenPair += c
                if(c == '[') chosenPair = "["
                if(c == ']') {

                    break
                }
            }
            val (left, right) = chosenPair.subSequence(1,chosenPair.length-1).split(",").map { it.toInt() }
            processed = processed.replace(chosenPair, (left*3+right*2).toString())
        }
        return processed.toInt()
    }

    private fun reduceNumber(input: String): String {
        var processed = input

        while (true) {
            val reduced = explodeNumbers2(processed)
            if(reduced != processed) {
                // println("Explode")
                // println(reduced)
                processed = reduced
                continue
            }

            val split = splitNumber(reduced)

            if (split == processed) break

            // println("Split")
            // println(split)

            processed = split
        }

        return processed
    }

    override fun part2(): Int {
        val input = getInputText().split("\n")
        return input.flatMap { i -> input.map { j -> Pair(i,j)} }.map { (i, j) ->
            computeMagnitude(reduceNumber(addNumbers(i, j)))
        }.maxByOrNull { it }!!
    }

    fun explodeNumbers2(input: String): String {
        var countOpen = 0
        var chosenPair = ""
        var chosenPairStartingIdx = 0
        for((idx, it) in input.withIndex()) {
            chosenPair += it

            if(it == '[') {
                countOpen += 1
                chosenPairStartingIdx = idx
                chosenPair = "["
            }
            if(it == ']') {
                if(countOpen > 4) {
                    val (left, right) = chosenPair.subSequence(1,chosenPair.length-1).split(",").map { it.toInt() }

                    // calculate left chunk
                    val previous = input.subSequence(0,chosenPairStartingIdx).withIndex().findLast { it.value in '0'..'9' }
                    val leftChunk = if(previous != null) {
                        val (previousNumberIdx, previousNumberChar) = previous
                        val (previousNumberIdxRange, previousNumber) =
                            if (input[previousNumberIdx - 1] in '0'..'9') {
                                Pair(
                                    previousNumberIdx - 1..previousNumberIdx,
                                    input[previousNumberIdx - 1] + previousNumberChar.toString()
                                )
                            } else {
                                Pair(previousNumberIdx..previousNumberIdx, previousNumberChar.toString())
                            }
                        val newPrevious = left + previousNumber.toInt()
                        input.subSequence(0, chosenPairStartingIdx).replaceRange(previousNumberIdxRange, newPrevious.toString()).toString()
                    } else input.subSequence(0, chosenPairStartingIdx).toString()

                    val remaining = input.subSequence(chosenPairStartingIdx+chosenPair.length, input.length)
                    val next = remaining.withIndex().firstOrNull { it.value in '0'..'9' }
                    val rightChunk: String = if(next != null) {
                        val (nextNumberIdx, nextNumberChar) = next
                        val (nextNumberIdxRange, nextNumber) =
                            if (remaining[nextNumberIdx + 1] in '0'..'9') {
                                Pair(
                                    nextNumberIdx..nextNumberIdx + 1,
                                    nextNumberChar.toString() + remaining[nextNumberIdx + 1]
                                )
                            } else {
                                Pair(nextNumberIdx..nextNumberIdx, nextNumberChar.toString())
                            }

                        val newNext = right + nextNumber.toInt()

                        remaining.replaceRange(
                            nextNumberIdxRange,
                            newNext.toString()
                        ).toString()
                    } else input.subSequence(chosenPairStartingIdx+chosenPair.length, input.length).toString()

                    return leftChunk + "0" + rightChunk

                } else input.subSequence(chosenPairStartingIdx + chosenPair.length, input.length)
                countOpen -= 1
                chosenPair = ""
            }
        }

        return input
    }

    fun findDeepestFirstNesting(input: String): Int {
        var countOpen = 0
        for((idx, it) in input.withIndex()) {
            if(it == '[') countOpen += 1
            if(it == ']') {
                if(countOpen > 4) {
                    return idx-2
                }
                countOpen -= 1
            }
        }

        return -1
    }

    fun explodeNumbers(input: String): String {
        val commaLocation = findDeepestFirstNesting(input)
        if(commaLocation == -1) return input

        val left = input[commaLocation-1].toString().toInt()
        val right = input[commaLocation+1].toString().toInt()

        val leftReplaceIndex = (commaLocation-2 downTo 0).find { input[it] in '0'..'9' }
        var rightReplaceIndex = (commaLocation+2..input.length-1).find { input[it] in '0'..'9' }

        var output: String = input
        var leftSizePlusOne = false
        if(leftReplaceIndex != null) {
            val leftReplace = input[leftReplaceIndex].toString().toInt()
            output = output.replaceRange(leftReplaceIndex, leftReplaceIndex+1, (left + leftReplace).toString())
            if(left + leftReplace > 9) leftSizePlusOne = true
        }
        if(rightReplaceIndex != null) {
            if(leftSizePlusOne) rightReplaceIndex += 1

            val rightReplace = output[rightReplaceIndex].toString().toInt()
            output = output.replaceRange(rightReplaceIndex, rightReplaceIndex+1, (right + rightReplace).toString())
        }


        if(leftReplaceIndex != null && leftSizePlusOne) {
            output = output.replaceRange(commaLocation-1, commaLocation+4, "0")
        } else {
            output = output.replaceRange(commaLocation-2, commaLocation+3, "0")
        }


        return output
    }

    fun splitNumber(input: String): String {
        val regex = Pattern.compile("([0-9][0-9]+)").toRegex()
        if(!regex.containsMatchIn(input)) {
            return input
        }

        val matchingNumberStr = regex.find(input)!!.groups[0]!!.value
        val matchingNumber = matchingNumberStr.toInt()

        val left = floor(matchingNumber/2.0).toInt()
        val right = ceil(matchingNumber/2.0).toInt()

        return input.replaceFirst(matchingNumberStr, "[$left,$right]")
    }

    fun addNumbers(valOne: String, valTwo: String): String {
        return "[$valOne,$valTwo]"
    }
}

fun main() {
    Day18().run()
}
