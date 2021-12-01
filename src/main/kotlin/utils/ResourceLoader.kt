package utils

class ResourceLoader {

    fun readInputAsText(path: String) = this::class.java.classLoader.getResource(path).readText()

    fun readInputAsLines(path: String) = readInputAsText(path).split("\n")
}