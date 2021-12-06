package day6

import utils.ResourceLoader

fun main() {
    println("Day 6 - Part 2")

    val exampleInput = getExampleInput()
    val realInput = getRealInput()

    val day = 256

    println("Population of lanternfish would reach ${getPopulationSizeOnDay(exampleInput, day)} on day $day with example input.")
    println("Population of lanternfish would reach ${getPopulationSizeOnDay(realInput, day)} on day $day with real input.")
}

private fun getExampleInput() = "3,4,3,1,2".split(",").map { it.toInt()}

private fun getRealInput() = ResourceLoader().readInputAsText("day6/part2/input.txt").split(",").map { it.toInt()}
