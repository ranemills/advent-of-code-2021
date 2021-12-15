package support

data class Coord(val x: Int, val y: Int) {
    fun nextX() = Coord(x + 1, y)
    fun nextY() = Coord(x, y + 1)
    fun previousX() = Coord(x - 1, y)
    fun previousY() = Coord(x, y - 1)
}

typealias Grid<T> = List<List<T>>

fun String.toGrid(): Grid<Int> = this.split("\n").map { it.chunked(1).map { it.toInt() }.toMutableList() }
fun <T> Grid<T>.getCoord(x: Int, y: Int): T = this[y][x]
fun <T> Grid<T>.getCoord(coord: Coord): T = this[coord.y][coord.x]
fun Grid<*>.validCoord(coord: Coord) = coord.x in 0..this[0].size && coord.y in 0..this.size