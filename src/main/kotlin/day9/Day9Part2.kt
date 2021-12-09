package day9

import utils.ResourceLoader

fun main() {
    println("Day 9 - Part 2")

    println("Product of the size of the three largest basins in the example output is ${getProductOfThreeLargestBasins(getExampleInput())}")
    println("Product of the size of the three largest basins in the example output is ${getProductOfThreeLargestBasins(getRealInput())}")
}

private fun getProductOfThreeLargestBasins(input: List<String>) = getBasinSizes(input).sorted().takeLast(3).fold(1) { product, basinSize -> product * basinSize }

private fun getBasinSizes(input: List<String>): List<Int> {
    val heightMap = input.map { line -> line.toCharArray().filter { it.isDigit() }.map { character -> character.digitToInt() } }

    val basins = mutableListOf<Int>()
    val notVisitedYet = heightMap.toListOfCoordinates().toMutableSet()

    val queue = ArrayDeque<Pair<Int, Int>>(9)
    queue.add(0 to 0)

    while (queue.isNotEmpty()) {
        val point = queue.removeFirst()
        notVisitedYet.remove(point)

        if (heightMap.get(point) != 9) {
            if (basins.isEmpty()) {
                basins.add(0)
            }

            basins[basins.lastIndex] += 1

            val notYetVisitedNeighbouringPoints = listOf(
                Pair(point.first, point.second - 1),
                Pair(point.first, point.second + 1),
                Pair(point.first - 1, point.second),
                Pair(point.first + 1, point.second)
            ).intersect(notVisitedYet) - queue

            queue.addAll(notYetVisitedNeighbouringPoints)
        }

        if (queue.isEmpty() && notVisitedYet.isNotEmpty()) {
            val pointInNextBasinToExplore = notVisitedYet.first()

            if (heightMap.get(pointInNextBasinToExplore) != 9) {
                basins.add(0)
            }

            queue.add(pointInNextBasinToExplore)
            notVisitedYet.remove(pointInNextBasinToExplore)
        }
    }

    return basins
}

private fun List<List<Int>>.toListOfCoordinates() = (0 until getRowCount()).map { rowIndex -> (0 until getColumnCount()).map { columnIndex -> Pair(rowIndex, columnIndex) } }.flatten()

private fun List<List<Int>>.get(point: Pair<Int, Int>) = this[point.first][point.second]

private fun getExampleInput() = listOf("2199943210", "3987894921", "9856789892", "8767896789", "9899965678")

private fun getRealInput() = ResourceLoader().readInputAsLines("day9/part2/input.txt")
