package day10

import utils.ResourceLoader
import java.util.*

fun main() {
    println("Day 10 - Part 1")

    println("The total syntax error score for errors in the example input is ${getTotalSyntaxErrorScore(getExampleInput())}")
    println("The total syntax error score for errors in the real input is ${getTotalSyntaxErrorScore(getRealInput())}")
}

private val scoreMap = mapOf(
    ')' to 3,
    ']' to 57,
    '}' to 1197,
    '>' to 25137,
)

val chunkMarkers = mapOf(
    '(' to ')',
    '[' to ']',
    '{' to '}',
    '<' to '>'
)

private fun getTotalSyntaxErrorScore(input: List<String>) =
    input.mapNotNull { getIncorrectClosingCharacter(it) }.sumOf { scoreMap[it]!! }

private fun getIncorrectClosingCharacter(line: String): Char? {
    val stack = Stack<Char>()

    line.toCharArray().forEach { char ->
        if (stack.isNotEmpty()) {
            val isAClosingChar = chunkMarkers.values.contains(char)

            if (!isAClosingChar) {
                stack.push(char)
            } else if (char == chunkMarkers[stack.peek()]) {
                stack.pop()
            } else {
                // println("$line - Expected ${chunkMarkers[stack.peek()]}, but found $char instead.")
                return char
            }
        } else {
            stack.push(char)
        }
    }

    return null
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

private fun getRealInput() = ResourceLoader().readInputAsLines("day10/part1/input.txt")
