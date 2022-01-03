package com.mills.advent.twentyone

import com.mills.advent.support.AdventOfCode

class Day24 : AdventOfCode {
    private fun getInputText(): String = Day24::class.java.getResource("day24.txt")?.readText()!!

    private val registerKeys = setOf('w', 'x', 'y', 'z')

    override fun day(): String = "24"

    override fun part1(): Long {
        val instructions = getInputText().split("\n")

        val parsedInstructions = parseInstructions(instructions)

//        for (i in 99999999999999 downTo 11111111111111) {
        for (i in 13579246899999 downTo 13579246899999) {
            if (i.toString().contains('0')) continue
            val (outRegister, _) = parsedInstructions.fold(
                mutableMapOf(
                    'w' to 0L,
                    'x' to 0L,
                    'y' to 0L,
                    'z' to 0L
                ) to i.toString()
            ) { acc, function ->
                val newInput = function(acc.first, acc.second)
                println(acc.first)
                acc.first to newInput
            }
//            print(outRegister)
            if (outRegister['z']!! == 0L) return i
        }

        return -1
    }

    fun parseInstructions(instructions: List<String>): List<(MutableMap<Char, Long>, String) -> String> =
        instructions.map { instr ->
            val splitInstructions = instr.split(" ")

            val operator = splitInstructions[0]
            val var1 = splitInstructions[1][0]

            if (operator == "inp") {
                { register, input ->
                    register[var1] = input.first().toString().toLong()
                    input.substring(1)
                }
            } else {
                val var2 = splitInstructions[2]
                val valueFn: (Map<Char, Long>) -> Long = if (registerKeys.contains(var2[0])) {
                    { register -> register[var2[0]]!! }
                } else {
                    { _ -> var2.toLong() }
                }

                when (operator) {
                    "add" -> { register, input ->
                        register[var1] = register[var1]!! + valueFn(register)
                        input
                    }
                    "mul" -> { register, input ->
                        register[var1] = register[var1]!! * valueFn(register)
                        input
                    }
                    "div" -> { register, input ->
                        register[var1] = register[var1]!!.floorDiv(valueFn(register))
                        input
                    }
                    "mod" -> { register, input ->
                        register[var1] = register[var1]!! % valueFn(register)
                        input
                    }
                    "eql" -> { register, input ->
                        register[var1] = if (register[var1]!! == valueFn(register)) 1 else 0
                        input
                    }
                    else -> { _, input ->
                        input
                    }
                }
            }
        }


    fun runInstructions(instructions: List<String>, input: Long): Map<Char, Long> {
        val initialRegister = mutableMapOf(
            'w' to 0L,
            'x' to 0L,
            'y' to 0L,
            'z' to 0L
        )
        val inputString = input.toString()

        var inputPointer = 0

        return instructions.fold(initialRegister) { register, instr ->
            val splitInstructions = instr.split(" ")

            val operator = splitInstructions[0]
            val var1 = splitInstructions[1][0]

            if (operator == "inp") {
                register[var1] = inputString[inputPointer].toString().toLong()
                inputPointer += 1
            } else {
                val var2 = splitInstructions[2]
                val value = if (register.containsKey(var2[0])) {
                    register[var2[0]]!!
                } else {
                    var2.toLong()
                }

                register[var1] = when (operator) {
                    "add" -> register[var1]!! + value
                    "mul" -> register[var1]!! * value
                    "div" -> register[var1]!!.floorDiv(value)
                    "mod" -> register[var1]!! % value
                    "eql" -> if (register[var1]!! == value) 1 else 0
                    else -> register[var1]!!
                }
            }

            register
        }
    }

    override fun part2() {
    }
}

fun main() {
    Day24().run()
}
