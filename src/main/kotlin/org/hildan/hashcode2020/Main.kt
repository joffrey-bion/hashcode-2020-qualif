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
    val N = readInt()
    val list = List(N) { readInt() }
    return Problem(list)
}
