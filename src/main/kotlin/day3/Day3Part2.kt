package day3

import utils.ResourceLoader

fun main() {
    println("Day 3 - Part 2")

    val exampleInput = getExampleInput()
    val realInput = getRealInput()

    println("Life support rating in example input: ${getLifeSupportRating(exampleInput)}")
    println("Life support rating in real input: ${getLifeSupportRating(realInput)}")
}

private fun getLifeSupportRating(input: List<String>) = getOxygenGeneratorRating(input) * getCo2ScrubberRating(input)

private fun getCo2ScrubberRating(input: List<String>) = getRating(input, leastCommonValue)

private fun getOxygenGeneratorRating(input: List<String>) = getRating(input, mostCommonValue)

private fun getRating(input: List<String>, ratingFunction: (List<String>, Int) -> Char): Int {
    val queue = ArrayDeque(input)
    var columnIndex = 0

    while (queue.size != 1) {
        val rating = ratingFunction(queue, columnIndex)
        queue.removeIf { it[columnIndex] != rating }
        columnIndex++
    }

    return queue.first().binaryToDecimal()
}

private val mostCommonValue: (List<String>, Int) -> Char = { input, columnIndex ->
    with(getColumnsValueStats(input, columnIndex)) {
        if (first > second) {
            '0'
        } else {
            '1'
        }
    }
}

private val leastCommonValue: (List<String>, Int) -> Char = { input, columnIndex ->
    with(getColumnsValueStats(input, columnIndex)) {
        if (first <= second) {
            '0'
        } else {
            '1'
        }
    }
}

private fun getColumnsValueStats(input: List<String>, columnIndex: Int) =
    input.map { it[columnIndex] }.fold(0 to 0) { acc, columnValue ->
        if (columnValue == '0') {
            acc.first + 1 to acc.second
        } else {
            acc.first to acc.second + 1
        }
    }

private fun String.binaryToDecimal() = Integer.parseInt(this, 2)

private fun getExampleInput() = listOf("00100", "11110", "10110", "10111", "10101", "01111", "00111", "11100", "10000", "11001", "00010", "01010")

private fun getRealInput() = ResourceLoader().readInputAsLines("day3/part2/input.txt")
