package parsers

import classes.Time
import com.github.doyaaaaaken.kotlincsv.client.CsvFileReader
import logger
import java.io.File

data class IncompleteCheckpoint(val name: String, val timeMatching: Map<String, Time>)

class CsvFileReaderWithFileName(val fileName: String, private val reader: CsvFileReader) {

    fun readAllRemainingRecords() = reader.readAllAsSequence().toList()

    fun readSingleRecord(): List<String>? {
        val record = reader.readNext()
        logger.debug { "$fileName line contains \"$record\"" }
        return record
    }

    fun getFirstFieldFromRecord(record: List<String>?): String {
        require(record != null) { "No records" }
        require(record[0].isNotBlank()) { "First record(in $fileName) is blank" }
        return record[0]
    }
}

fun createDir(dir: String) {
    if (!File(dir).exists())
        File(dir).mkdir()
}

fun <T> getMappedListOfFilesFromDir(dir: String, mapFunction: (fileName: String) -> T): List<T> {
    createDir(dir)
    val listFromPath = File(dir).list()
    require(listFromPath != null) { "Unknown state with directory $dir" }
    return listFromPath.sorted().map { mapFunction(dir + it) }
}


fun convertRecordsToTimeMatching(list: List<List<String>>): Map<String, Time> {
    val timeMatching = mutableMapOf<String, Time>()
    for (record in list) {
        require(record.size == 2) { "Record($record) size is not 2" }
        timeMatching[record[0]] = Time(record[1])
    }
    return timeMatching
}
