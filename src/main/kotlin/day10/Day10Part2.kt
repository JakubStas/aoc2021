package day10

import utils.ResourceLoader
import java.util.*

fun main() {
    println("Day 10 - Part 2")

    println(
        "The middle autocomplete score for lines missing characters in the example input is ${
            getAutocompleteMiddleScore(
                getExampleInput()
            )
        }"
    )
    println(
        "The middle autocomplete score for lines missing characters in the real input is ${
            getAutocompleteMiddleScore(
                getRealInput()
            )
        }"
    )
}

private val scoreMap = mapOf(
    ')' to 1,
    ']' to 2,
    '}' to 3,
    '>' to 4,
)

private fun getAutocompleteMiddleScore(input: List<String>): Long {
    val scores = input.filter { !hasIncorrectClosingCharacter(it) }
        .map { getMissingClosingChars(it) }
        .map { getAutoCompleteScore(it) }
        .sorted()

    return scores[scores.size / 2]
}

private fun getAutoCompleteScore(completion: List<Char>) = completion.fold(0L) { product, character ->
    (product * 5 + scoreMap[character]!!)
}

private fun getMissingClosingChars(line: String): List<Char> {
    val stack = Stack<Char>()

    line.toCharArray().forEach { char ->
        if (stack.isNotEmpty()) {
            val isAClosingChar = chunkMarkers.values.contains(char)

            if (!isAClosingChar) {
                stack.push(char)
            } else {
                stack.pop()
            }
        } else {
            stack.push(char)
        }
    }

    return stack.map { chunkMarkers[it]!! }.reversed()
}

private fun hasIncorrectClosingCharacter(line: String): Boolean {
    val stack = Stack<Char>()

    line.toCharArray().forEach { char ->
        if (stack.isNotEmpty()) {
            val isAClosingChar = chunkMarkers.values.contains(char)

            if (!isAClosingChar) {
                stack.push(char)
            } else if (char == chunkMarkers[stack.peek()]) {
                stack.pop()
            } else {
                return true
            }
        } else {
            stack.push(char)
        }
    }

    return false
}

private fun getExampleInput() = """
    [({(<(())[]>[[{[]{<()<>>
    [(()[<>])]({[<{<<[]>>(
    {([(<{}[<>[]}>{[]{[(<()>
    (((({<>}<{<{<>}{[]{[]{}
    [[<[([]))<([[{}[[()]]]
    [{[{({}]{}}([{[{{{}}([]
    {<[[]]>}<{[{[{[]{()[[[]
    [<(<(<(<{}))><([]([]()
    <{([([[(<>()){}]>(<<{{
    <{([{{}}[<[[[<>{}]]]>[]]
""".trimIndent().split("\n")

private fun getRealInput() = ResourceLoader().readInputAsLines("day10/part2/input.txt")
