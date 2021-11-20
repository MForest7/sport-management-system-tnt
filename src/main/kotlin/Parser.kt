import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import ru.emkn.kotlin.sms.logger
import java.io.File

data class Group(val name: String)

data class Competitor(
    val wishGroup: Group,
    val surname: String,
    val name: String,
    val dateOfBirth: String,
    val title: String,
    val medicalExamination: String,
    val medicalInsurance: String,
)

data class Team(val name: String, val competitors: List<Competitor>)

fun getCompetitorFromListOfStrings(competitorInfo: List<String>): Competitor {
    logger.debug { "getCompetitorFromListOfStrings starts" }
    logger.debug { "competitorInfo = $competitorInfo" }
    require(competitorInfo.size <= 7) { "Too many records in line $competitorInfo" }
    val competitor = Competitor(
        Group(competitorInfo.elementAtOrElse(0) { "" }),
        competitorInfo.elementAtOrElse(1) { "" },
        competitorInfo.elementAtOrElse(2) { "" },
        competitorInfo.elementAtOrElse(3) { "" },
        competitorInfo.elementAtOrElse(4) { "" },
        competitorInfo.elementAtOrElse(5) { "" },
        competitorInfo.elementAtOrElse(6) { "" }
    )
    logger.debug {"Competitor = $competitor"}
    return competitor
}

fun readSingleTeamFromFile(fileName: String): Team {
    logger.info { "readSingleTeamFromFile starts" }
    var teamName = ""
    val competitors = mutableListOf<Competitor>()
    csvReader().open(fileName) {
        var currentRecord = readNext()
        logger.debug {"$fileName line contains \"$currentRecord\""}

        require(currentRecord != null) { "No records" }
        teamName = currentRecord[0]
        require(teamName.isNotBlank()) { "Team name($fileName application) is blank" }

        currentRecord = readNext() // Skip definition line
        logger.debug {"$fileName line contains \"$currentRecord\""}

        currentRecord = readNext()
        logger.debug {"$fileName line contains \"$currentRecord\""}
        while (currentRecord != null) {
            competitors.add(
                getCompetitorFromListOfStrings(
                    currentRecord.toMutableList().dropLastWhile { it.isBlank() })
            )

            currentRecord = readNext()
            logger.debug {"$fileName line contains \"$currentRecord\""}
        }
    }
    val team = Team(teamName, competitors)
    logger.debug {"team = $team"}
    return team
}

fun readListOfTeamsFromDirectory(dir: String): List<Team> {
    require(File(dir).exists()) { "Applications directory don't exists" }
    val listFromPath = File(dir).list()
    require(listFromPath != null) { "Unknown state with directory $dir" }
    return listFromPath.map { readSingleTeamFromFile(dir + it) }
}