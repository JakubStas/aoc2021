package day13

import utils.ResourceLoader

fun main() {
    println("Day 13 - Part 1")

    println("There are ${getNumberOfDots(getExampleInput())} dots visible after completing just the first fold in the example input")
    println("There are ${getNumberOfDots(getRealInput())} dots visible after completing just the first fold in the example input")
}

private fun getNumberOfDots(input: List<String>): Int {
    val (paperGrid, foldingInstructions) = parseInput(input)

    val result = foldPaper(paperGrid, foldingInstructions[0])

    return result.flatten().count { it == '#' }
}

fun foldPaper(paperGrid: List<MutableList<Char>>, foldingInstruction: Pair<Char, Int>): List<MutableList<Char>> {
    return when (foldingInstruction.first) {
        'x' -> foldPaperAlongX(paperGrid, foldingInstruction.second) // column
        'y' -> foldPaperAlongY(paperGrid, foldingInstruction.second) // row
        else -> throw RuntimeException("Folding instruction parsing error!")
    }
}

private fun foldPaperAlongX(paperGrid: List<MutableList<Char>>, pivotColumn: Int): List<MutableList<Char>> {
    var leftColumnToProcess = pivotColumn - 1
    var rightRowToProcess = pivotColumn + 1

    while (leftColumnToProcess >= 0 && rightRowToProcess < paperGrid.getColumnCount()) {
        paperGrid.forEachIndexed { rowIndex, row ->
            paperGrid[rowIndex][leftColumnToProcess] = if (row[leftColumnToProcess] == '#' || paperGrid[rowIndex][rightRowToProcess] == '#') {
                '#'
            } else {
                '.'
            }
        }

        leftColumnToProcess--
        rightRowToProcess++
    }

    return paperGrid.map { it.take(pivotColumn).toMutableList() }
}

private fun foldPaperAlongY(paperGrid: List<MutableList<Char>>, pivotRow: Int): List<MutableList<Char>> {
    var topRowToProcess = pivotRow - 1
    var bottomRowToProcess = pivotRow + 1

    while (topRowToProcess >= 0 && bottomRowToProcess < paperGrid.getRowCount()) {
        paperGrid[topRowToProcess].forEachIndexed { columnIndex, aboveChar ->
            paperGrid[topRowToProcess][columnIndex] = if (aboveChar == '#' || paperGrid[bottomRowToProcess][columnIndex] == '#') {
                '#'
            } else {
                '.'
            }
        }

        topRowToProcess--
        bottomRowToProcess++
    }

    return paperGrid.take(pivotRow)
}

fun parseInput(input: List<String>): Pair<List<MutableList<Char>>, List<Pair<Char, Int>>> {
    val points = input.takeWhile { it != "" }
    val instructions = input.takeLastWhile { it != "" }

    val listOfCoordinates = points.map { Pair(it.split(',')[0].toInt(), it.split(',')[1].toInt()) }

    val columnCount = listOfCoordinates.maxOf { it.first } + 1
    val rowCount = listOfCoordinates.maxOf { it.second } + 1

    val paperGrid = List(rowCount) { MutableList(columnCount) { '.' } }

    listOfCoordinates.forEach { point ->
        paperGrid[point.second][point.first] = '#'
    }

    val foldingInstructions = instructions.map { it.substringAfterLast(' ') }
        .map { Pair(it.split('=')[0].toCharArray()[0], it.split('=')[1].toInt()) }

    return Pair(paperGrid, foldingInstructions)
}

private fun List<MutableList<Char>>.getRowCount() = this.size

private fun List<MutableList<Char>>.getColumnCount() = this[0].size

fun getExampleInput() = """
    6,10
    0,14
    9,10
    0,3
    10,4
    4,11
    6,0
    6,12
    4,1
    0,13
    10,12
    3,4
    3,0
    8,4
    1,10
    2,14
    8,10
    9,0
    
    fold along y=7
    fold along x=5
""".trimIndent().split("\n")

private fun getRealInput() = ResourceLoader().readInputAsLines("day13/part1/input.txt")
