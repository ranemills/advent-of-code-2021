package com.mills.advent.twentyone

import com.mills.advent.support.AdventOfCode
import com.mills.advent.support.Coord
import com.mills.advent.support.Grid
import com.mills.advent.support.getCoord
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

const val MAX = Int.MAX_VALUE / 2

class Day23 : AdventOfCode {
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
        val ret = moveAmphipods(grid, listOf(), 0)
        println(ret)

        return ret.second
    }

    fun Grid<Char>.copyGrid() = List(this.size) { idx -> this[idx].toMutableList() }

    val gridCache: MutableSet<Grid<Char>> = mutableSetOf()

    fun moveAmphipods(grid: Grid<Char>, locked: List<Coord>, costSoFar: Int): Pair<List<Pair<Coord, Coord>>, Int> {
//        println()
//        grid.forEach { y ->
//            println(y)
//        }

        if (isGridComplete(grid)) {
            println("Success! $costSoFar")
            return listOf<Pair<Coord, Coord>>() to costSoFar
        }

        if (grid in gridCache) {
            return listOf<Pair<Coord, Coord>>() to MAX
        }

        gridCache.add(grid)

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
            val ret = moveAmphipods(newGrid, newLocked, newCostSoFar)

            (listOf(start to end) + ret.first) to ret.second
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
    Day23().run()
}
