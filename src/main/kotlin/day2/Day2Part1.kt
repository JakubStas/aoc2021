package day2

import utils.ResourceLoader

fun main() {
    println("Day 2 - Part 1")

    val exampleInput = getExampleInput()
    val realInput = getRealInput()

    println("Horizontal x Depth multiple in example input: ${getPositionMultiple(exampleInput)}")
    println("Horizontal x Depth multiple in real input: ${getPositionMultiple(realInput)}")
}

private fun getPositionMultiple(input: List<String>): Int {
    var horizontal = 0
    var depth = 0

    input.forEach {
        val instruction = it.split(" ")[0]
        val value = it.split(" ")[1].toInt()

        when (instruction) {
            "forward" -> horizontal += value
            "up" -> depth -= value
            "down" -> depth += value
        }
    }

    return horizontal * depth
}

private fun getExampleInput() = listOf("forward 5", "down 5", "forward 8", "up 3", "down 8", "forward 2")

private fun getRealInput() = ResourceLoader().readInputAsLines("day2/part1/input.txt")
