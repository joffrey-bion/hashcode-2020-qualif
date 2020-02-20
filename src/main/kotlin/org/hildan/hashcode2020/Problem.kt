package org.hildan.hashcode2020

data class OutputLibrary(val index: Int, val books: List<Int>)

class Problem(
    val scanningTime: Int,
    val bookToScore: List<Int>,
    var libs: MutableList<Library>,
    val selectedBooks: MutableSet<Int> = hashSetOf()
) {
    fun solve(): Iterable<String> {
        val output: MutableList<String> = arrayListOf()
        val filteredLib = createOutput()
            .filter { it.books.isNotEmpty() }

        output.add(filteredLib.size.toString())
        filteredLib.forEach {
            output.add(it.index.toString() + " " + it.books.size)
            output.add(it.books.joinToString(" "))
        }
        return output
    }

    fun createOutput(): MutableList<OutputLibrary> {
        val outputLibraries: MutableList<OutputLibrary> = arrayListOf()
        for (currentTime in 0..scanningTime) {
            var toto = 0
            libs = libs
                .sortedBy {
                    it.books
                        .filter { !selectedBooks.contains(it) }
                        .take(scanningTime - currentTime - it.signupTime)
                        .sumBy { b -> bookToScore[b] }
                }.takeWhile {
                    toto += it.signupTime;
                    toto <= scanningTime
                }.toMutableList()
            if (libs.isEmpty()) {
                return outputLibraries
            }
            val library = libs.removeAt(0)

            val outputLibrary = OutputLibrary(
                library.index,
                library.books
                    .filter { !selectedBooks.contains(it) }
                    .sortedBy { bookToScore[it] }
                    .take(scanningTime - currentTime))

            outputLibraries.add(outputLibrary)
            selectedBooks.addAll(outputLibrary.books)
        }
        return outputLibraries
    }
}
