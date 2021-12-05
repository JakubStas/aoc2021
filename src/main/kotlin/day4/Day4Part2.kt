package day4

import utils.ResourceLoader

fun main() {
    println("Day 4 - Part 2")

    val exampleDrawInput = getExampleDrawInput()
    val exampleBingoCardInput = getExampleBingoCardsInput()

    val realInput = getRealInput()
    val realDrawInput = realInput[0].toListOfInts(",")
    val realBingoCardInput = realInput.subList(2, realInput.lastIndex).joinToString(prefix = "\n", separator = "\n")

    println("The score of the winning bingo card in example input: ${findWinningCard(exampleDrawInput, exampleBingoCardInput).getScore()}")
    println("The score of the winning bingo card in real input: ${findWinningCard(realDrawInput, realBingoCardInput).getScore()}")
}

private fun findWinningCard(drawInput: List<Int>, bingoCardInput: String): BingoCard {
    val cardDeck = parseIntoBingoCards(bingoCardInput)

    drawInput.forEach { number ->
        cardDeck.forEach { card ->
            val numberPosition = card.checkCardForNumber(number)

            if (numberPosition != null) {
                card.markDownPosition(numberPosition.first, numberPosition.second)
            }
        }

        val (winningCards, otherCards) = cardDeck.partition { it.isWinningCard() }

        if (otherCards.isNotEmpty()) {
            cardDeck.removeAll(winningCards)
        } else {
            return winningCards.first()
        }
    }

    throw RuntimeException("Solution not found!")
}

private fun parseIntoBingoCards(input: String) = input.lines().fold(listOf<BingoCard>()) { cardDeck, line ->
    if (line == "") {
        cardDeck + listOf(BingoCard(listOf()))
    } else {
        val cardLine = line.toListOfInts("\\s+")
        val cardToUpdate = cardDeck.last()

        val updatedCardGrid = cardToUpdate.cardGrid + listOf(cardLine)

        cardDeck.dropLast(1) + listOf(BingoCard(updatedCardGrid))
    }
}.toMutableList()

private fun String.toListOfInts(separator: String) = this.split(separator.toRegex()).filter { it != "" }.map { it.toInt() }

private fun getExampleDrawInput() =
    listOf(7, 4, 9, 5, 11, 17, 23, 2, 0, 14, 21, 24, 10, 16, 13, 6, 15, 25, 12, 22, 18, 20, 8, 19, 3, 26, 1)

private fun getExampleBingoCardsInput() = """
    
    22 13 17 11  0
     8  2 23  4 24
    21  9 14 16  7
     6 10  3 18  5
     1 12 20 15 19
    
     3 15  0  2 22
     9 18 13 17  5
    19  8  7 25 23
    20 11 10 24  4
    14 21 16 12  6
    
    14 21 17 24  4
    10 16 15  9 19
    18  8 23 26 20
    22 11 13  6  5
     2  0 12  3  7
""".trimIndent()

private fun getRealInput() = ResourceLoader().readInputAsLines("day4/part1/input.txt")
