package day14

import utils.ResourceLoader

typealias PairInsertionRules = Map<Pair<Char, Char>, Char>

typealias Polymer = Map<Pair<Char, Char>, Long>

fun main() {
    println("Day 14 - Part 2")

    val steps = 40

    println("You get ${analyzePolymer(getExampleInput(), steps)} if you take the quantity of the most common element and subtract the quantity of the least common element")
    println("You get ${analyzePolymer(getRealInput(), steps)} if you take the quantity of the most common element and subtract the quantity of the least common element")
}

fun analyzePolymer(input: List<String>, steps: Int): Long {
    val (polymerTemplate, pairInsertionRules) = parse(input)

    val polymer = (1..steps).fold(polymerTemplate) { polymerIteration, _ ->
        advancePolymerByOneStep(polymerIteration, pairInsertionRules)
    }

    val characterOccurrences = getCharacterOccurrences(polymer)

    return characterOccurrences.values.maxOrNull()!! - characterOccurrences.values.minOrNull()!!
}

fun advancePolymerByOneStep(polymer: Polymer, pairInsertionRules: PairInsertionRules): Polymer {
    val nextIterationOfPolymer = mutableMapOf<Pair<Char, Char>, Long>()

    for ((key, count) in polymer) {
        val rule = pairInsertionRules[key]

        if (rule != null) {
            val (firstChar, secondChar) = key

            nextIterationOfPolymer[Pair(firstChar, rule)] =
                nextIterationOfPolymer.getOrDefault(Pair(firstChar, rule), 0L) + count
            nextIterationOfPolymer[Pair(rule, secondChar)] =
                nextIterationOfPolymer.getOrDefault(Pair(rule, secondChar), 0L) + count
        } else {
            nextIterationOfPolymer[key] = count
        }
    }

    return nextIterationOfPolymer
}

fun getCharacterOccurrences(polymer: Polymer): Map<Char, Long> {
    val characterOccurrences = mutableMapOf<Char, Long>()

    for ((key, count) in polymer) {
        characterOccurrences[key.first] = characterOccurrences.getOrDefault(key.first, 0L) + count
    }

    return characterOccurrences
}

fun parse(input: List<String>): Pair<Polymer, MutableMap<Pair<Char, Char>, Char>> {
    val polymerTemplate = input[0]

    val polymer = mutableMapOf<Pair<Char, Char>, Long>()

    polymerTemplate.windowed(2, 1, true).forEach { twoChars ->
        val secondChar = if (twoChars.length == 1) {
            '_'
        } else {
            twoChars[1]
        }
        val key = Pair(twoChars[0], secondChar)
        polymer[key] = polymer.getOrDefault(key, 0L) + 1L
    }

    val pairInsertionRules = mutableMapOf<Pair<Char, Char>, Char>()
    val regex = "(.+)\\s*->\\s*(.+)".toRegex()

    for (line in input.drop(2)) {
        val match = regex.find(line)
        if (match != null) {
            val twoChars = match.groupValues[1].trim()
            pairInsertionRules[Pair(twoChars[0], twoChars[1])] = match.groupValues[2].trim()[0]
        }
    }

    return polymer to pairInsertionRules
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

private fun getRealInput() = ResourceLoader().readInputAsLines("day14/part2/input.txt")
