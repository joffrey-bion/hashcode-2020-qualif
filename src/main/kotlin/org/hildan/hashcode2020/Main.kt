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
    val nBooks = readInt()
    val nLibraries = readInt()
    val nDays = readInt()
    val bookScores = IntArray(nBooks) { readInt() }
    val libraries = List(nLibraries) { id -> readLibrary(id) }.filter { it.signUpDays < nDays }
    return Problem(
        nBooks = nBooks,
        nDays = nDays,
        bookScores = bookScores,
        libraries = libraries
    )
}

fun HCReader.readLibrary(id: Int): Library {
    val nBooks = readInt()
    val signUpDays = readInt()
    val booksPerDay = readInt()
    val books = List(nBooks) { readInt() }.toSet()
    return Library(id, signUpDays, booksPerDay, books)
}
