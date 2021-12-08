package day8

import utils.ResourceLoader

fun main() {
    println("Day 8 - Part 1")

    println("Digits 1, 4, 7, or 8 appear in the example output ${getUniqueNumberDigitCount(getSmallExampleInput())}")
    println("Digits 1, 4, 7, or 8 appear in the example output ${getUniqueNumberDigitCount(getBiggerExampleInput())}")
    println("Digits 1, 4, 7, or 8 appear in the real output ${getUniqueNumberDigitCount(getRealInput())}")
}

private fun getUniqueNumberDigitCount(input: List<String>) =
    getDigitsInOutput(input).count { it.length in listOf(2, 3, 4, 7) }

private fun getDigitsInOutput(input: List<String>) =
    input.map { line -> line.split("|")[1].trim().split(" ") }.flatten()

private fun getSmallExampleInput() = listOf("acedgfb cdfbe gcdfa fbcad dab cefabd cdfgeb eafb cagedb ab | cdfeb fcadb cdfeb cdbaf")

private fun getBiggerExampleInput() = ResourceLoader().readInputAsLines("day8/part1/example.txt")

private fun getRealInput() = ResourceLoader().readInputAsLines("day8/part1/input.txt")
