package day14

import utils.ResourceLoader

fun main() {
    println("Day 14 - Part 1")

    println("You get ${analyzePolymer(getExampleInput())} if you take the quantity of the most common element and subtract the quantity of the least common element")
    println("You get ${analyzePolymer(getRealInput())} if you take the quantity of the most common element and subtract the quantity of the least common element")
}

private fun analyzePolymer(input: List<String>): Int {
    val polymer = apply10StepsToPolymerTemplate(input)

    val characterOccurrences = polymer.toCharArray().groupBy { it }.map { it.key to it.value.size }.toMap()

    return characterOccurrences.entries.maxOf { it.value } - characterOccurrences.entries.minOf { it.value }
}

private fun apply10StepsToPolymerTemplate(input: List<String>): String {
    val (polymerTemplate, pairInsertionRules) = parseInput(input)

    return (0 until 10).fold(polymerTemplate) { polymer, _ ->
        val resultingPolymer = StringBuilder("")

        polymer.windowed(2, 1).forEachIndexed { index, twoChars ->
            val rule = pairInsertionRules.find { rule -> rule.first == twoChars }
            if (resultingPolymer.isNotEmpty()) {
                resultingPolymer.deleteCharAt(resultingPolymer.lastIndex)
            }

            resultingPolymer.append(
                if (rule != null) {
                    twoChars[0] + rule.second + twoChars[1]
                } else {
                    twoChars
                }
            )
        }

        resultingPolymer.toString()
    }
}

private fun parseInput(input: List<String>): Pair<String, List<Pair<String, String>>> {
    val polymerTemplate = input.takeWhile { it != "" }.first()
    val pairInsertionRules = input.takeLastWhile { it != "" }.map { Pair(it.split(" -> ")[0], it.split(" -> ")[1]) }

    return Pair(polymerTemplate, pairInsertionRules)
}

private fun getExampleInput() = """
    NNCB

    CH -> B
    HH -> N
    CB -> H
    NH -> C
    HB -> C
    HC -> B
    HN -> C
    NN -> C
    BH -> H
    NC -> B
    NB -> B
    BN -> B
    BB -> N
    BC -> B
    CC -> N
    CN -> C
""".trimIndent().split("\n")

private fun getRealInput() = ResourceLoader().readInputAsLines("day14/part1/input.txt")
