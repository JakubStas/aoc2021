package day1

import utils.ResourceLoader

const val windowSize = 3

fun main() {
    println("Day 1 - Part 2")

    val exampleInput = getExampleInput()
    val realInput = getRealInput()

    println("Number of increases in example input: ${getNumberOfIncreases(exampleInput)}")
    println("Number of increases in real input: ${getNumberOfIncreases(realInput)}")
}

private fun getNumberOfIncreases(input: List<Int>): Int {
    var previousWindowSum = input.sumOfWindow(0, windowSize)
    var increases = 0

    (0 until input.size - 2).forEach {
        val currentWindowSum = input.sumOfWindow(it, windowSize)

        if (currentWindowSum > previousWindowSum) {
            increases++
        }

        previousWindowSum = currentWindowSum
    }

    return increases
}

private fun List<Int>.sumOfWindow(from: Int, size: Int) = (0 until size).sumOf { this[from + it] }

private fun getExampleInput() = listOf(199, 200, 208, 210, 200, 207, 240, 269, 260, 263)

private fun getRealInput() = ResourceLoader().readInputAsLines("day1/part2/input.txt").map { it.toInt() }
