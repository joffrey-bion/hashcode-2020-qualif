package org.hildan.hashcode2020

import kotlinx.coroutines.runBlocking
import org.hildan.hashcode.utils.reader.HCReader
import org.hildan.hashcode.utils.solveHCFilesInParallel

fun main(files: Array<String>) = runBlocking {
    solveHCFilesInParallel(*files) {
        val problem = readProblem()
        problem.solve()
    }
}

fun HCReader.readProblem(): Problem {
    val nbBooks = readInt()
    val nbLibraries = readInt()
    val scanningTime = readInt()
    val bookToScore = List(nbBooks) { readInt() }
    val libs = List(nbLibraries) { index ->  readLibrary(index) }.toMutableList()
    return Problem(scanningTime, bookToScore, libs)
}

data class Library(
    val index: Int,
    val nbBooks: Int,
    val signupTime: Int,
    val nbCanShip: Int,
    val books: List<Int>
)

fun HCReader.readLibrary(index: Int): Library {
    val nbBooks = readInt()
    val signupLegnth = readInt()
    val nbCanShip = readInt()
    val books = List(nbBooks) { readInt() }
    return Library(index, nbBooks, signupLegnth, nbCanShip, books)
}

