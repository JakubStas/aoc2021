package day1

import utils.ResourceLoader

fun main() {
    println("Day 1 - Part 1")

    val exampleInput = getExampleInput()
    val realInput = getRealInput()

    println("Number of increases in example input: ${getNumberOfIncreases(exampleInput)}")
    println("Number of increases in real input: ${getNumberOfIncreases(realInput)}")
}

private fun getNumberOfIncreases(input: List<Int>): Int {
    var previous = input.first()

    return input.fold(0) { increases, reading ->
        increases + when {
            reading > previous -> 1
            else -> 0
        }.also { previous = reading }
    }
}

private fun getExampleInput() = listOf(199, 200, 208, 210, 200, 207, 240, 269, 260, 263)

private fun getRealInput() = ResourceLoader().readInputAsLines("day1/part1/input.txt").map { it.toInt() }
