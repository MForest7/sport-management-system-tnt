package parsers

import classes.Competitor
import classes.Team
import com.github.doyaaaaaken.kotlincsv.client.CsvFileReader
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import logger
import java.io.File
import kotlin.reflect.full.memberProperties

private fun getCompetitorFromListOfStrings(competitorInfo: List<String>): Competitor {
    val properties = Competitor::class.memberProperties.size
    logger.debug { "getCompetitorFromListOfStrings starts" }
    logger.debug { "competitorInfo = $competitorInfo" }
    require(competitorInfo.size <= properties) { "Too many records in line $competitorInfo" }
    val (wishGroup, surname, name, birth, title, MedicalExamination, MedicalInsurance) =
        (0 until properties).map { competitorInfo.elementAtOrElse(it) { "" } }
    val competitor = Competitor(wishGroup, surname, name, birth, title, MedicalExamination, MedicalInsurance)
    logger.debug { "Competitor = $competitor" }
    return competitor
}

private operator fun <E> List<E>.component7() = this[6]

private operator fun <E> List<E>.component6() = this[5]

private class CsvFileReaderWithFileName(val fileName: String, val reader: CsvFileReader) {
    fun createTeamFromFile(): Team {
        val teamName = getTeamNameFromRecord(readSingleRecord())
        readSingleRecord() // skip definition line
        val competitors = readAllRemainingRecords().map(::fromListOfStringToListCompetitors)
        logger.debug { competitors }
        return Team(teamName, competitors)
    }
}

private fun CsvFileReaderWithFileName.getTeamNameFromRecord(record: List<String>?): String {
    require(record != null) { "No records" }
    require(record[0].isNotBlank()) { "Team name($fileName application) is blank" }
    return record[0]
}

fun readSingleTeamFromFile(fileName: String): Team {
    logger.info { "readSingleTeamFromFile starts" }
    val createTeam = { csvFileReader: CsvFileReader ->
        CsvFileReaderWithFileName(fileName, csvFileReader).createTeamFromFile()
    }
    val team = csvReader().open(fileName, createTeam)
    logger.debug { "team = $team" }
    return team
}

private fun fromListOfStringToListCompetitors(list: List<String>) =
    getCompetitorFromListOfStrings(list.toMutableList().dropLastWhile { it.isBlank() })

private fun CsvFileReaderWithFileName.readAllRemainingRecords() = reader.readAllAsSequence().toList()

private fun CsvFileReaderWithFileName.readSingleRecord(): List<String>? {
    val record = reader.readNext()
    logger.debug { "$fileName line contains \"$record\"" }
    return record
}

fun readListOfTeamsFromDirectory(dir: String): List<Team> {
    if (!File(dir).exists()) File(dir).mkdir()
    val listFromPath = File(dir).list()
    require(listFromPath != null) { "Unknown state with directory $dir" }
    return listFromPath.sorted().map { readSingleTeamFromFile(dir + it) }
}