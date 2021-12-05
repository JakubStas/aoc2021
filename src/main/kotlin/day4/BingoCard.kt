package day4

class BingoCard(val cardGrid: List<List<Int>>) {

    private val cardMarker = Pair(mutableListOf(0,0,0,0,0), mutableListOf(0,0,0,0,0))

    private var mostRecentlyAddedNumber = 0

    var sumOfAllUnmarkedNumbers = cardGrid.flatten().sum()

    fun getLikelihoodOfWinning() = (cardMarker.first.maxOrNull() ?: 0) + (cardMarker.second.maxOrNull() ?: 0)

    fun checkCardForNumber(number: Int): Pair<Int, Int>? {
        cardGrid.forEachIndexed { rowIndex, rowNumbers ->
            val columnIndex = rowNumbers.indexOf(number)

            if (columnIndex != -1) {
                return rowIndex to columnIndex
            }
        }

        return null
    }

    fun markDownPosition(rowIndex: Int, columnIndex: Int) {
        cardMarker.first[rowIndex] += 1
        cardMarker.second[columnIndex] += 1

        mostRecentlyAddedNumber = cardGrid[rowIndex][columnIndex]
        sumOfAllUnmarkedNumbers -= mostRecentlyAddedNumber
    }

    fun isWinningCard(): Boolean {
        val isWinningRow = cardMarker.first.indexOf(5) != -1
        val isWinningColumn = cardMarker.second.indexOf(5) != -1

        return isWinningRow || isWinningColumn
    }

    fun getScore() = sumOfAllUnmarkedNumbers * mostRecentlyAddedNumber
}
