package day15

import utils.ResourceLoader

fun main() {
    println("Day 15 - Part 2")

    println("The lowest total risk of any path from the top left to the bottom right is ${getScoreOfLeastRiskyPath(getExampleInput())} in the example input.")
    println("The lowest total risk of any path from the top left to the bottom right is ${getScoreOfLeastRiskyPath(getRealInput())} in the real input.")
}

private fun getScoreOfLeastRiskyPath(input: List<String>): Int {
    val vertexGrid = multiplyVertexGrid(buildVertexGrid(input))

    return calculateTotalRiskOfLeastRiskyPath(findLeastRiskyPath(vertexGrid), vertexGrid)
}

private fun multiplyVertexGrid(originalGrid: List<List<Int>>, multiple: Int = 5): List<List<Int>> {
    val newGrid = List(originalGrid.size * multiple) { MutableList(originalGrid[0].size * multiple) { 0 } }

    originalGrid.forEachIndexed { rowIndex, row ->
        row.forEachIndexed { columnIndex, value ->
            (0 until multiple).fold(value) { risk, step ->
                val rightMultiple = Pair(rowIndex, columnIndex + step * originalGrid.size)
                newGrid[rightMultiple.first][rightMultiple.second] = risk

                risk % 9 + 1
            }
        }
    }

    newGrid.take(originalGrid.size).forEachIndexed { rowIndex, row ->
        row.forEachIndexed { columnIndex, value ->
            (0 until multiple).fold(value) { risk, step ->
                val bottomMultiple = Pair(rowIndex + step * originalGrid[0].size, columnIndex)

                newGrid[bottomMultiple.first][bottomMultiple.second] = risk

                risk % 9 + 1
            }
        }
    }

    return newGrid
}

private fun getExampleInput() = """
    1163751742
    1381373672
    2136511328
    3694931569
    7463417111
    1319128137
    1359912421
    3125421639
    1293138521
    2311944581
""".trimIndent().split("\n")

private fun getRealInput() = ResourceLoader().readInputAsLines("day15/part2/input.txt")
