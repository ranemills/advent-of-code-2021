package com.mills.advent.twentyone

import com.mills.advent.support.AdventOfCode
import com.mills.advent.support.Coord3
import kotlin.math.abs

class Day19 : AdventOfCode {
    private fun getInputText(): String = Day19::class.java.getResource("day19.txt")?.readText()!!

    override fun day(): String = "19"

    fun findBeaconsAndScanners(): Pair<MutableMap<Int, Coord3>, MutableSet<Coord3>> {
        val beaconDetectionsPerScanner = getInputText().split("\n\n").associate {
            val lines = it.split("\n")
            val points = lines.subList(1, lines.size).map {
                val (x, y, z) = it.split(",").map { it.toLong() }
                Coord3(x, y, z)
            }
            Pair(lines[0].replace("-", "").replace("scanner", "").trim().toInt(), points)
        }

        val scannersToFind = beaconDetectionsPerScanner.keys.toMutableList()

        val allBeacons = beaconDetectionsPerScanner[0]!!.toMutableSet()
        val scannerLocations = mutableMapOf(
            0 to Coord3(0, 0, 0)
        )
        scannersToFind.remove(0)

        while (scannersToFind.isNotEmpty()) {
            val scannerKey = scannersToFind.removeAt(0)
            val beacons = beaconDetectionsPerScanner[scannerKey]!!
//            println("Scanner key: $scannerKey")
            transforms.firstNotNullOfOrNull { transform ->
                val transformedBeacons = beacons.map { coord ->
                    transform.fold(coord) { acc, it -> it(acc) }
                }

                allBeacons
                    .flatMap { ab -> transformedBeacons.map { tb -> ab - tb } }
                    .distinct()
                    .firstNotNullOfOrNull { adjustment ->
                        val adjustedBeacons = transformedBeacons.map { it + adjustment }
                        var c = 0
                        for((idx,b) in adjustedBeacons.withIndex()) {
                            if(b in allBeacons) c++
                            if(c >= 12 || idx > adjustedBeacons.size-12+c) break
                        }

                        if (c >= 12) {
                            adjustment to adjustedBeacons
                        } else {
                            null
                        }
                    }
            }?.also {
                val (adjustment, adjustedBeacons) = it
                allBeacons.addAll(adjustedBeacons)
                scannerLocations[scannerKey] = adjustment
            } ?: scannersToFind.add(scannerKey)
        }

        return Pair(scannerLocations, allBeacons)
    }

    override fun part1(): Int {
        val (_, allBeacons) = findBeaconsAndScanners()
        return allBeacons.size
    }

    override fun part2(): Long {
        val (scannerLocations, _) = findBeaconsAndScanners()

        return scannerLocations.values.flatMap { x ->
            scannerLocations.values.map { y ->
                x - y
            }
        }.maxOf {
            abs(it.x) + abs(it.y) + abs(it.z)
        }
    }


    private val transforms by lazy {
        // generate each orientation
        val xRotate: (Coord3) -> Coord3 = { coord: Coord3 -> Coord3(coord.x, coord.z, -coord.y) }
        val yRotate: (Coord3) -> Coord3 = { coord: Coord3 -> Coord3(coord.z, coord.y, -coord.x) }
        val zRotate: (Coord3) -> Coord3 = { coord: Coord3 -> Coord3(coord.y, -coord.x, coord.z) }

        listOf(
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
            listOf(zRotate, yRotate, yRotate),
            listOf(zRotate, yRotate, yRotate, xRotate),
            listOf(zRotate, yRotate, yRotate, xRotate, xRotate),
            listOf(zRotate, yRotate, yRotate, xRotate, xRotate, xRotate),
            listOf(zRotate, zRotate, yRotate, yRotate, xRotate, xRotate),
            listOf(zRotate, zRotate, zRotate, yRotate, yRotate, xRotate, xRotate),
        )
    }

}

fun main() {
    Day19().run()
}
