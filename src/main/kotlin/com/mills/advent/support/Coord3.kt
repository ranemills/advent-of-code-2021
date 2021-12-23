package com.mills.advent.support

data class Coord3(
    val x: Long,
    val y: Long,
    val z: Long
) {
    constructor(x: Int, y: Int, z: Int) : this(x.toLong(), y.toLong(), z.toLong())

    infix operator fun minus(other: Coord3) = Coord3(this.x - other.x, this.y - other.y, this.z - other.z)
    infix operator fun plus(other: Coord3) = Coord3(this.x + other.x, this.y + other.y, this.z + other.z)
}