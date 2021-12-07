package day7

import utils.ResourceLoader
import kotlin.math.abs

fun main() {
    println("Day 7 - Part 1")

    val exampleInput = getExampleInput()
    val realInput = getRealInput()

    val exampleCost = getCheapestAlignmentCost(exampleInput)
    val realCost = getCheapestAlignmentCost(realInput)

    println("It takes ${exampleCost.second} fuel to align at the position ${exampleCost.first}")
    println("It takes ${realCost.second} fuel to align at the position ${realCost.first}")
}

private fun getCheapestAlignmentCost(input: List<Int>): Pair<Int, Int> {
    val highestNumber = input.maxOrNull() ?: 0

    return List(highestNumber + 1) { it }.fold(-1 to Integer.MAX_VALUE) { cheapestAlignmentSoFar, currentLevel ->
        val currentCost = input.sumOf { abs(currentLevel - it) }

        if (cheapestAlignmentSoFar.second < currentCost) {
            cheapestAlignmentSoFar
        } else {
            currentLevel to currentCost
        }
    }
}

private fun getExampleInput() = "16,1,2,0,4,2,7,1,2,14".split(",").map { it.toInt() }

private fun getRealInput() = ResourceLoader().readInputAsText("day7/part1/input.txt").split(",").map { it.toInt() }
