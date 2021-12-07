package parsers

import classes.Competitor
import classes.Team
import com.github.doyaaaaaken.kotlincsv.client.CsvFileReader
import logger
import java.io.File
import kotlin.reflect.full.memberProperties

class CsvFileReaderWithFileName(private val fileName: String, private val reader: CsvFileReader) {

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


    fun createTeamFromFile(): Team {
        val teamName = getTeamNameFromRecord(readSingleRecord())
        val competitors = readAllRemainingRecords().map(::fromListOfStringToCheckpoints)
        logger.debug { competitors }
        return Team(teamName, competitors)
    }

    private fun fromListOfStringToCheckpoints(list: List<String>) =
        getCompetitorFromListOfStrings(list.toMutableList().dropLastWhile { it.isBlank() })

    private fun getTeamNameFromRecord(record: List<String>?): String =
        getFirstFieldFromRecord(record)

    private fun getCompetitorFromListOfStrings(competitorInfo: List<String>): Competitor {
        val properties = Competitor::class.memberProperties.size
        logger.debug { "getCompetitorFromListOfStrings starts" }
        logger.debug { "competitorInfo = $competitorInfo" }
        require(competitorInfo.size <= properties) { "Too many records in line $competitorInfo" }

        val competitor = Competitor(
            competitorInfo.elementAtOrElse(0) { "" },
            competitorInfo.elementAtOrElse(1) { "" },
            competitorInfo.elementAtOrElse(2) { "" },
            competitorInfo.elementAtOrElse(3) { "" },
            competitorInfo.elementAtOrElse(4) { "" },
            competitorInfo.elementAtOrElse(5) { "" },
            competitorInfo.elementAtOrElse(6) { "" })
        logger.debug { "Competitor = $competitor" }
        return competitor
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