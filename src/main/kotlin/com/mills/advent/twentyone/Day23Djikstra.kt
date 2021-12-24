package com.mills.advent.twentyone

import com.mills.advent.support.AdventOfCode
import com.mills.advent.support.Coord
import com.mills.advent.support.Grid
import com.mills.advent.support.getCoord
import java.util.Comparator
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
        val grid = getInputText().split("\n").map { it.chunked(1).map { it[0] }.toMutableList() }

        var currentGrid: Grid<Char> = grid
        val gridCosts = mutableMapOf(currentGrid to 0)
        val visitedNodes = mutableSetOf<Grid<Char>>(grid)

        while(!isGridComplete(currentGrid)) {
            getValidMoves(currentGrid).associate { (start, end) ->
                val startValue = currentGrid.getCoord(start)

                val newGrid = currentGrid.copyGrid()
                newGrid[end.y][end.x] = startValue
                newGrid[start.y][start.x] = '.'

                val energy = getEnergy(startValue)
                val (diffX, diffY) = end - start
                val cost = (abs(diffX) + abs(diffY)) * energy

                newGrid to gridCosts[currentGrid]!! + cost
            }.filter {
                it.key !in visitedNodes
            }.forEach{ (neighbour, cost) ->
                if(gridCosts.getOrDefault(neighbour, MAX) > cost) {
                    gridCosts[neighbour] = cost
                }
            }

            visitedNodes.add(currentGrid)
            gridCosts.remove(currentGrid)
            val nextGrid = gridCosts.minByOrNull { it.value }?.key
            if(nextGrid == null) {
                throw NoSuchElementException()
            }
            else currentGrid = nextGrid
        }

        return gridCosts[currentGrid]!!
    }

    fun Grid<Char>.copyGrid() = List(this.size) { idx -> this[idx].toMutableList() }

    var minSoFar = MAX

    fun getValidMoves(grid: Grid<Char>): List<Pair<Coord, Coord>> {
        val outOfRoomMoves = mapOf('A' to aRoom, 'B' to bRoom, 'C' to cRoom, 'D' to dRoom).flatMap { (char, room) ->
            when {
                grid.getCoord(room[1]) != char && grid.getCoord(room[0]) == '.' -> listOf(room[1])
                grid.getCoord(room[1]) != char && grid.getCoord(room[0]) == char -> listOf(room[0])
                grid.getCoord(room[0]) != char -> listOf(room[0])
                else -> listOf()
            }
        }.flatMap { roomCoord ->
            val corridorPairs = corridorCoords.filter { grid.getCoord(it) == '.' }.map { corridorCoord ->
                roomCoord to corridorCoord
            }
            val char = grid.getCoord(roomCoord)
            val otherRoom = getRoom(char)
            val otherRoomPairs = if(otherRoom.isNotEmpty()) {
                if (grid.getCoord(otherRoom[0]) == '.') {
                    when (grid.getCoord(otherRoom[1])) {
                        '.' -> {
                            listOf(roomCoord to otherRoom[1])
                        }
                        char -> {
                            listOf(roomCoord to otherRoom[0])
                        }
                        else -> {
                            listOf()
                        }
                    }
                } else {
                    listOf()
                }
            } else listOf()


            corridorPairs + otherRoomPairs
        }

        val outOfCorridorMoves = corridorCoords.associateWith { corridorCoord -> grid.getCoord(corridorCoord) }.filter {
            it.value != '.'
        }.flatMap { (corridorCoord, char) ->
            val room = getRoom(char)
            if (grid.getCoord(room[0]) == '.') {
                when (grid.getCoord(room[1])) {
                    '.' -> {
                        listOf(corridorCoord to room[1], corridorCoord to room[0])
                    }
                    char -> {
                        listOf(corridorCoord to room[0])
                    }
                    else -> {
                        listOf()
                    }
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
                grid.getCoord(it) == '.'
            }

        }
    }

    fun moveAmphipods(grid: Grid<Char>, locked: List<Coord>, visited: List<Grid<Char>>, costSoFar: Int): Pair<List<Pair<Coord, Coord>>, Int> {
//        println()
//        grid.forEach { y ->
//            println(y)
//        }

        if(costSoFar > minSoFar)
            return listOf<Pair<Coord, Coord>>() to MAX

        if (isGridComplete(grid)) {
            println("Success! $costSoFar")
            minSoFar = min(costSoFar, minSoFar)
            return listOf<Pair<Coord, Coord>>() to costSoFar
        }

        val outOfRoomMoves = mapOf('A' to aRoom, 'B' to bRoom, 'C' to cRoom, 'D' to dRoom).flatMap { (char, room) ->
            when {
                grid.getCoord(room[1]) != char && grid.getCoord(room[0]) == '.' -> listOf(room[1])
                grid.getCoord(room[1]) != char && grid.getCoord(room[0]) == char -> listOf(room[0])
                grid.getCoord(room[0]) != char -> listOf(room[0])
                else -> listOf()
            }
        }.filter {
            it !in locked
        }.flatMap { roomCoord ->
            val corridorPairs = corridorCoords.filter { grid.getCoord(it) == '.' }.map { corridorCoord ->
                roomCoord to corridorCoord
            }
            val otherRoom = getRoom(grid.getCoord(roomCoord))
            val otherRoomPairs = if(otherRoom.isNotEmpty()) {
                if (grid.getCoord(otherRoom[0]) == '.') {
                    if (grid.getCoord(otherRoom[1]) == '.') {
                        listOf(roomCoord to otherRoom[1])
                    } else {
                        listOf(roomCoord to otherRoom[0])
                    }
                } else {
                    listOf()
                }
            } else listOf()


            corridorPairs + otherRoomPairs
        }

        val outOfCorridorMoves = corridorCoords.associate { corridorCoord ->
            corridorCoord to grid.getCoord(corridorCoord)
        }.filter {
            it.value != '.'
        }.flatMap { (corridorCoord, char) ->
            val room = getRoom(char)
            if (grid.getCoord(room[0]) == '.') {
                if (grid.getCoord(room[1]) == '.') {
                    listOf(corridorCoord to room[1], corridorCoord to room[0])
                } else {
                    listOf(corridorCoord to room[0])
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
                grid.getCoord(it) == '.'
            }

        }.map { (start, end) ->
            // if going into a room, lock the coord
            val newLocked = if (start.y > end.y) locked + end else locked

            val newGrid = grid.copyGrid()
            newGrid[end.y][end.x] = grid.getCoord(start)
            newGrid[start.y][start.x] = grid.getCoord(end)

            val energy = getEnergy(grid.getCoord(start))
            val (diffX, diffY) = end - start
            val cost = (abs(diffX) + abs(diffY)) * energy

            val newCostSoFar = costSoFar + cost
            if(newGrid !in visited) {
                val ret = moveAmphipods(newGrid, newLocked, visited + listOf(newGrid as Grid<Char>), newCostSoFar)

                (listOf(start to end) + ret.first) to ret.second
            } else {
                listOf<Pair<Coord, Coord>>() to MAX
            }
        }.minByOrNull {
            it.second
        } ?: (listOf<Pair<Coord, Coord>>() to MAX)
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

    fun isRoomComplete(grid: Grid<Char>, room: List<Coord>, c: Char) =
        room.map { grid.getCoord(it) }.all { it == c }

    fun isGridComplete(grid: Grid<Char>): Boolean =
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
