package parsers

import classes.Team
import com.github.doyaaaaaken.kotlincsv.client.CsvFileReader
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import logger

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
