package classes

import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter

class CsvWriter(private val filename: String) {
    fun writeRow(row: List<String>) {
        csvWriter().open(filename) { writeRow(row) }
    }

    fun writeRows(table: List<List<String>>) {
        csvWriter().open(filename) { writeRows(table) }
    }
}