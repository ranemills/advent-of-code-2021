package com.mills.advent.twentyone

import com.mills.advent.support.AdventOfCode
import com.mills.advent.support.Coord
import com.mills.advent.support.Grid
import com.mills.advent.support.getCoord
import java.util.*
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class Day23Djikstra : AdventOfCode {
    private fun getInputText(): String = Day23::class.java.getResource("day23.txt")?.readText()!!

    override fun day(): String = "23"

    val corridorCoords = listOf(
        Coord(1, 1),
        Coord(2, 1),
        Coord(4, 1),
        Coord(6, 1),
        Coord(8, 1),
        Coord(10, 1),
        Coord(11, 1),
    )
    val aRoom = listOf(
        Coord(3, 2),
        Coord(3, 3)
    )
    val bRoom = listOf(
        Coord(5, 2),
        Coord(5, 3)
    )
    val cRoom = listOf(
        Coord(7, 2),
        Coord(7, 3)
    )
    val dRoom = listOf(
        Coord(9, 2),
        Coord(9, 3)
    )

    override fun part1(): Int {
        val grid = getInputText().split("\n").withIndex().flatMap { (y, row) ->
            row.chunked(1).withIndex().map { (x, cell) ->
                Coord(x,y) to cell[0]
            }
        }.filter{ (_, value) -> value != '#' && value != '\r' }
            .associate { it }

        var currentGrid: Map<Coord, Char> = grid
        val gridCosts = mutableMapOf(currentGrid to 0)
        val gridCostsBackup = mutableMapOf(currentGrid to 0)
//        val gridCosts = sortedMapOf(0 to mutableSetOf(currentGrid))
        val visitedNodes = mutableSetOf(grid)
        val previous: MutableMap<Map<Coord,Char>, Map<Coord,Char>> = mutableMapOf()

        while(!isGridComplete(currentGrid)) {
            getValidMoves(currentGrid).associate { (start, end) ->
                val startValue = currentGrid[start]!!
                val energy = getEnergy(startValue)

                val moves = if(start.y != 1 && end.y != 1) {
                    // room-to-room
                    end.y-1 + start.y-1 + abs(end.x-start.x)
                } else {
                    val (diffX, diffY) = end - start
                    abs(diffX) + abs(diffY)
                }
                val cost = moves*energy
                val newGrid = currentGrid.toMutableMap()
                newGrid[end] = startValue
                newGrid[start] = '.'

                if(newGrid == currentGrid) {
                    throw IllegalStateException()
                }

                newGrid to gridCostsBackup[currentGrid]!! + cost
            }.filter {
                it.key !in visitedNodes
            }.forEach{ (neighbour, cost) ->
                val storedCost = gridCostsBackup.getOrDefault(neighbour, MAX)
                if(storedCost > cost) {
                    gridCostsBackup[neighbour] = cost
                    gridCosts[neighbour] = cost
                }
            }

            visitedNodes.add(currentGrid)
            gridCosts.remove(currentGrid)

            val nextGrid = gridCosts.minByOrNull { it.value }?.key

            if(currentGrid == nextGrid) {
                throw IllegalStateException()
            }

            if(nextGrid == null) {
                throw IllegalStateException()
            }

            currentGrid = nextGrid
        }

        var printingGrid = currentGrid
        while(printingGrid != grid) {
            println(gridCostsBackup[printingGrid])
            printingGrid.print()
            printingGrid = previous[printingGrid]!!
        }

        return gridCostsBackup[currentGrid]!!
    }

    fun removeGridCost(gridCosts: SortedMap<Int, MutableSet<Map<Coord, Char>>>, grid: Map<Coord, Char>, cost: Int) {
        val storedSet = gridCosts.computeIfAbsent(cost) { mutableSetOf() }
        storedSet.remove(grid)
        if(storedSet.isEmpty()) {
            gridCosts.remove(cost)
        } else {
            gridCosts[cost] = storedSet
        }
    }

    fun Map<Coord, Char>.print() {
        (0..this.maxOf { it.key.y }).forEach { y ->
            (0..this.maxOf {it.key.x}).forEach { x ->
                print(this.getOrDefault(Coord(x,y), '#'))
            }
            println()
        }
        println()
    }

    val stepCountCache = mutableMapOf<Pair<Coord, Coord>, List<Coord>>()

    fun getValidMoves(grid: Map<Coord, Char>): List<Pair<Coord, Coord>> {
        val emptyCorridorSpaces = corridorCoords.filter {
            grid[it] == '.'
        }

        val lastEmptyForRoom =  mapOf('A' to aRoom, 'B' to bRoom, 'C' to cRoom, 'D' to dRoom).mapValues { (char, room) ->
            val room0Value = grid[room[0]]
            val room1Value = grid[room[1]]

            when {
                room0Value != '.' -> null
                room1Value == '.' -> room[1]
                room1Value == char -> room[0]
                else -> null
            }
        }

        val outOfRoomMoves = mapOf('A' to aRoom, 'B' to bRoom, 'C' to cRoom, 'D' to dRoom).flatMap { (char, room) ->
            val room0Value = grid[room[0]]
            val room1Value = grid[room[1]]
            when {
                room0Value == char && room1Value == char -> listOf()
                room0Value != '.' -> listOf(room0Value!! to room[0])
                room1Value !in listOf('.', char) -> listOf(room1Value to room[1])
                else -> listOf()
            }
        }.flatMap { (char, roomCoord) ->
            val corridorPairs = emptyCorridorSpaces.map { corridorCoord ->
                roomCoord to corridorCoord
            }

            lastEmptyForRoom[char]?.let { corridorPairs + (roomCoord to it) } ?: corridorPairs
        }

        val outOfCorridorMoves = corridorCoords.associateWith { corridorCoord ->
            grid[corridorCoord]!!
        }.filter {
            it.value != '.'
        }.flatMap { (corridorCoord, char) ->
            lastEmptyForRoom[char]?.let { listOf(corridorCoord to it) } ?: listOf()
        }

        return (outOfRoomMoves + outOfCorridorMoves).filter { (from, to) ->
            val steps = stepCountCache.computeIfAbsent(Pair(from, to)) {
                if (from.y == 1) {
                    // from corridor to room
                    (min(from.x, to.x)..max(from.x, to.x)).map { x -> Coord(x, from.y) } +
                            (min(from.y, to.y)..max(from.y, to.y)).map { y -> Coord(to.x, y) }
                } else if (to.y == 1) {
                    // from room to corridor
                    (min(from.x, to.x)..max(from.x, to.x)).map { x -> Coord(x, to.y) } +
                            (min(from.y, to.y)..max(from.y, to.y)).map { y -> Coord(from.x, y) }
                } else {
                    // from room to room
                    (min(from.x, to.x)..max(from.x, to.x)).map { x -> Coord(x, 1) } +
                            (1..from.y).map { y -> Coord(from.x, y) } +
                            (1..to.y).map { y -> Coord(to.x, y) }
                }.filter {
                    it != from
                }
            }

            steps.all {
                grid[it] == '.'
            }

        }
    }

    fun getRoom(c: Char) = when (c) {
        'A' -> aRoom
        'B' -> bRoom
        'C' -> cRoom
        'D' -> dRoom
        else -> listOf()
    }

    fun getEnergy(c: Char) = when (c) {
        'A' -> 1
        'B' -> 10
        'C' -> 100
        'D' -> 1000
        else -> 0
    }

    fun isRoomComplete(grid: Map<Coord, Char>, room: List<Coord>, c: Char) =
        room.all { grid[it] == c }

    fun isGridComplete(grid: Map<Coord, Char>): Boolean =
        isRoomComplete(grid, aRoom, 'A') &&
                isRoomComplete(grid, bRoom, 'B') &&
                isRoomComplete(grid, cRoom, 'C') &&
                isRoomComplete(grid, dRoom, 'D') &&
                isRoomComplete(grid, corridorCoords, '.')


    override fun part2() {
    }
}

fun main() {
    Day23Djikstra().run()
}
