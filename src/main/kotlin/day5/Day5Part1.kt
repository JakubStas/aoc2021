package day5

import utils.ResourceLoader
import kotlin.math.abs

typealias Point = Pair<Int, Int>
typealias VentLine = Pair<Point, Point>

fun main() {
    println("Day 5 - Part 1")

    val exampleInput = getExampleInput()
    val realInput = getRealInput()

    println("Number of dangerous areas in the example input: ${getNumberOfDangerousAreas(exampleInput)}")
    println("Number of dangerous areas in the real input: ${getNumberOfDangerousAreas(realInput)}")
}

private fun getNumberOfDangerousAreas(input: String): Int {
    val coordinates = parseCoordinates(input)
    val grid = buildGridFromDimensions(coordinates)

    markVentsOnTheGrid(coordinates, grid)

//    grid.map { it.joinToString(separator = " ") }.forEach { println(it) }

    return grid.flatten().count { it > 1 }
}

private fun markVentsOnTheGrid(coordinates: List<VentLine>, grid: List<MutableList<Int>>) = coordinates.forEach {
    val fromX = it.first.first
    val fromY = it.first.second
    val toX = it.second.first
    val toY = it.second.second

    val xIncrement = when {
        fromX > toX -> -1
        fromX < toX -> 1
        else -> 0
    }

    val yIncrement = when {
        fromY > toY -> -1
        fromY < toY -> 1
        else -> 0
    }

    val lineLength = when {
        fromX == toX -> abs(fromY - toY)
        fromY == toY -> abs(fromX - toX)
        else -> 0
    }

    if (lineLength > 0) {
        (0..lineLength).fold(fromX to fromY) { point, _ ->
            grid[point.second][point.first] += 1
            (point.first + xIncrement) to (point.second + yIncrement)
        }
    }
}

private fun buildGridFromDimensions(coordinates: List<VentLine>): List<MutableList<Int>> {
    val gridX = 1 + (coordinates.flatMap { listOf(it.first.first, it.second.first) }.maxOrNull() ?: 0)
    val gridY = 1 + (coordinates.flatMap { listOf(it.first.second, it.second.second) }.maxOrNull() ?: 0)

    return List(gridY) { MutableList(gridX) { 0 } }
}

private fun parseCoordinates(input: String) = input.split("\n").map { line ->
    val rawPoints = line.split(" -> ")

    val rawFromPoint = rawPoints[0].split(',')
    val rawToPoint = rawPoints[1].split(',')

    (rawFromPoint[0].toInt() to rawFromPoint[1].toInt()) to (rawToPoint[0].toInt() to rawToPoint[1].toInt())
}

private fun getExampleInput() = """
    0,9 -> 5,9
    8,0 -> 0,8
    9,4 -> 3,4
    2,2 -> 2,1
    7,0 -> 7,4
    6,4 -> 2,0
    0,9 -> 2,9
    3,4 -> 1,4
    0,0 -> 8,8
    5,5 -> 8,2
""".trimIndent()

private fun getRealInput() = ResourceLoader().readInputAsText("day5/part1/input.txt")
