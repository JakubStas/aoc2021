package day12

import utils.ResourceLoader

fun main() {
    println("Day 12 - Part 2")

    println("There are ${getNumberOfPathsDfs(getSmallExampleInput())} paths through the cave system in the small example input")
    println("There are ${getNumberOfPathsDfs(getMediumExampleInput())} paths through the cave system in the medium example input")
    println("There are ${getNumberOfPathsDfs(getLargeExampleInput())} paths through the cave system in the large example input")
    println("There are ${getNumberOfPathsDfs(getRealInput())} paths through the cave system in the real input")
}

private fun getNumberOfPathsDfs(input: List<String>) = advanceCaveExploration("start", mutableMapOf(), mapOutTheCaveSystem(input), 0)

private fun advanceCaveExploration(caveName: String, smallCaves: MutableMap<String, Int>, caveMap: Map<String, Cave>, pathsFound: Int, path: String = ""): Int {
    val cave = caveMap[caveName]!!

    val currentlyInSmallCave = caveName.isSmallCaveName() && caveName != "start" && caveName != "end"

    if (currentlyInSmallCave) {
        smallCaves[cave.name] = if (smallCaves.containsKey(cave.name)) {
            smallCaves[cave.name]!! + 1
        } else {
            1
        }
    }

    val branchedPathCount = if (cave.name == "end") {
        pathsFound + 1
    } else {
        cave.tunnels.asSequence().sorted()
            .map { caveMap[it] }.filterNot { it!!.name == "start" }.filterNotNull().sumOf { tunnel ->
            val headingToSmallCave = tunnel.name.isSmallCaveName()
            val headingToAlreadyVisitedCave = smallCaves.contains(tunnel.name) && smallCaves[tunnel.name]!! > 0

            val headingToClosedCave = tunnel.tunnels.size == 1
            val notHeadingToEnd = tunnel.name != "end"

            val everBeenInSmallCaveTwice = smallCaves.any { it.value > 1 }

            when {
                everBeenInSmallCaveTwice && headingToSmallCave && headingToAlreadyVisitedCave -> 0
                everBeenInSmallCaveTwice && currentlyInSmallCave && headingToClosedCave && notHeadingToEnd -> 0
                else -> {
                    val updatedPath = if (path == "") { caveName } else { "$path,$caveName" }
                    advanceCaveExploration(tunnel.name, smallCaves, caveMap, pathsFound, updatedPath)
                }
            }
        }
    }

    if (currentlyInSmallCave) {
        smallCaves[caveName] = smallCaves[caveName]!! - 1
    }

    return branchedPathCount
}

private fun getSmallExampleInput() = ResourceLoader().readInputAsLines("day12/part2/small.txt")
private fun getMediumExampleInput() = ResourceLoader().readInputAsLines("day12/part2/medium.txt")
private fun getLargeExampleInput() = ResourceLoader().readInputAsLines("day12/part2/large.txt")

private fun getRealInput() = ResourceLoader().readInputAsLines("day12/part2/input.txt")
