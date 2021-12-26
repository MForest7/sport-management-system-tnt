package readers

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader

open class CsvReaderException(message: String) : Exception(message)

class CsvReaderEmptyFileException(message: String) : CsvReaderException(message)

class CsvReader(val filename: String) {
    fun readFirstRow(): List<String> =
        csvReader().open(filename) {
            readNext() ?: throw CsvReaderEmptyFileException("$filename is empty")
        }

    fun readAll() = csvReader().open(filename) { readAllAsSequence().toList() }

    fun readFirstElementInFirstRow(): String = readFirstRow()[0]

    fun readAllExceptFirst(): List<List<String>> {
        val data = readAll()
        return if (data.isEmpty()) {
            listOf()
        } else {
            data.subList(1, data.size)
        }
    }
}