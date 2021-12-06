package day6

import utils.ResourceLoader

fun main() {
    println("Day 6 - Part 1")

    val exampleInput = getExampleInput()
    val realInput = getRealInput()

    val day = 80

    println("Population of lanternfish would reach ${getPopulationSizeOnDay(exampleInput, day)} on day $day with example input.")
    println("Population of lanternfish would reach ${getPopulationSizeOnDay(realInput, day)} on day $day with real input.")
}

fun getPopulationSizeOnDay(input: List<Int>, day: Int): Long {
    val school = input.groupBy { it }.map { it.key to it.value.size.toLong() }.sortedBy { it.first }.reversed().toMap()

    val schoolOnTheLastDay = (0 until day).fold(school) { school, day ->
        // logSchoolStats(school, day)

        school.flatMap { (timer, count) ->
            if (timer == 0) {
                listOf(8 to count, 6 to count)
            } else {
                listOf(timer - 1 to count)
            }
        }.groupingBy {
            it.first
        }.aggregate { _: Int, fishCount: Long?, timerToCount: Pair<Int, Long>, _: Boolean ->
            timerToCount.second + (fishCount ?: 0)
        }
    }

    return schoolOnTheLastDay.values.sum()
}

private fun logSchoolStats(school: Map<Int, Long>, day: Int) = println("Day $day: $school [${school.values.sum()}]")

private fun getExampleInput() = "3,4,3,1,2".split(",").map { it.toInt()}

private fun getRealInput() = ResourceLoader().readInputAsText("day6/part1/input.txt").split(",").map { it.toInt()}
