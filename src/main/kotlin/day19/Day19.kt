package day19

import support.AdventOfCode

data class Coord(
    val x: Long,
    val y: Long,
    val z: Long
) {
    constructor(x: Int, y: Int, z:Int) : this(x.toLong(), y.toLong(), z.toLong())
}

class Day19 : AdventOfCode {
    private fun getInputText(): String = Day19::class.java.getResource("input.txt")?.readText()!!

    override fun day(): String = "19"

    override fun part1() {
        val beaconDetectionsPerScanner = getInputText().split("\n\n").associate {
            val lines = it.split("\n")
            val points = lines.subList(1, lines.lastIndex).map {
                val (x,y,z) = it.split(",").map { it.toLong() }
                Coord(x,y,z)
            }
            Pair(lines[0].replace("-", "").trim(), points)
        }

        val relativeBeaconDetectionsPerScanner = beaconDetectionsPerScanner.mapValues { (k,v) ->
            v.flatMap {
                    left -> v.map { right -> Coord(left.x-right.x, left.y-right.y, left.z-right.z) }
            }.filter {
                it != Coord(0,0,0)
            }
        }

        // euclidean distances
//        val relativeBeaconDetectionsPerScanner = beaconDetectionsPerScanner.mapValues { (k,v) ->
//            v.flatMap {
//                    left -> v.map { right ->
//                Pair(left, sqrt(
//                    (left.x - right.x).toDouble().pow(2) +
//                    (left.y-right.y).toDouble().pow(2) +
//                    (left.z-right.z).toDouble().pow(2)
//                ))
//                    }
//            }.filter {
//                it.second != 0.toDouble()
//            }
//        }

        // generate each orientation
        val xRotate: (Coord)-> Coord = { coord: Coord -> Coord(coord.x, coord.z, -coord.y)}
        val yRotate: (Coord)-> Coord = { coord: Coord -> Coord(coord.z, coord.y, -coord.x)}
        val zRotate: (Coord)-> Coord = { coord: Coord -> Coord(coord.y, -coord.x, coord.z)}

        val transforms = listOf(
            listOf(xRotate),
            listOf(xRotate, xRotate),
            listOf(xRotate, xRotate, xRotate),
            listOf(yRotate),
            listOf(yRotate, xRotate),
            listOf(yRotate, xRotate, xRotate),
            listOf(yRotate, xRotate, xRotate, xRotate),
            listOf(yRotate, yRotate),
            listOf(yRotate, yRotate, xRotate),
            listOf(yRotate, yRotate, xRotate, xRotate),
            listOf(yRotate, yRotate, xRotate, xRotate, xRotate),
            listOf(yRotate, yRotate, yRotate),
            listOf(yRotate, yRotate, yRotate, xRotate),
            listOf(yRotate, yRotate, yRotate, xRotate, xRotate),
            listOf(yRotate, yRotate, yRotate, xRotate, xRotate, xRotate),
            listOf(zRotate, xRotate),
            listOf(zRotate, xRotate, xRotate),
            listOf(zRotate, xRotate, xRotate, xRotate),
//            listOf(zRotate, yRotate),
//            listOf(zRotate, yRotate, xRotate),
//            listOf(zRotate, yRotate, xRotate, xRotate),
//            listOf(zRotate, yRotate, xRotate, xRotate, xRotate),
            listOf(zRotate, yRotate, yRotate),
            listOf(zRotate, yRotate, yRotate, xRotate),
            listOf(zRotate, yRotate, yRotate, xRotate, xRotate),
            listOf(zRotate, yRotate, yRotate, xRotate, xRotate, xRotate),
//            listOf(zRotate, yRotate, yRotate, yRotate),
//            listOf(zRotate, yRotate, yRotate, yRotate, xRotate),
//            listOf(zRotate, yRotate, yRotate, yRotate, xRotate, xRotate),
//            listOf(zRotate, yRotate, yRotate, yRotate, xRotate, xRotate, xRotate),
//            listOf(zRotate, zRotate, xRotate),
//            listOf(zRotate, zRotate, xRotate, xRotate),
//            listOf(zRotate, zRotate, xRotate, xRotate, xRotate),
//            listOf(zRotate, zRotate, yRotate),
//            listOf(zRotate, zRotate, yRotate, xRotate),
//            listOf(zRotate, zRotate, yRotate, xRotate, xRotate),
//            listOf(zRotate, zRotate, yRotate, xRotate, xRotate, xRotate),
//            listOf(zRotate, zRotate, yRotate, yRotate),
//            listOf(zRotate, zRotate, yRotate, yRotate, xRotate),
            listOf(zRotate, zRotate, yRotate, yRotate, xRotate, xRotate),
//            listOf(zRotate, zRotate, yRotate, yRotate, xRotate, xRotate, xRotate),
//            listOf(zRotate, zRotate, yRotate, yRotate, yRotate),
//            listOf(zRotate, zRotate, yRotate, yRotate, yRotate, xRotate),
//            listOf(zRotate, zRotate, yRotate, yRotate, yRotate, xRotate, xRotate),
//            listOf(zRotate, zRotate, yRotate, yRotate, yRotate, xRotate, xRotate, xRotate),
//            listOf(zRotate, zRotate, zRotate, xRotate),
//            listOf(zRotate, zRotate, zRotate, xRotate, xRotate),
//            listOf(zRotate, zRotate, zRotate, xRotate, xRotate, xRotate),
//            listOf(zRotate, zRotate, zRotate, yRotate),
//            listOf(zRotate, zRotate, zRotate, yRotate, xRotate),
//            listOf(zRotate, zRotate, zRotate, yRotate, xRotate, xRotate),
//            listOf(zRotate, zRotate, zRotate, yRotate, xRotate, xRotate, xRotate),
//            listOf(zRotate, zRotate, zRotate, yRotate, yRotate),
//            listOf(zRotate, zRotate, zRotate, yRotate, yRotate, xRotate),
            listOf(zRotate, zRotate, zRotate, yRotate, yRotate, xRotate, xRotate),
//            listOf(zRotate, zRotate, zRotate, yRotate, yRotate, xRotate, xRotate, xRotate),
//            listOf(zRotate, zRotate, zRotate, yRotate, yRotate, yRotate),
//            listOf(zRotate, zRotate, zRotate, yRotate, yRotate, yRotate, xRotate),
//            listOf(zRotate, zRotate, zRotate, yRotate, yRotate, yRotate, xRotate, xRotate),
//            listOf(zRotate, zRotate, zRotate, yRotate, yRotate, yRotate, xRotate, xRotate, xRotate),
        )

        val initial = Coord(1,2,3)
        transforms.withIndex().map { (idx, transformList) ->
            println(idx.toString() + transformList.fold(initial) { acc, it -> it(acc) })
            transformList.fold(initial) { acc, it -> it(acc) }
        }.distinct().also {
            println(it.size)
            println(it)
        }


        val leftScanner = relativeBeaconDetectionsPerScanner["scanner 0"]!!.toMutableList()
        val rightScanner = relativeBeaconDetectionsPerScanner["scanner 1"]!!.toMutableList()
        val rightScannerSize = rightScanner.size

        val matchingBeacons = mutableListOf<Coord>()
        leftScanner.forEach { v ->
            rightScanner.remove(v)
        }

        val overlaps = rightScannerSize-rightScanner.size
        // 78 is 12th triangle number: 12+11+10...
        if(overlaps > 78) {
            println()
        }

        println(rightScannerSize-rightScanner.size)

    }

    override fun part2() {
    }
}

fun main() {
    Day19().run()
}
