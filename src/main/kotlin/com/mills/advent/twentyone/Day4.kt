package com.mills.advent.twentyone

import com.mills.advent.support.AdventOfCode
import java.util.regex.Pattern

class Day4 : AdventOfCode {
    private fun getInputText(): String = Day4::class.java.getResource("day4.txt")?.readText()!!

    override fun part1(): Int {
        val lines = getInputText().split("\n\n")
        val numbersCalled = lines[0].split(",").map { it.toInt() }
        val bingoBoards: MutableList<BingoBoard> = lines.subList(1,lines.size).map {
            it.split("\n").map { line ->
                line.trim().split(Pattern.compile("\\s+")).map { number ->
                    number.toInt()
                }.toMutableList()
            }
        }.map {
            BingoBoard(it)
        }.toMutableList()

        for( number in numbersCalled ) {
            for(board in bingoBoards) {
                board.markNumber(number)
                if(board.hasBingo()) {
                    return board.unmarkedSum()*number
                }
            }
        }
        return 0
    }

    override fun part2():Int {
        val lines = getInputText().split("\n\n")
        val numbersCalled = lines[0].split(",").map { it.toInt() }
        val bingoBoards: MutableList<BingoBoard> = lines.subList(1,lines.size).map {
            it.split("\n").map { line ->
                line.trim().split(Pattern.compile("\\s+")).map { number ->
                    number.toInt()
                }.toMutableList()
            }
        }.map {
            BingoBoard(it)
        }.toMutableList()

        for( number in numbersCalled ) {
            val boards = bingoBoards.filter { it.active }

            for(board in boards) {
                board.markNumber(number)
                if(board.hasBingo()) {
                    board.active = false
                    if(boards.size == 1) {
                        return board.unmarkedSum()*number
                    }
                }
            }
        }
        return 0
    }

    override fun day(): String = "4"
}

fun main() {
    Day4().run()
}

class BingoBoard(rows: List<List<Int>>, var active: Boolean = true) {
    private val matchableRows: List<MutableList<Int>>
    private val matchableColumns: List<MutableList<Int>>

    init {
        matchableRows = rows.map {it.toMutableList()}
        val runs: MutableList<MutableList<Int>> = rows.map {it.toMutableList()}.toMutableList()
        for(column in rows.indices) {
            val columnMatch: MutableList<Int> = listOf<Int>().toMutableList()
            for (row in rows.indices) {
                columnMatch.add(rows[row][column])
            }
            runs.add(columnMatch)
        }
        matchableColumns = runs
    }

    fun unmarkedSum(): Int = matchableRows.sumOf { row -> row.sum() }


    fun markNumber(number: Int): Unit {
        for(run in matchableRows +matchableColumns) {
            if(run.contains(number)) { run.remove(number) }
        }
    }

    fun hasBingo(): Boolean {
        for(run in matchableRows + matchableColumns) {
            if(run.isEmpty()) {
                return true
            }
        }
        return false
    }
}