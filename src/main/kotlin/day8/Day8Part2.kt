package day8

import utils.ResourceLoader

fun main() {
    println("Day 8 - Part 2")

    println("Sum of all outputs in the small example output ${getSumOfAllOutputs(getSmallExampleInput())}")
    println("Sum of all outputs in the big example output ${getSumOfAllOutputs(getBigExampleInput())}")
    println("Sum of all outputs in the big example output ${getSumOfAllOutputs(getRealInput())}")
}

private fun getSumOfAllOutputs(input: List<String>) = input.sumOf { decodeLine(it) }

private fun decodeLine(input: String): Int {
    val segmentsInput = getSegmentsInput(input)
    val digitsInOutput = getDigitsInOutput(input)

    val charToSegmentMap = decodeUnknownSegmentsFromLine(segmentsInput)
    val displayValue = digitsInOutput.map { it.decodeUsing(charToSegmentMap) }.joinToString(separator = "").toInt()

    // println("$digitsInOutput: $displayValue")

    return displayValue
}

private fun String.decodeUsing(decoder: Map<Char, Segment>) = Digit.getDigitBySegments(toCharArray().map { decoder[it]!! }).number

private fun decodeUnknownSegmentsFromLine(segmentsInput: List<String>): Map<Char, Segment> {
    val segmentToCharCandidatesMap = decodeKnownSegmentsFromLine(segmentsInput)

    for (input in segmentsInput) {
        // if the line is decoded, just bubble up the results
        if (segmentToCharCandidatesMap.all { it.value.size == 1 }) {
            return segmentToCharCandidatesMap.map { it.value.first() to it.key }.toMap()
        }

        val knownSegments = segmentToCharCandidatesMap.filter { it.value.size == 1 }
            .map { entry: Map.Entry<Segment, MutableSet<Char>> -> entry.value.iterator().next() to entry.key }
            .toMap()

        // redact the decoded characters from possible candidate lists
        segmentToCharCandidatesMap.filterNot { knownSegments.values.contains(it.key) }
            .forEach { toBeUpdatedSegmentCandList ->
                toBeUpdatedSegmentCandList.value.removeAll(knownSegments.keys)
            }

        // if the line is decoded, just bubble up the results
        if (segmentToCharCandidatesMap.all { it.value.size == 1 }) {
            return segmentToCharCandidatesMap.map { it.value.first() to it.key }.toMap()
        }

        val digitsMatchingSize = Digit.getDigitsBySegmentCount(input.length)

        if (digitsMatchingSize.size == 1) {
            digitsMatchingSize[0].segments.forEach { segment ->
                if (segmentToCharCandidatesMap[segment]!!.size > 1) {
                    segmentToCharCandidatesMap[segment]!!.removeIf { !input.toCharArray().contains(it) }
                }
            }

            Segment.values().filterNot { digitsMatchingSize[0].segments.contains(it) }.forEach {
                segmentToCharCandidatesMap[it]!!.removeAll(input.toCharArray().toSet())
            }
        } else {
            throw RuntimeException("Given the properties of the problem and the fact inputs are sorted by size, this should never happen!")
        }
    }

    return segmentToCharCandidatesMap.map { it.value.first() to it.key }.toMap()
}

private fun decodeKnownSegmentsFromLine(input: List<String>): MutableMap<Segment, MutableSet<Char>> {
    val segmentToCharInputs =
        Segment.values().associateWith { mutableSetOf('a', 'b', 'c', 'd', 'e', 'f', 'g') }.toMutableMap()

    val characterOccurrences = mutableMapOf<Char, Int>()

    input.joinToString(separator = "").toCharArray().forEach {
        if (characterOccurrences.containsKey(it)) {
            characterOccurrences[it] = characterOccurrences[it]!! + 1
        } else {
            characterOccurrences[it] = 1
        }
    }

    // bottom left segment is the only segment that occurs 4 times across the input
    segmentToCharInputs[Segment.BOTTOM_LEFT] =
        mutableSetOf(characterOccurrences.filter { it.value == 4 }.keys.iterator().next())
    // top left segment is the only segment that occurs 6 times across the input
    segmentToCharInputs[Segment.TOP_LEFT] =
        mutableSetOf(characterOccurrences.filter { it.value == 6 }.keys.iterator().next())
    // bottom right segment is the only segment that occurs 9 times across the input
    segmentToCharInputs[Segment.BOTTOM_RIGHT] =
        mutableSetOf(characterOccurrences.filter { it.value == 9 }.keys.iterator().next())

    return segmentToCharInputs
}

private fun getSegmentsInput(input: String) =
    input.split("|")[0].trim().split(" ").map { String(it.toCharArray()) }.sortedBy { it.length }

private fun getDigitsInOutput(input: String) =
    input.split("|")[1].trim().split(" ")

enum class Segment {
    TOP, TOP_LEFT, TOP_RIGHT, MIDDLE, BOTTOM_LEFT, BOTTOM_RIGHT, BOTTOM
}

enum class Digit(val segments: List<Segment>, val number: Int) {
    ZERO(listOf(Segment.TOP, Segment.TOP_LEFT, Segment.BOTTOM_LEFT, Segment.BOTTOM, Segment.BOTTOM_RIGHT, Segment.TOP_RIGHT), 0),
    ONE(listOf(Segment.TOP_RIGHT, Segment.BOTTOM_RIGHT), 1),
    TWO(listOf(Segment.TOP, Segment.TOP_RIGHT, Segment.MIDDLE, Segment.BOTTOM_LEFT, Segment.BOTTOM), 2),
    THREE(listOf(Segment.TOP, Segment.TOP_RIGHT, Segment.MIDDLE, Segment.BOTTOM_RIGHT, Segment.BOTTOM), 3),
    FOUR(listOf(Segment.TOP_LEFT, Segment.MIDDLE, Segment.TOP_RIGHT, Segment.BOTTOM_RIGHT), 4),
    FIVE(listOf(Segment.TOP, Segment.TOP_LEFT, Segment.MIDDLE, Segment.BOTTOM_RIGHT, Segment.BOTTOM), 5),
    SIX(listOf(Segment.TOP_LEFT, Segment.MIDDLE, Segment.BOTTOM_RIGHT, Segment.BOTTOM, Segment.BOTTOM_LEFT, Segment.TOP), 6),
    SEVEN(listOf(Segment.TOP, Segment.TOP_RIGHT, Segment.BOTTOM_RIGHT), 7),
    EIGHT(Segment.values().asList(), 8),
    NINE(listOf(Segment.TOP, Segment.TOP_LEFT, Segment.TOP_RIGHT, Segment.MIDDLE, Segment.BOTTOM, Segment.BOTTOM_RIGHT), 9);

    companion object {

        fun getDigitsBySegmentCount(segmentCount: Int) = values().filter { it.segments.size == segmentCount }

        fun getDigitBySegments(segments: List<Segment>) = values().first { it.segments.size == segments.size && it.segments.containsAll(segments) }
    }
}

private fun getSmallExampleInput() = listOf("acedgfb cdfbe gcdfa fbcad dab cefabd cdfgeb eafb cagedb ab | cdfeb fcadb cdfeb cdbaf")

private fun getBigExampleInput() = ResourceLoader().readInputAsLines("day8/part1/example.txt")

private fun getRealInput() = ResourceLoader().readInputAsLines("day8/part2/input.txt")
