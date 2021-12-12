package day11

import utils.ResourceLoader

fun main() {
    println("Day 11 - Part 2")

    println("The first step during which all octopuses flash in the example input: ${getFullySynchronizedStep(getExampleInput())}")
    println("The first step during which all octopuses flash in the real input: ${getFullySynchronizedStep(getRealInput())}")
}

private fun getFullySynchronizedStep(input: List<String>): Int {
    val octopuses = input.map { line -> line.toCharArray().map { character -> character.digitToInt() }.toMutableList() }
    var step = 1

    while (getNumberOfFlashesInOneStep(octopuses) != octopuses.getColumnCount() * octopuses.getRowCount()) {
        step++
    }

    return step
}

private fun getNumberOfFlashesInOneStep(octopuses: List<MutableList<Int>>): Int {
    val flashedOctopuses = increaseEnergyOfAllAndGetFlashing(octopuses)

    while (flashedOctopuses.isNotEmpty()) {
        val impactedByFlashing = flashedOctopuses.map { flashedOctopus ->
            octopuses.getNeighbours(flashedOctopus)
        }.flatten().filter { octopuses.get(it) != 0 }

        flashedOctopuses.clear()

        impactedByFlashing.forEach { impactedOctopus ->
            if (octopuses.get(impactedOctopus) == 9) {
                octopuses.resetCharge(impactedOctopus)
                flashedOctopuses.add(impactedOctopus)
            } else if (octopuses.get(impactedOctopus) == 0) {
                // skip as it was impacted by more than a single flash
            } else {
                octopuses.addCharge(impactedOctopus)
            }
        }
    }

    return octopuses.sumOf { row -> row.count { it == 0 } }
}

private fun getExampleInput() = """
    5483143223
    2745854711
    5264556173
    6141336146
    6357385478
    4167524645
    2176841721
    6882881134
    4846848554
    5283751526
""".trimIndent().split("\n")

private fun getRealInput() = ResourceLoader().readInputAsLines("day11/part2/input.txt")
