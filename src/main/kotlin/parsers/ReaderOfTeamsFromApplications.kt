package parsers

import classes.Competitor
import classes.Team
import com.github.doyaaaaaken.kotlincsv.client.CsvFileReader
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import logger
import kotlin.reflect.full.memberProperties

private fun CsvFileReaderWithFileName.createTeamFromFile(): Team {
    val teamName = getTeamNameFromRecord(readSingleRecord())
    val competitors = readAllRemainingRecords().map(::fromListOfStringToCheckpoints)
    logger.debug { competitors }
    return Team(teamName, competitors)
}

private fun fromListOfStringToCheckpoints(list: List<String>) =
    getCompetitorFromListOfStrings(list.toMutableList().dropLastWhile { it.isBlank() })

private fun CsvFileReaderWithFileName.getTeamNameFromRecord(record: List<String>?): String =
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

fun readSingleTeamFromFile(fileName: String): Team {
    val createTeam = { csvFileReader: CsvFileReader ->
        CsvFileReaderWithFileName(fileName, csvFileReader).createTeamFromFile()
    }
    val team = csvReader().open(fileName, createTeam)
    logger.debug { "team($team) from file($fileName)" }
    return team
}


fun readListOfTeamsFromDirectory(dir: String): List<Team> =
    getMappedListOfFilesFromDir(dir, ::readSingleTeamFromFile)
