package day3

import kotlin.math.pow

const val DAY = "3"

class Main {
    private fun getInputText(): String = Main::class.java.getResource("input.txt")?.readText()!!

    fun part1(): Double {
        val diagnostics: List<String> = getInputText().split("\n")
        val initialValue = List(diagnostics[0].length) { 0 }

        val gammaList = diagnostics.map { it.chunked(1).map { it.toInt() } }
            .fold(initialValue) { acc, value ->
                acc.mapIndexed { idx, i ->
                    i + value[idx]
                }
            }.map {
                if (it > diagnostics.size / 2) 1 else 0
            }

        val epsilonList = gammaList.map { if (it == 1) 0 else 1 }

        return binaryToDouble(gammaList) * binaryToDouble(epsilonList)
    }

    fun part2(): Double {
        val diagnostics: List<List<Int>> = getInputText().split("\n").map { it.chunked(1).map { it.toInt() } }
        val initialValue = List(diagnostics[0].size) { 0 }

        var oxygenDiag: List<List<Int>> = diagnostics.toMutableList()
        var co2Diag: List<List<Int>> = diagnostics.toMutableList()

        for (i in initialValue.indices) {
            val oxygenCountOnes = oxygenDiag.fold(0) { acc, value -> acc + value[i] }
            val co2CountOnes = co2Diag.fold(0) { acc, value -> acc + value[i] }

            val oxygenChosenValue = if (oxygenCountOnes >= oxygenDiag.size.toDouble() / 2) 1 else 0
            val co2ChosenValue = if (co2CountOnes >= co2Diag.size.toDouble() / 2) 0 else 1

            oxygenDiag = if (oxygenDiag.size == 1) oxygenDiag else oxygenDiag.filter { it[i] == oxygenChosenValue }
            co2Diag = if (co2Diag.size == 1) co2Diag else co2Diag.filter { it[i] == co2ChosenValue }
        }

        return binaryToDouble(oxygenDiag[0]) * binaryToDouble(co2Diag[0])
    }

    private fun binaryToDouble(gammaList: List<Int>) =
        gammaList.reversed().mapIndexed { idx, value -> value * (2.toDouble().pow(idx.toDouble())) }.sum()
}

fun main() {
    println("Day $DAY")
    println("Part 1")
    println(Main().part1())
    println("Part 2")
    println(Main().part2())
}
