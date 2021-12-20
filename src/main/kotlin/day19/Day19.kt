package day19

import support.AdventOfCode
import kotlin.math.abs

data class Coord(
    val x: Long,
    val y: Long,
    val z: Long
) {
    constructor(x: Int, y: Int, z: Int) : this(x.toLong(), y.toLong(), z.toLong())

    infix operator fun minus(other: Coord) = Coord(this.x-other.x, this.y-other.y, this.z-other.z)
    infix operator fun plus(other: Coord) = Coord(this.x+other.x, this.y+other.y, this.z+other.z)
}

class Day19 : AdventOfCode {
    private fun getInputText(): String = Day19::class.java.getResource("input.txt")?.readText()!!

    override fun day(): String = "19"

    override fun part1(): Int {
        val beaconDetectionsPerScanner = getInputText().split("\n\n").associate {
            val lines = it.split("\n")
            val points = lines.subList(1, lines.size).map {
                val (x, y, z) = it.split(",").map { it.toLong() }
                Coord(x, y, z)
            }
            Pair(lines[0].replace("-", "").replace("scanner", "").trim().toInt(), points)
        }

        val allBeacons = beaconDetectionsPerScanner[0]!!.toMutableSet()
        val fixedBeacons = mutableMapOf(
            0 to beaconDetectionsPerScanner[0]!!
        )
        val scannerLocations = mutableMapOf(
            0 to (Coord(0,0,0) to listOf { coord: Coord -> Coord(coord.x, coord.y, coord.z) })
        )
        while(beaconDetectionsPerScanner.size - scannerLocations.size > 0) {
            for (scanner in beaconDetectionsPerScanner.filter { (k, _) -> !scannerLocations.containsKey(k) }) {
                var success = false

                if (scanner.key == 0) continue

                for (transform in getTransforms()) {
                    val transformedBeacons = scanner.value.map { coord ->
                        transform.fold(coord) { acc, it -> it(acc) }
                    }

                    for (adjustment in allBeacons.flatMap { ab -> transformedBeacons.map { tb -> ab - tb } }) {
                        val adjustedBeacons = transformedBeacons.map { it + adjustment }

                        if (adjustedBeacons.intersect(allBeacons.toSet()).size >= 12) {
                            allBeacons.addAll(adjustedBeacons)
                            fixedBeacons[scanner.key] = adjustedBeacons
                            scannerLocations[scanner.key] = adjustment to transform
                            success = true
                            break
                        }
                    }
                    if (success) break
                }
            }
        }

        scannerLocations.forEach { (k,v) -> println("$k: ${v.first}")}

        val rawScannerLocations = scannerLocations.map { (k,v) -> v.first }

        println(rawScannerLocations.flatMap { x -> rawScannerLocations.map { y -> x - y } }.map { abs(it.x) + abs(it.y) + abs(it.z) }.maxByOrNull { it })

        return allBeacons.size
    }


    override fun part2() {
    }


    fun getTransforms(): List<List<(Coord) -> Coord>> {
        // generate each orientation
        val xRotate: (Coord) -> Coord = { coord: Coord -> Coord(coord.x, coord.z, -coord.y) }
        val yRotate: (Coord) -> Coord = { coord: Coord -> Coord(coord.z, coord.y, -coord.x) }
        val zRotate: (Coord) -> Coord = { coord: Coord -> Coord(coord.y, -coord.x, coord.z) }

        return listOf(
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
