package com.mills.advent.twentyone

import com.mills.advent.support.AdventOfCode
import com.mills.advent.support.Coord3
import kotlin.math.abs

class Day22 : AdventOfCode {
    private fun getInputText(): String = Day22::class.java.getResource("day22.txt")?.readText()!!

    override fun day(): String = "22"

    override fun part1(): Int {
        val instructions = getInputText().split("\n").map { step ->
            val (onOff, coords) = step.split(" ")
            (onOff == "on") to coords.split(",").map { range ->
                val (start, finish) = range.substring(2).split("..").map { it.toInt() }
                val actualStart = if (start < -50) -50 else start
                val actualFinish = if (finish > 50) 50 else finish
                actualStart..actualFinish
            }
        }

        return instructions.fold(setOf<Coord3>()) { acc, instr ->
            val coords = (instr.second[0]).flatMap { x ->
                instr.second[1].flatMap { y ->
                    instr.second[2].map { z ->
                        Coord3(
                            x,
                            y,
                            z
                        )
                    }
                }
            }
            if (instr.first) {
                acc + coords
            } else {
                acc - coords
            }
        }.size
    }

    override fun part2(): Long {
        val instructions = getInputText().split("\n").map { step ->
            val (onOff, coords) = step.split(" ")
            (onOff == "on") to coords.split(",").map { range ->
                val (start, finish) = range.substring(2).split("..").map { it.toInt() }
                val actualStart = if (start < -50) -50 else start
                val actualFinish = if (finish > 50) 50 else finish
                actualStart..actualFinish
//                start..finish
            }
        }

        val onRegions = instructions.fold(setOf<Pair<Coord3, Coord3>>()) { acc, instr ->
            val startCoord = Coord3(
                instr.second[0].first,
                instr.second[1].first,
                instr.second[2].first
            )
            val endCoord = Coord3(
                instr.second[0].last,
                instr.second[1].last,
                instr.second[2].last
            )
            if (instr.first) {
                acc + (startCoord to endCoord)
            } else {
                val newAcc = acc.toMutableSet()
                for(onBox in acc) {
                    newAcc.remove(onBox)
                    newAcc.addAll(
                        boxesFromIntersection(
                            onBox.first,
                            onBox.second,
                            startCoord,
                            endCoord
                        )
                    )
                }


                newAcc
            }

        }

        val ret = onRegions.sumOf {
            (it.second.x - it.first.x) *
                    (it.second.y - it.first.y) *
                    (it.second.z - it.first.z) as Long
        }

        println(ret)

        return ret
    }

    fun Pair<Coord3, Coord3>.contains(coord: Coord3): Boolean =
        coord.x in this.first.x..this.second.x &&
                coord.y in this.first.y..this.second.y &&
                coord.z in this.first.z..this.second.z

    fun boxesFromIntersection(
        onBoxStart: Coord3,
        onBoxEnd: Coord3,
        offBoxStart: Coord3,
        offBoxEnd: Coord3
    ): Set<Pair<Coord3, Coord3>> {
//        setOf(onBoxStart to onBoxEnd).flatMap {
//
//        }

        val startX1 = onBoxStart.x
        val endX1 = offBoxStart.x - 1
        val startX2 = offBoxStart.x
        val endX2 = offBoxEnd.x
        val startX3 = offBoxEnd.x + 1
        val endX3 = onBoxEnd.x

        val startY1 = onBoxStart.y
        val endY1 = offBoxStart.y - 1
        val startY2 = offBoxStart.y
        val endY2 = offBoxEnd.y
        val startY3 = offBoxEnd.y + 1
        val endY3 = onBoxEnd.y

        val startZ1 = onBoxStart.z
        val endZ1 = offBoxStart.z - 1
        val startZ2 = offBoxStart.z
        val endZ2 = offBoxEnd.z
        val startZ3 = offBoxEnd.z + 1
        val endZ3 = onBoxEnd.z

        return setOf(
            // first slice
            // top row
            Coord3(startX1, startY1, startZ1) to Coord3(endX1, endY1, endZ1),
            Coord3(startX2, startY1, startZ1) to Coord3(endX2, endY1, endZ1),
            Coord3(startX3, startY1, startZ1) to Coord3(endX3, endY1, endZ1),

            // middle row
            Coord3(startX1, startY2, startZ1) to Coord3(endX1, endY2, endZ1),
            Coord3(startX2, startY2, startZ1) to Coord3(endX2, endY2, endZ1),
            Coord3(startX3, startY2, startZ1) to Coord3(endX3, endY2, endZ1),

            // bottom row
            Coord3(startX1, startY3, startZ1) to Coord3(endX1, endY3, endZ1),
            Coord3(startX2, startY3, startZ1) to Coord3(endX2, endY3, endZ1),
            Coord3(startX3, startY3, startZ1) to Coord3(endX3, endY3, endZ1),

            // second slice
            // top row
            Coord3(startX1, startY1, startZ2) to Coord3(endX1, endY1, endZ2),
            Coord3(startX2, startY1, startZ2) to Coord3(endX2, endY1, endZ2),
            Coord3(startX3, startY1, startZ2) to Coord3(endX3, endY1, endZ2),

            // middle row
            Coord3(startX1, startY2, startZ2) to Coord3(endX1, endY2, endZ2),
            Coord3(startX3, startY2, startZ2) to Coord3(endX3, endY2, endZ2),

            // bottom row
            Coord3(startX1, startY3, startZ2) to Coord3(endX1, endY3, endZ2),
            Coord3(startX2, startY3, startZ2) to Coord3(endX2, endY3, endZ2),
            Coord3(startX3, startY3, startZ2) to Coord3(endX3, endY3, endZ2),


            // third slice
            // top row
            Coord3(startX1, startY1, startZ3) to Coord3(endX1, endY1, endZ3),
            Coord3(startX2, startY1, startZ3) to Coord3(endX2, endY1, endZ3),
            Coord3(startX3, startY1, startZ3) to Coord3(endX3, endY1, endZ3),

            // middle row
            Coord3(startX1, startY2, startZ3) to Coord3(endX1, endY2, endZ3),
            Coord3(startX2, startY2, startZ3) to Coord3(endX2, endY2, endZ3),
            Coord3(startX3, startY2, startZ3) to Coord3(endX3, endY2, endZ3),

            // bottom row
            Coord3(startX1, startY3, startZ3) to Coord3(endX1, endY3, endZ3),
            Coord3(startX2, startY3, startZ3) to Coord3(endX2, endY3, endZ3),
            Coord3(startX3, startY3, startZ3) to Coord3(endX3, endY3, endZ3),
        )
    }

    fun boxesFromIntersection(
        onBoxStart: Coord,
        onBoxEnd: Coord,
        offBoxStart: Coord,
        offBoxEnd: Coord
    ): Set<Pair<Coord, Coord>> {
        val startX1 = onBoxStart.x
        val endX1 = offBoxStart.x-1
        val startX2 = offBoxStart.x
        val endX2 = offBoxEnd.x
        val startX3 = offBoxEnd.x+1
        val endX3 = onBoxEnd.x

        val startY1 = onBoxStart.y
        val endY1 = offBoxStart.y-1
        val startY2 = offBoxStart.y
        val endY2 = offBoxEnd.y
        val startY3 = offBoxEnd.y+1
        val endY3 = onBoxEnd.y

        val ret = mutableListOf<Pair<Coord, Coord>>()

        val xRanges = getRanges(onBoxStart.x, onBoxEnd.x, offBoxStart.x, offBoxEnd.x)
        val yRanges = getRanges(onBoxStart.y, onBoxEnd.y, offBoxStart.y, offBoxEnd.y)

//        return xRanges.flatMap { xRange ->
//            yRanges.map { yRange ->
//                Coord(xRange.first, yRange.first) to Coord(xRange.second, yRange.second)
//            }
//        }.toSet()

            // top row
            if(offBoxStart.x > onBoxStart.x) {
                ret.add(Coord(startX1, startY1) to Coord(endX1, endY1))
            }
            ret.add(Coord(startX2,startY1) to Coord(endX2,endY1))
            if(offBoxEnd.x < onBoxEnd.x) {
                ret.add(Coord(startX3, startY1) to Coord(endX3, endY1))
            }

            // middle row
            if(offBoxStart.x > onBoxStart.x) {
                ret.add(Coord(startX1, startY2) to Coord(endX1, endY2))
            }
            if(offBoxEnd.x < onBoxEnd.x) {
                ret.add(Coord(startX3, startY2) to Coord(endX3,endY2))
            }

            // bottom row
            if(offBoxStart.y < onBoxEnd.y) {
                if(offBoxStart.x > onBoxStart.x) {
                    ret.add(Coord(startX1, startY3) to Coord(endX1, endY3))
                }
                ret.add(Coord(startX2, startY3) to Coord(endX2, endY3))
                if(offBoxEnd.x < onBoxEnd.x) {
                    ret.add(Coord(startX3, startY3) to Coord(endX3, endY3))
                }
            }

//        ).filter { (start,end) ->
//            start.x >= onBoxStart.x && start.y >= onBoxStart.y && end.y <= onBoxEnd.y && end.x <= onBoxEnd.x &&
//                    start.x <= end.x && start.y <= end.y
//        }.toSet()

        return ret.toSet()
    }

    fun getRanges(
        onBoxStart: Int,
        onBoxEnd: Int,
        offBoxStart: Int,
        offBoxEnd: Int
    ) =
        when {
            (offBoxStart < onBoxStart && offBoxEnd < onBoxEnd) -> listOf(offBoxEnd + 1 to onBoxEnd)
            (offBoxStart < onBoxStart && offBoxEnd > onBoxEnd) -> listOf(onBoxStart to onBoxEnd)
            (offBoxStart > onBoxStart && offBoxEnd < onBoxEnd) -> listOf(
                onBoxStart to offBoxStart - 1,
                offBoxEnd + 1 to onBoxEnd
            )
            (offBoxStart > onBoxStart && offBoxEnd >= onBoxEnd) -> listOf(onBoxStart to offBoxStart - 1)
            else -> listOf()
        }
}

fun main() {
    Day22().part2()
////    val onBoxes = Day22().boxesFromIntersection(
////        Coord3(0,0, 0),
////        Coord3(5,5, 5),
////        Coord3(2,2, 2),
//////        Coord3(3,3, 3)
////        Coord3(5,5,5)
////    )
////
////    val on = onBoxes.flatMap { range ->
////        (range.first.x..range.second.x).flatMap { x-> (range.first.y..range.second.y).flatMap { y -> (range.first.z..range.second.z).map { z -> Coord3(x,y,z) } } }
////    }
////
////    println(onBoxes)
////
////    (0..5).forEach { z->
////        println()
////        (0..5).forEach{y ->
////            println()
////            (0..5).forEach{x ->
////                print(if(Coord3(x,y,z) in on) "#" else ".")
////            }
////        }
////    }
//
//
//    val onBoxes = Day22().boxesFromIntersection(
//        Coord(0, 0),
//        Coord(5, 5),
//        Coord(-2, 5),
////        Coord3(3,3, 3)
//        Coord(6, 5)
//    )
//
//    val count = onBoxes.sumOf {
//        (abs(it.second.x - it.first.x) + 1) * (abs(it.second.y - it.first.y) + 1)
//    }
//    println(count)
//
//    val on = onBoxes.flatMap { range ->
//        (range.first.x..range.second.x).flatMap { x -> (range.first.y..range.second.y).map { y -> Coord(x, y) } }
//    }
//
//    println(onBoxes)
//
//    (-2..7).forEach { y ->
//        println()
//        (-2..7).forEach { x ->
//            print(if (Coord(x, y) in on) "#" else ".")
//        }
//    }
}
