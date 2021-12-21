package com.mills.advent.twentyone

import com.mills.advent.support.AdventOfCode
import java.lang.Math.pow

typealias Packet = Pair<Int, Int?>

class Day16 : AdventOfCode {
    private fun getInputText(): String = Day16::class.java.getResource("day16.txt")?.readText()!!

    private val hexMapping = mapOf(
        '0' to "0000",
        '1' to "0001",
        '2' to "0010",
        '3' to "0011",
        '4' to "0100",
        '5' to "0101",
        '6' to "0110",
        '7' to "0111",
        '8' to "1000",
        '9' to "1001",
        'A' to "1010",
        'B' to "1011",
        'C' to "1100",
        'D' to "1101",
        'E' to "1110",
        'F' to "1111"
    )

    override fun day(): String = "16"

    override fun part1(): Int {
        val binaryRep = "220D4B80491FE6FBDCDA61F23F1D9B763004A7C128012F9DA88CE27B000B30F4804D49CD515380352100763DC5E8EC000844338B10B667A1E60094B7BE8D600ACE774DF39DD364979F67A9AC0D1802B2A41401354F6BF1DC0627B15EC5CCC01694F5BABFC00964E93C95CF080263F0046741A740A76B704300824926693274BE7CC880267D00464852484A5F74520005D65A1EAD2334A700BA4EA41256E4BBBD8DC0999FC3A97286C20164B4FF14A93FD2947494E683E752E49B2737DF7C4080181973496509A5B9A8D37B7C300434016920D9EAEF16AEC0A4AB7DF5B1C01C933B9AAF19E1818027A00A80021F1FA0E43400043E174638572B984B066401D3E802735A4A9ECE371789685AB3E0E800725333EFFBB4B8D131A9F39ED413A1720058F339EE32052D48EC4E5EC3A6006CC2B4BE6FF3F40017A0E4D522226009CA676A7600980021F1921446700042A23C368B713CC015E007324A38DF30BB30533D001200F3E7AC33A00A4F73149558E7B98A4AACC402660803D1EA1045C1006E2CC668EC200F4568A5104802B7D004A53819327531FE607E118803B260F371D02CAEA3486050004EE3006A1E463858600F46D8531E08010987B1BE251002013445345C600B4F67617400D14F61867B39AA38018F8C05E430163C6004980126005B801CC0417080106005000CB4002D7A801AA0062007BC0019608018A004A002B880057CEF5604016827238DFDCC8048B9AF135802400087C32893120401C8D90463E280513D62991EE5CA543A6B75892CB639D503004F00353100662FC498AA00084C6485B1D25044C0139975D004A5EB5E52AC7233294006867F9EE6BA2115E47D7867458401424E354B36CDAFCAB34CBC2008BF2F2BA5CC646E57D4C62E41279E7F37961ACC015B005A5EFF884CBDFF10F9BFF438C014A007D67AE0529DED3901D9CD50B5C0108B13BAFD6070".map {
            hexMapping[it]
        }.joinToString("").toList()
        return parsePacketPart1(ArrayDeque(binaryRep)).sumOf { it.first }
    }

    override fun part2(): Long {
        val binaryRep = "220D4B80491FE6FBDCDA61F23F1D9B763004A7C128012F9DA88CE27B000B30F4804D49CD515380352100763DC5E8EC000844338B10B667A1E60094B7BE8D600ACE774DF39DD364979F67A9AC0D1802B2A41401354F6BF1DC0627B15EC5CCC01694F5BABFC00964E93C95CF080263F0046741A740A76B704300824926693274BE7CC880267D00464852484A5F74520005D65A1EAD2334A700BA4EA41256E4BBBD8DC0999FC3A97286C20164B4FF14A93FD2947494E683E752E49B2737DF7C4080181973496509A5B9A8D37B7C300434016920D9EAEF16AEC0A4AB7DF5B1C01C933B9AAF19E1818027A00A80021F1FA0E43400043E174638572B984B066401D3E802735A4A9ECE371789685AB3E0E800725333EFFBB4B8D131A9F39ED413A1720058F339EE32052D48EC4E5EC3A6006CC2B4BE6FF3F40017A0E4D522226009CA676A7600980021F1921446700042A23C368B713CC015E007324A38DF30BB30533D001200F3E7AC33A00A4F73149558E7B98A4AACC402660803D1EA1045C1006E2CC668EC200F4568A5104802B7D004A53819327531FE607E118803B260F371D02CAEA3486050004EE3006A1E463858600F46D8531E08010987B1BE251002013445345C600B4F67617400D14F61867B39AA38018F8C05E430163C6004980126005B801CC0417080106005000CB4002D7A801AA0062007BC0019608018A004A002B880057CEF5604016827238DFDCC8048B9AF135802400087C32893120401C8D90463E280513D62991EE5CA543A6B75892CB639D503004F00353100662FC498AA00084C6485B1D25044C0139975D004A5EB5E52AC7233294006867F9EE6BA2115E47D7867458401424E354B36CDAFCAB34CBC2008BF2F2BA5CC646E57D4C62E41279E7F37961ACC015B005A5EFF884CBDFF10F9BFF438C014A007D67AE0529DED3901D9CD50B5C0108B13BAFD6070".map {
            hexMapping[it]
        }.joinToString("").toList()
        return parsePacketPart2(ArrayDeque(binaryRep), "")
    }

    fun parsePacketPart1(binaryRep: ArrayDeque<Char>): List<Packet> {
        val version = (1..3).joinToString("") { binaryRep.removeFirst().toString() }.binaryToInt()
        val packetType = (4..6).joinToString("") { binaryRep.removeFirst().toString() }.binaryToInt()

        if (packetType == 4L) {
            var value = ""
            while (true) {
                val indicator = binaryRep.removeFirst()
                value += (1..4).joinToString("") { binaryRep.removeFirst().toString() }
                if (indicator == '0') break
            }
            return listOf(Packet(version.toInt(), value.binaryToInt().toInt()))
        }

        val indicator = binaryRep.removeFirst()

        if (indicator == '0') {
            // total length of subpackets
            val totalLength = (1..15).joinToString("") { binaryRep.removeFirst().toString() }.binaryToInt()

            val subPackets = ArrayDeque((1..totalLength).map { binaryRep.removeFirst() })
            val retPackets = mutableListOf(Packet(version.toInt(), null))
            while (subPackets.isNotEmpty()) {
                retPackets.addAll(parsePacketPart1(subPackets))
            }
            return retPackets
        } else {
            // number of sub-packets
            val numberPackets = (1..11).joinToString("") { binaryRep.removeFirst().toString() }.binaryToInt()

            return listOf(Packet(version.toInt(), null)) + (1..numberPackets).map { parsePacketPart1(binaryRep) }.flatten()
        }
    }

    fun parsePacketPart2(binaryRep: ArrayDeque<Char>, indent: String): Long {
        val version = (1..3).joinToString("") { binaryRep.removeFirst().toString() }.binaryToInt()
        val packetType = (4..6).joinToString("") { binaryRep.removeFirst().toString() }.binaryToInt()

        if (packetType == 4L) {
            var value = ""
            while (true) {
                val indicator = binaryRep.removeFirst()
                value += (1..4).joinToString("") { binaryRep.removeFirst().toString() }
                if (indicator == '0') break
            }
            println("$indent PacketType: $packetType, answer: ${value.binaryToInt()}")
            return value.binaryToInt()
        }

        val indicator = binaryRep.removeFirst()

        val subPacketValues = if (indicator == '0') {
            // total length of subpackets
            val totalLength = (1..15).joinToString("") { binaryRep.removeFirst().toString() }.binaryToInt()

            val subPackets = ArrayDeque((1..totalLength).map { binaryRep.removeFirst() })
            val retPackets = mutableListOf<Long>()
            while (subPackets.isNotEmpty()) {
                retPackets.add(parsePacketPart2(subPackets, indent + "\t"))
            }
            retPackets
        } else {
            // number of sub-packets
            val numberPackets = (1..11).joinToString("") { binaryRep.removeFirst().toString() }.binaryToInt()

            (1..numberPackets).map { parsePacketPart2(binaryRep, indent + "\t") }
        }

        val retVal = when (packetType.toInt()) {
            0 -> subPacketValues.sum()
            1 -> subPacketValues.fold(1L) { acc, i -> acc * i }
            2 -> subPacketValues.minOrNull()!!
            3 -> subPacketValues.maxOrNull()!!
            5 -> if (subPacketValues[0]> subPacketValues[1]) 1 else 0
            6 -> if (subPacketValues[0] <subPacketValues[1]) 1 else 0
            7 -> if (subPacketValues[0] == subPacketValues[1]) 1 else 0
            else -> 0
        }

        println("$indent PacketType: $packetType, values: $subPacketValues, answer: $retVal")

        return retVal
    }

    private fun CharSequence.binaryToInt(): Long = this.reversed().mapIndexed { idx, b ->
        if (b == '1') pow(2.toDouble(), idx.toDouble()) else 0.toDouble()
    }.sumOf { it }.toLong()
}

fun main() {
    Day16().run()
}
