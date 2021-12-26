package readers

import basicClasses.CheckpointForParticipantRecord
import basicClasses.IncompleteCheckPointRecord
import basicClasses.Time
import java.io.File


open class DirectoryReaderException(message: String) : Exception(message)

class DirectoryReaderExceptionInDirectory(message: String) : DirectoryReaderException(message)

interface DirectoryReader<IntermediateRepresentation, FinalType> {
    val dir: String

    fun readUnit(csvReader: CsvReader): IntermediateRepresentation

    fun readUnmerged(): List<IntermediateRepresentation> {
        val listOfFiles = File(dir).list()?.toList() ?: throw DirectoryReaderExceptionInDirectory("Strange directory ($dir)")

        val sortedListOfFiles = listOfFiles.sorted() // to escape undefined order

        return sortedListOfFiles.map { readUnit(CsvReader("$dir/$it")) }
    }

    fun read(): FinalType
}

fun convertRecordsToParticipantRecords(list: List<List<String>>): List<CheckpointForParticipantRecord> {
    val timeMatching = mutableListOf<CheckpointForParticipantRecord>()
    for (record in list) {
        require(record.size == 2) { "Record($record) size is not 2" }
        timeMatching.add(CheckpointForParticipantRecord(record[0], Time(record[1])))
    }
    return timeMatching
}

fun convertRecordsToCheckpointRecords(list: List<List<String>>): List<IncompleteCheckPointRecord> {
    val timeMatching = mutableListOf<IncompleteCheckPointRecord>()
    for (record in list) {
        require(record.size == 2) { "Record($record) size is not 2" }
        timeMatching.add(IncompleteCheckPointRecord(record[0], Time(record[1])))
    }
    return timeMatching
}