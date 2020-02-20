package org.hildan.hashcode2020

class Problem(
    val items: List<Int>
) {
    fun solve(): Iterable<String> {
        return items.map { (it * 2).toString() }
    }
}
