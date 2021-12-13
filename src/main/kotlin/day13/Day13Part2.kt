package day13

import utils.ResourceLoader

fun main() {
    println("Day 13 - Part 2")

    println("\nAfter all the folding, the example input paper looks like this:\n")
    getNumberOfDots(getExampleInput())

    println("\nAfter all the folding, the real input paper looks like this:\n")
    getNumberOfDots(getRealInput())
}

private fun getNumberOfDots(input: List<String>): Int {
    val (paperGrid, foldingInstructions) = parseInput(input)

    val result = foldPaperAllTheWay(paperGrid, foldingInstructions)

    result.forEach { row ->
        println(row.joinToString(separator = " "))
    }

    return result.flatten().count { it == '#' }
}

private fun foldPaperAllTheWay(paperGrid: List<MutableList<Char>>, foldingInstructions: List<Pair<Char, Int>>): List<MutableList<Char>> {
    return foldingInstructions.fold(paperGrid) { grid, foldingInstruction ->
        foldPaper(grid, foldingInstruction)
    }
}

private fun getRealInput() = ResourceLoader().readInputAsLines("day13/part2/input.txt")
