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
//        val gridCosts = mutableMapOf(currentGrid to 0)
        val gridCostsBackup = mutableMapOf(currentGrid to 0)
        val gridCosts = sortedMapOf(0 to mutableSetOf(currentGrid))
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

                newGrid to gridCostsBackup[currentGrid]!! + cost
            }.filter {
                it.key !in visitedNodes
            }.forEach{ (neighbour, cost) ->
                val storedCost = gridCostsBackup.getOrDefault(neighbour, MAX)
                if(storedCost > cost) {
                    gridCostsBackup[neighbour] = cost
                    previous[neighbour] = currentGrid

                    val storedSet = gridCosts.computeIfAbsent(storedCost) { mutableSetOf() }
                    storedSet.remove(neighbour)
                    if(storedSet.isEmpty()) {
                        gridCosts.remove(storedCost)
                    } else {
                        gridCosts[storedCost] = storedSet
                    }

                    val newSet = gridCosts.computeIfAbsent(cost) { mutableSetOf() }
                    newSet.add(neighbour)
                    gridCosts[cost] = newSet
                }
            }

            visitedNodes.add(currentGrid)

            val nextGrid = gridCosts[gridCosts.firstKey()]?.first()
            currentGrid = nextGrid!!
        }

        var printingGrid = currentGrid
        while(printingGrid != grid) {
            println(gridCostsBackup[printingGrid])
            printingGrid.print()
            printingGrid = previous[printingGrid]!!
        }

        return gridCostsBackup[currentGrid]!!
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

    fun getValidMoves(grid: Map<Coord, Char>): List<Pair<Coord, Coord>> {
        val emptyCorridorSpaces = corridorCoords.filter {
            grid[it] == '.'
        }

        val outOfRoomMoves = mapOf('A' to aRoom, 'B' to bRoom, 'C' to cRoom, 'D' to dRoom).flatMap { (char, room) ->
            val room0Value = grid[room[0]]
            val room1Value = grid[room[1]]
            when {
                room1Value != char && room0Value == '.' -> listOf(room1Value!! to room[1])
                room1Value != char && room0Value == char -> listOf(room0Value to room[0])
                room0Value != char -> listOf(room0Value!! to room[0])
                else -> listOf()
            }
        }.flatMap { (char, roomCoord) ->
            val corridorPairs = emptyCorridorSpaces.map { corridorCoord ->
                roomCoord to corridorCoord
            }

            val otherRoom = getRoom(char)

            val otherRoomPair = if(otherRoom.isNotEmpty()) {
                val room0Value = grid[otherRoom[0]]
                val room1Value = grid[otherRoom[1]]

                when {
                    room0Value == '.' && room1Value == '.' -> roomCoord to otherRoom[1]
                    room0Value == '.' && room1Value == char -> roomCoord to otherRoom[0]
                    else -> null
                }
            } else null

            if(otherRoomPair != null) corridorPairs + otherRoomPair
            else corridorPairs
        }

        val outOfCorridorMoves = corridorCoords.associateWith { corridorCoord ->
            grid[corridorCoord]!!
        }.filter {
            it.value != '.'
        }.flatMap { (corridorCoord, char) ->
            val room = getRoom(char)
            if (grid[room[0]] == '.') {
                when (grid[room[1]]) {
                    '.' -> listOf(corridorCoord to room[1], corridorCoord to room[0])
                    char -> listOf(corridorCoord to room[0])
                    else -> listOf()
                }
            } else {
                listOf()
            }
        }

        return (outOfRoomMoves + outOfCorridorMoves).filter { (from, to) ->
            // is it a clear path?

            val steps = if (from.y == 1) {
                // from corridor to room
//                Coord(to.x, from.y)
                (min(from.x, to.x)..max(from.x, to.x)).map { x -> Coord(x, from.y) } +
                        (min(from.y, to.y)..max(from.y, to.y)).map { y -> Coord(to.x, y) }
            }
            else if(to.y == 1) {
                // from room to corridor
//                Coord(from.x, to.y)

                (min(from.x, to.x)..max(from.x, to.x)).map { x -> Coord(x, to.y) } +
                        (min(from.y, to.y)..max(from.y, to.y)).map { y -> Coord(from.x, y) }
            } else {
                // from room to room
                (min(from.x, to.x)..max(from.x, to.x)).map { x -> Coord(x, 1) } +
                        (1..from.y).map { y -> Coord(from.x, y) } +
                        (1..to.y).map { y -> Coord(to.x, y) }
            }



            steps.filter {
                it != from
            }.all {
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
