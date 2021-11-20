import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import ru.emkn.kotlin.sms.logger
import java.io.File
import kotlin.reflect.full.memberProperties

fun getCompetitorFromListOfStrings(competitorInfo: List<String>): Competitor {
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

fun getTeamNameFromRecord(record: List<String>?, fileName: String): String {
    require(record != null) { "No records" }
    require(record[0].isNotBlank()) { "Team name($fileName application) is blank" }
    return record[0]
}

fun readSingleTeamFromFile(fileName: String): Team {
    logger.info { "readSingleTeamFromFile starts" }

    val (teamName, competitors) = csvReader().open(fileName) {
        val readSingleRecord = {
            val record = readNext()
            logger.debug { "$fileName line contains \"$record\"" }
            record
        }

        val firstRecord = readSingleRecord()
        val teamName = getTeamNameFromRecord(firstRecord, fileName)

        readSingleRecord() // skip definition line

        val fromListOfStringToListCompetitors = { list: List<String> ->
            getCompetitorFromListOfStrings(list.toMutableList().dropLastWhile { it.isBlank() })
        }

        val competitors = this.readAllAsSequence().toList().map(fromListOfStringToListCompetitors)
        logger.debug { competitors }

        return@open Pair(teamName, competitors)
    }

    val team = Team(teamName, competitors)
    logger.debug { "team = $team" }
    return team
}

fun readListOfTeamsFromDirectory(dir: String): List<Team> {
    if (!File(dir).exists()) File(dir).mkdir()
    val listFromPath = File(dir).list()
    require(listFromPath != null) { "Unknown state with directory $dir" }
    return listFromPath.sorted().map { readSingleTeamFromFile(dir + it) }
}