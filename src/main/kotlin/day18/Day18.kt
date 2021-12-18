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

    override fun part1() {
        val input = listOf(
            "[[[0,[4,5]],[0,0]],[[[4,5],[2,6]],[9,5]]]",
            "[7,[[[3,7],[4,3]],[[6,3],[8,8]]]]"
        )
//        val input = "[7,[6,[5,[4,[3,2]]]]]"

        var result = input[0]
        for(i in input.subList(1, input.size)) {
            result = reduceNumber(addNumbers(result, i))
        }
    }

    private fun reduceNumber(input: String): String {
        var processed = input

        println(input)

        while (true) {
            val reduced = explodeNumbers(processed)
            println("Explode")
            println(reduced)
            if(reduced != processed) {
                processed = reduced
                continue
            }

            val split = splitNumber(reduced)
            println("Split")
            println(split)

            if (split == processed) break

            processed = split
        }

        println("Final")
        println(processed)

        return processed
    }

//    fun parseNumber(input: String): TreeNode {
//
//    }

    override fun part2() {
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


        return input.replace(matchingNumberStr, "[$left,$right]")
    }

    fun addNumbers(valOne: String, valTwo: String): String {
        return "[$valOne,$valTwo]"
    }
}

fun main() {
    Day18().run()
}
