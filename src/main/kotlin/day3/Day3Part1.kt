package day3

import utils.ResourceLoader

fun main() {
    println("Day 3 - Part 1")

    val exampleInput = getExampleInput()
    val realInput = getRealInput()

    println("Gamma x epsilon rate multiple in example input: ${getPowerConsumption(exampleInput)}")
    println("Gamma x epsilon rate multiple in real input: ${getPowerConsumption(realInput)}")
}

private fun getPowerConsumption(input: List<String>): Int {
    val windowSize = input.first().length

    val diagnostics = input.flatMap(String::asIterable).foldIndexed(mutableMapOf<Int, Pair<Int, Int>>()) { indexOfCharacter, indexToStats, singleValue ->
        val indexOfReading = indexOfCharacter % windowSize

        indexToStats.computeIfAbsent(indexOfReading) { 0 to 0 }

        indexToStats[indexOfReading] = with(indexToStats[indexOfReading]!!) {
            if (singleValue == '0') {
                first + 1 to second
            } else {
                first to second + 1
            }
        }

        indexToStats
    }.toSortedMap()

    return diagnostics.getRate(gammaRate) * diagnostics.getRate(epsilonRate)
}

private val gammaRate: (Pair<Int, Int>) -> Char = { if (it.first < it.second) { '0' } else { '1' } }

private val epsilonRate: (Pair<Int, Int>) -> Char = { if (it.first > it.second) { '0' } else { '1' } }

fun <T> Map<Int, T>.getRate(ratingFunction: (T) -> Char): Int = Integer.parseInt(values.map { ratingFunction(it) }.toCharArray().concatToString(), 2)

private fun getExampleInput() =
    listOf("00100", "11110", "10110", "10111", "10101", "01111", "00111", "11100", "10000", "11001", "00010", "01010")

private fun getRealInput() = ResourceLoader().readInputAsLines("day3/part1/input.txt")
