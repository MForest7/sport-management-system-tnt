package parsers

import classes.CsvReader
import classes.Time
import java.io.File
import java.lang.Exception


open class DirectoryReaderException(message: String) : Exception(message)

class DirectoryReaderExceptionInDirectory(message: String) : DirectoryReaderException(message)

interface DirectoryReader<IntermediateRepresentation, FinalType> {
    val dir: String

    fun readUnit(csvReader: CsvReader): IntermediateRepresentation

    fun readUnmerged(): List<IntermediateRepresentation> {
        val listOfFiles = File(dir).list()?.toList() ?: throw DirectoryReaderExceptionInDirectory("Strange directory")

        val sortedListOfFiles = listOfFiles.sorted() // to escape undefined order

        return sortedListOfFiles.map { readUnit(CsvReader("$dir/$it")) }
    }

    fun read(): FinalType
}

fun convertRecordsToTimeMatching(list: List<List<String>>): Map<String, Time> {
    val timeMatching = mutableMapOf<String, Time>()
    for (record in list) {
        require(record.size == 2) { "Record($record) size is not 2" }
        timeMatching[record[0]] = Time(record[1])
    }
    return timeMatching
}