package day15

import utils.FibonacciHeap
import utils.ResourceLoader

typealias Vertex = Pair<Int, Int>

fun main() {
    println("Day 15 - Part 1")

    println("The lowest total risk of any path from the top left to the bottom right is ${getScoreOfLeastRiskyPath(getExampleInput())} in the example input.")
    println("The lowest total risk of any path from the top left to the bottom right is ${getScoreOfLeastRiskyPath(getRealInput())} in the real input.")
}

private fun getScoreOfLeastRiskyPath(input: List<String>): Int {
    val vertexGrid = buildVertexGrid(input)

    return calculateTotalRiskOfLeastRiskyPath(findLeastRiskyPath(vertexGrid), vertexGrid)
}

fun calculateTotalRiskOfLeastRiskyPath(previousVertexReferences: Map<Vertex, Vertex?>, vertexGrid: List<List<Int>>, startingPoint: Vertex = Vertex(0, 0)): Int {
    val sequence = mutableListOf<Vertex>()
    var target: Vertex? = Vertex(vertexGrid.size - 1, vertexGrid[0].size - 1)

    if (previousVertexReferences[target] != null || target == startingPoint) {
        while (target != null) {
            sequence.add(0, target)
            target = previousVertexReferences[target]
        }
    }

    return sequence.drop(1).sumOf { vertexGrid[it.first][it.second] }
}

fun findLeastRiskyPath(vertexGrid: List<List<Int>>, startingPoint: Vertex = Vertex(0, 0)): Map<Vertex, Vertex?> {
    val riskScoreFromStart = mutableMapOf<Vertex, Long>()
    val previousVertexReferences = mutableMapOf<Vertex, Vertex?>()
    val vertexToFibonacciNode = mutableMapOf<Vertex, FibonacciHeap.Node>()

    riskScoreFromStart[startingPoint] = 0

    val priorityQueue = FibonacciHeap()
    val priorityQueueAsSet = mutableSetOf<Vertex>()

    vertexGrid.forEachIndexed { rowIndex, row ->
        row.forEachIndexed { columnIndex, _ ->
            val vertex = Vertex(rowIndex, columnIndex)

            if (vertex != startingPoint) {
                riskScoreFromStart[vertex] = Long.MAX_VALUE
                previousVertexReferences[vertex] = null
            }

            val fibonacciNode = priorityQueue.insert(vertex, riskScoreFromStart[vertex]!!.toDouble())
            vertexToFibonacciNode[vertex] = fibonacciNode
            priorityQueueAsSet.add(vertex)
        }
    }

    while (!priorityQueue.isEmpty) {
        val min = priorityQueue.removeMin()!! as Vertex
        priorityQueueAsSet.remove(min)

        min.getNeighbours().filter { it in priorityQueueAsSet }.forEach { vertex ->
            val alt = riskScoreFromStart[min]!! + vertexGrid[vertex.first][vertex.second]

            if (alt < riskScoreFromStart[vertex]!!) {
                riskScoreFromStart[vertex] = alt
                previousVertexReferences[vertex] = min

                priorityQueue.decreaseKey(vertexToFibonacciNode[vertex], alt.toDouble())
            }
        }
    }

    return previousVertexReferences
}

fun Vertex.getNeighbours() = listOf(
    first - 1 to second,
    first + 1 to second,
    first to second - 1,
    first to second + 1
)

fun buildVertexGrid(input: List<String>) =
    input.map { line -> line.toCharArray().map { char -> char.digitToInt() } }

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

private fun getRealInput() = ResourceLoader().readInputAsLines("day15/part1/input.txt")
