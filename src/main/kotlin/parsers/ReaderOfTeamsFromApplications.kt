package parsers

import classes.Applications
import classes.Competitor
import classes.CsvReader
import classes.Team
import logger
import kotlin.reflect.full.memberProperties


class ApplicationsReader(override val dir: String) : DirectoryReader<Team, Applications> {
    companion object {
        var idCounter = 1
    }

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
            competitorInfo.elementAtOrElse(6) { "" },
            idCounter++
        )
        logger.debug { "Competitor = $competitor" }
        return competitor
    }

    private fun fromListOfStringToCheckpoints(list: List<String>) =
        getCompetitorFromListOfStrings(list.toMutableList().dropLastWhile { it.isBlank() })

    override fun readUnit(csvReader: CsvReader): Team {
        val teamName = csvReader.readFirstElementInFirstRow()
        val application = csvReader.readAllExceptFirst()
        val competitors = application.map(::fromListOfStringToCheckpoints).toMutableList()
        return Team(teamName, competitors)
    }

    override fun read(): Applications {
        return Applications(readUnmerged().toMutableList())
    }
}

