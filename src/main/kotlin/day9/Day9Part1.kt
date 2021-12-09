package day9

import utils.ResourceLoader

fun main() {
    println("Day 9 - Part 1")

    println("The product of the size of the three largest basins in the example input is ${getSumOfTheRiskLevels(getExampleInput())}")
    println("The product of the size of the three largest basins in the real input is ${getSumOfTheRiskLevels(getRealInput())}")
}

private fun getSumOfTheRiskLevels(input: List<String>): Int {
    val heightMap = input.map { line -> line.toCharArray().map { character -> character.digitToInt() } }

    val lowPoints = heightMap.foldIndexed(listOf<Int>()) { rowIndex, lowPoints, row ->
        row.foldIndexed(lowPoints) { columnIndex, lowPoints, height ->
            if (heightMap.isTheLowestInTheArea(rowIndex, columnIndex)) {
                lowPoints + listOf(height)
            }

            lowPoints
        }
    }

    return lowPoints.sumOf { it + 1 }
}

private fun List<List<Int>>.isTheLowestInTheArea(rowIndex: Int, columnIndex: Int): Boolean {
    val isLowerThanLeft = if (columnIndex > 0) {
        this[rowIndex][columnIndex] < this[rowIndex][columnIndex - 1]
    } else {
        true
    }

    val isLowerThanRight = if (columnIndex < getColumnCount() - 1) {
        this[rowIndex][columnIndex] < this[rowIndex][columnIndex + 1]
    } else {
        true
    }

    val isLowerThanTop = if (rowIndex > 0) {
        this[rowIndex][columnIndex] < this[rowIndex - 1][columnIndex]
    } else {
        true
    }

    val isLowerThanBottom = if (rowIndex < getRowCount() - 1) {
        this[rowIndex][columnIndex] < this[rowIndex + 1][columnIndex]
    } else {
        true
    }

    return isLowerThanLeft && isLowerThanTop && isLowerThanRight && isLowerThanBottom
}

fun List<List<Int>>.getColumnCount() = first().size

fun List<List<Int>>.getRowCount() = size

private fun getExampleInput() = listOf("2199943210", "3987894921", "9856789892", "8767896789", "9899965678")

private fun getRealInput() = ResourceLoader().readInputAsLines("day9/part1/input.txt")
