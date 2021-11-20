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

    logger.debug { "Competitor = $competitor" }
    return competitor
}

fun getTeamNameFromRecord(record: List<String>?, fileName: String) : String {
    require(record != null) { "No records" }
    require(record[0].isNotBlank()) { "Team name($fileName application) is blank" }
    return record[0]
}

fun readSingleTeamFromFile(fileName: String): Team {
    logger.info { "readSingleTeamFromFile starts" }
    var teamName = ""
    var competitors = listOf<Competitor>()

    csvReader().open(fileName) {

        val readSingleRecord = {
            val record = readNext()
            logger.debug { "$fileName line contains \"$record\"" }
            record
        }

        val firstRecord = readSingleRecord()
        teamName = getTeamNameFromRecord(firstRecord, fileName)

        readSingleRecord() // skip definition line

        val fromListOfStringToListCompetitors = { list: List<String> ->
            getCompetitorFromListOfStrings(list.toMutableList().dropLastWhile { it.isBlank() })
        }

        competitors = this.readAllAsSequence().toList()
            .map(fromListOfStringToListCompetitors)
        logger.debug { competitors }
    }
    val team = Team(teamName, competitors)
    logger.debug { "team = $team" }
    return team
}

fun readListOfTeamsFromDirectory(dir: String): List<Team> {
    require(File(dir).exists()) { "Applications directory don't exists" }
    val listFromPath = File(dir).list()
    require(listFromPath != null) { "Unknown state with directory $dir" }
    return listFromPath.map { readSingleTeamFromFile(dir + it) }
}