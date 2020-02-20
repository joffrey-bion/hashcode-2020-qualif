package org.hildan.hashcode2020

import kotlin.math.min

const val BOOK_SCORE_IMPORTANCE = 2.0
const val SIGNUP_TIME_IMPORTANCE = 1.0

class Problem(
    val nBooks: Int,
    val nDays: Int,
    val bookScores: IntArray,
    val libraries: List<Library>
) {
    private val sortedBooksByLib: MutableMap<Int, List<Int>> = mutableMapOf()

    private var Library.booksSortedByDescendingScore
        get() = sortedBooksByLib.getOrPut(id) {
            books.sortedByDescending { bookScores[it] }
        }
        set(value) { sortedBooksByLib[id] = value }

    private var remainingLibs = libraries
    private val scannedBooks: MutableSet<Int> = HashSet()

    fun solve(): Iterable<String> {

        val scannedBooksByLib: MutableMap<Int, List<Int>> = mutableMapOf()

        var remainingDays = nDays
        while (remainingDays > 0) {
            val l = takeNextBestLib(remainingDays) ?: break
            remainingDays -= l.signUpDays

            if (remainingDays <= 0) continue // useless library

            val bookIds = l.booksSortedByDescendingScore
                .asSequence()
                .filterNot { it in scannedBooks }
                .take(min(l.books.size.toLong(), remainingDays.toLong() * l.booksPerDay).toInt())
                .toList()

            if (bookIds.isEmpty()) continue

            scannedBooksByLib[l.id] = bookIds
            scannedBooks.addAll(bookIds)
        }

        return listOf(scannedBooksByLib.size.toString()) +
                scannedBooksByLib.flatMap { (id, books) -> solutionBlock(id, books) }
    }

    private fun takeNextBestLib(remainingDays: Int): Library? {
        val libs = remainingLibs.sortedByDescending { it.computeScore(remainingDays) }.limitSignupable(remainingDays)
        if (libs.isEmpty()) return null
        remainingLibs = libs.subList(1, libs.size)
        return libs[0]
    }

    private fun List<Library>.limitSignupable(
        remainingDays: Int
    ): MutableList<Library> {
        val usableSortedLibraries = mutableListOf<Library>()
        var totalSignUpDays = 0
        for (l in this) {
            totalSignUpDays += l.signUpDays
            if (totalSignUpDays < remainingDays) {
                usableSortedLibraries.add(l)
            }
        }
        return usableSortedLibraries
    }

    private fun Library.computeScore(remainingDays: Int): Double {
        val totalBookScoreCap = totalBookScoreCap(remainingDays)
        val totalBookScore = totalBookScoreCap * BOOK_SCORE_IMPORTANCE
        val signUpPenalty = signUpDays * SIGNUP_TIME_IMPORTANCE
        return totalBookScore / signUpPenalty
    }

    private fun Library.totalBookScoreCap(remainingDays: Int): Int {
        val scanningDays = remainingDays - signUpDays
        if (scanningDays <= 0) {
            return 0
        }
        val maxScannedBooks = min(books.size.toLong(), booksPerDay * scanningDays.toLong()).toInt()
        return booksSortedByDescendingScore.asSequence()
            .filterNot { it in scannedBooks }
            .take(maxScannedBooks)
            .sumBy { bookScores[it] }
    }

    private fun solutionBlock(libId: Int, books: List<Int>): List<String> {
        return listOf("$libId ${books.size}", books.joinToString(" "))
    }
}

class Library(
    val id: Int,
    val signUpDays: Int,
    val booksPerDay: Int,
    val books: Set<Int>
)
