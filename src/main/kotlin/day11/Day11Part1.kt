package day11

import utils.ResourceLoader

fun main() {
    println("Day 11 - Part 1")

    val steps = 100

    println("After $steps steps, there have been a total of ${getNumberOfFlushesInStep(steps, getExampleInput())} in the example input")
    println("After $steps steps, there have been a total of ${getNumberOfFlushesInStep(steps, getRealInput())} in the real input")
}

private fun getNumberOfFlushesInStep(steps: Int, input: List<String>): Int {
    val octopuses = input.map { line -> line.toCharArray().map { character -> character.digitToInt() }.toMutableList() }

    return (1..steps).fold(0) { flashes, _ -> flashes + getNumberOfFlashesInOneStep(octopuses) }
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

fun increaseEnergyOfAllAndGetFlashing(octopuses: List<MutableList<Int>>): MutableList<Pair<Int, Int>> {
    val flashedOctopuses = mutableListOf<Pair<Int, Int>>()

    octopuses.forEachIndexed { rowIndex, row ->
        row.forEachIndexed { columnIndex, energy ->
            val octopus = Pair(rowIndex, columnIndex)

            if (energy == 9) {
                octopuses.resetCharge(octopus)
                flashedOctopuses.add(octopus)
            } else {
                octopuses.addCharge(octopus)
            }
        }
    }

    return flashedOctopuses
}

fun List<List<Int>>.getNeighbours(point: Pair<Int, Int>): List<Pair<Int, Int>> = (-1..1).map { rowAdjustment ->
    (-1..1).map { columnAdjustment ->
        Pair(point.first + rowAdjustment, point.second + columnAdjustment)
    }
}.flatten()
    .filter { it.first >= 0 && it.second >= 0 }
    .filter { it.first < getRowCount() && it.second < getColumnCount() } - point

fun List<List<Int>>.get(point: Pair<Int, Int>) = this[point.first][point.second]

fun List<MutableList<Int>>.resetCharge(point: Pair<Int, Int>) {
    this[point.first][point.second] = 0
}

fun List<MutableList<Int>>.addCharge(point: Pair<Int, Int>) {
    this[point.first][point.second] += 1
}

fun List<List<Int>>.getColumnCount() = first().size

fun List<List<Int>>.getRowCount() = size

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

private fun getRealInput() = ResourceLoader().readInputAsLines("day11/part1/input.txt")
