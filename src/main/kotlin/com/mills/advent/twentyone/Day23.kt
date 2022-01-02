package com.mills.advent.twentyone

import com.mills.advent.support.AdventOfCode
import com.mills.advent.support.Coord
import com.mills.advent.support.getCoord
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

const val MAX = Int.MAX_VALUE / 2

enum class Occupier(
    val char: Char,
    val energy: Int
) {
    EMPTY('.', 0), A('A', 1), B('B', 10), C('C', 100), D('D', 1000)
}

data class Room(
    val contents: List<Occupier>,
    val desiredOccupier: Occupier,
    val col: Int
)

data class Grid(
    val corridor: List<Occupier>, // 11 items, with 4 illegal spaces
    val roomA: List<Occupier>,
    val roomB: List<Occupier>,
    val roomC: List<Occupier>,
    val roomD: List<Occupier>,
    val roomSize: Int
) {

    val roomAObj = Room(roomA, Occupier.A, 2)
    val roomBObj = Room(roomB, Occupier.B, 4)
    val roomCObj = Room(roomC, Occupier.C, 6)
    val roomDObj = Room(roomD, Occupier.D, 8)

    val corridorIndices = listOf(0,1,3,5,7,9,10)

    fun print() {
        println()
        corridor.forEach { print(it.char) }
        println()
        (roomSize downTo 1).forEach { idx ->
            println("  ${roomA.getOrElse(idx-1) {Occupier.EMPTY.char}} ${roomB.getOrElse(idx-1) {Occupier.EMPTY.char}} ${roomC.getOrElse(idx-1) { Occupier.EMPTY.char }} ${roomD.getOrElse(idx-1){Occupier.EMPTY.char}} ")
        }
        println()
    }

    fun getDestinationRoom(char: Occupier): Room =
        when(char) {
            Occupier.A -> roomAObj
            Occupier.B -> roomBObj
            Occupier.C -> roomCObj
            Occupier.D -> roomDObj
            else -> throw IllegalArgumentException()
        }

    fun getCopy(
        from: Pair<Occupier, List<Occupier>>,
        to: Pair<Occupier, List<Occupier>>
    ) = this.copy(
        corridor = if(from.first == Occupier.EMPTY) from.second else if (to.first == Occupier.EMPTY) to.second else corridor,
        roomA = if(from.first == Occupier.A) from.second else if (to.first == Occupier.A) to.second else roomA,
        roomB = if(from.first == Occupier.B) from.second else if (to.first == Occupier.B) to.second else roomB,
        roomC = if(from.first == Occupier.C) from.second else if (to.first == Occupier.C) to.second else roomC,
        roomD = if(from.first == Occupier.D) from.second else if (to.first == Occupier.D) to.second else roomD
    )

    val corridorEmptyCache: MutableMap<Pair<Int,Int>, Boolean> = mutableMapOf()

    fun isCorridorRouteEmpty(from: Int, to: Int) =
        corridorEmptyCache.computeIfAbsent(from to to) {
            corridor.subList(
                min(from, to)+1,
                max(from, to)
            ).all { it == Occupier.EMPTY }
        }


    fun getValidNextGrids(): Map<Grid, Int> {
        val emptyCorridorIndices = corridorIndices.filter { corridor[it] == Occupier.EMPTY }

        val roomMoves = listOf(
            roomAObj, roomBObj, roomCObj, roomDObj
        ).filter { fromRoom ->
            fromRoom.contents.any { it != fromRoom.desiredOccupier }
        }.flatMap { fromRoom ->
            val modifiedRoom = fromRoom.contents.toMutableList()
            val charToMove = modifiedRoom.removeLastOrNull()

            if(charToMove == Occupier.EMPTY)
                throw IllegalArgumentException()

            if(charToMove != null) {
                // other rooms
                val destRoom = getDestinationRoom(charToMove)
                val outRoomList = if(
                    destRoom.contents.size != roomSize &&
                    destRoom.desiredOccupier != fromRoom.desiredOccupier &&
                    isCorridorRouteEmpty(fromRoom.col, destRoom.col) &&
                    destRoom.contents.all { it == destRoom.desiredOccupier }
                ) {
                    val modifiedDest = destRoom.contents.toMutableList()
                    modifiedDest.add(charToMove)

                    val energy = ((roomSize-modifiedRoom.size) + (roomSize-destRoom.contents.size) + abs(fromRoom.col - destRoom.col))*charToMove.energy

                    listOf(getCopy(
                        fromRoom.desiredOccupier to modifiedRoom,
                        destRoom.desiredOccupier to modifiedDest
                    ) to energy)
                } else emptyList()

                // corridor
                val outCorridorList = emptyCorridorIndices.filter {
                    isCorridorRouteEmpty(fromRoom.col, it)
                }.map {
                    val modifiedCorridor = corridor.toMutableList()
                    modifiedCorridor[it] = charToMove

                    // energy calculation
                    val energy = ((roomSize-modifiedRoom.size) + abs(it - fromRoom.col))*charToMove.energy

                    getCopy(
                        fromRoom.desiredOccupier to modifiedRoom,
                        Occupier.EMPTY to modifiedCorridor
                    ) to energy
                }

                outRoomList + outCorridorList

            } else emptyList()
        }

        val corridorMoves = corridorIndices.associateWith {
            corridor[it]
        }.filter {
                (_, value) -> value != Occupier.EMPTY
        }.flatMap { (idx, charToMove) ->
            val destRoom = getDestinationRoom(charToMove)
            if(
                destRoom.contents.size != roomSize &&
                destRoom.contents.all { it == destRoom.desiredOccupier} &&
                isCorridorRouteEmpty(destRoom.col, idx)
            ) {
                val modifiedDest = destRoom.contents.toMutableList()
                modifiedDest.add(charToMove)

                val modifiedCorridor = corridor.toMutableList()
                modifiedCorridor[idx] = Occupier.EMPTY

                val energy = ((roomSize-destRoom.contents.size) + abs(idx - destRoom.col))*charToMove.energy

                listOf(getCopy(
                    Occupier.EMPTY to modifiedCorridor,
                    destRoom.desiredOccupier to modifiedDest
                ) to energy)
            } else emptyList()
        }

        return (roomMoves + corridorMoves).associate { it }
                    // corridor indices = 0,1,3,5,7,9,10
    }

}

fun Char.toOccupier() =
    when(this) {
        '.' -> Occupier.EMPTY
        'A' -> Occupier.A
        'B' -> Occupier.B
        'C' -> Occupier.C
        'D' -> Occupier.D

        else -> Occupier.EMPTY
    }

class Day23 : AdventOfCode {
    private fun getInputText(): String = Day23::class.java.getResource("day23.txt")?.readText()!!

    override fun day(): String = "23"

    val corridorCoords = listOf(
        Coord(1, 1),
        Coord(2, 1),
        Coord(3, 1),
        Coord(4, 1),
        Coord(5, 1),
        Coord(6, 1),
        Coord(7, 1),
        Coord(8, 1),
        Coord(9, 1),
        Coord(10, 1),
        Coord(11, 1),
    )

    override fun part1(): Int {
        val grid = getInputText().split("\n").map { it.chunked(1).map { it[0] }.toMutableList() }

        val startingGrid = Grid(
            corridorCoords.map { grid.getCoord(it).toOccupier() }.reversed(),
            listOf(
                Coord(3, 2),
                Coord(3, 3)
            ).map { grid.getCoord(it).toOccupier() }.reversed(),
            listOf(
                Coord(5, 2),
                Coord(5, 3)
            ).map { grid.getCoord(it).toOccupier() }.reversed(),
            listOf(
                Coord(7, 2),
                Coord(7, 3)
            ).map { grid.getCoord(it).toOccupier() }.reversed(),
            listOf(
                Coord(9, 2),
                Coord(9, 3)
            ).map { grid.getCoord(it).toOccupier() }.reversed(),
            2
        )

        return solve(startingGrid)
    }

    fun solve(startingGrid: Grid): Int {

        val endGrid = Grid(
            List(startingGrid.corridor.size) { Occupier.EMPTY},
            List(startingGrid.roomA.size) { Occupier.A },
            List(startingGrid.roomB.size) { Occupier.B },
            List(startingGrid.roomC.size) { Occupier.C },
            List(startingGrid.roomD.size) { Occupier.D },
            startingGrid.roomSize
        )
        var currentGrid: Grid = startingGrid.copy()

        val visited = mutableSetOf(startingGrid)
        val unvisited = mutableSetOf<Grid>()
        val distances = mutableMapOf(currentGrid to 0)
        val previous = mutableMapOf<Grid, Grid>()

        val unvisitedCache = mutableSetOf<Grid>()
        var unvisitedCacheMax = 0


        while(currentGrid != endGrid) {
            val currentCost = distances[currentGrid]!!

            currentGrid.getValidNextGrids().minus(visited).forEach { (neighbour, energy) ->
                val newCost = currentCost + energy

                distances.compute(neighbour) { _, storedEnergy ->
                    if(storedEnergy == null) newCost else min(newCost, storedEnergy)
                }

                unvisited.add(neighbour)

                if(newCost < unvisitedCacheMax) {
                    unvisitedCache.add(neighbour)
                }

                previous[neighbour] = currentGrid
            }

            unvisited.remove(currentGrid)
            unvisitedCache.remove(currentGrid)
            visited.add(currentGrid)

            if(unvisitedCache.isEmpty()) {
                val toAdd = unvisited.map { it to distances[it]!! }.sortedBy { it.second }.take(500)
                unvisitedCacheMax = toAdd.maxOf { it.second }
                unvisitedCache.addAll(
                    toAdd.map { it.first }
                )
            }

            currentGrid = unvisitedCache.first()
        }

        // Printing route
//        var previousGrid: Grid? = currentGrid
//        while(previousGrid != null) {
//            println(distances[previousGrid])
//            previousGrid.print()
//            previousGrid = previous[previousGrid]
//        }

        return distances[currentGrid]!!
    }


    override fun part2(): Int {
        val grid = Day23::class.java.getResource("day23-2.txt")?.readText()!!.split("\n").map { it.chunked(1).map { it[0] }.toMutableList() }

        val startingGrid = Grid(
            corridorCoords.map { grid.getCoord(it).toOccupier() }.reversed(),
            listOf(
                Coord(3, 2),
                Coord(3, 3),
                Coord(3, 4),
                Coord(3, 5)
            ).map { grid.getCoord(it).toOccupier() }.reversed(),
            listOf(
                Coord(5, 2),
                Coord(5, 3),
                Coord(5, 4),
                Coord(5, 5)
            ).map { grid.getCoord(it).toOccupier() }.reversed(),
            listOf(
                Coord(7, 2),
                Coord(7, 3),
                Coord(7, 4),
                Coord(7, 5)
            ).map { grid.getCoord(it).toOccupier() }.reversed(),
            listOf(
                Coord(9, 2),
                Coord(9, 3),
                Coord(9, 4),
                Coord(9, 5)
            ).map { grid.getCoord(it).toOccupier() }.reversed(),
            4
        )

        return solve(startingGrid)
    }
}

fun main() {
    Day23().run()
}
