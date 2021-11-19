import com.github.doyaaaaaken.kotlincsv.dsl.csvReader

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
    require(competitorInfo.size <= 7) { "Too many records in line $competitorInfo" }
    return Competitor(
        Group(competitorInfo.elementAtOrElse(0) { "" }),
        competitorInfo.elementAtOrElse(1) { "" },
        competitorInfo.elementAtOrElse(2) { "" },
        competitorInfo.elementAtOrElse(3) { "" },
        competitorInfo.elementAtOrElse(4) { "" },
        competitorInfo.elementAtOrElse(5) { "" },
        competitorInfo.elementAtOrElse(6) { "" }
    )
}

fun readSingleTeamFromFile(fileName: String): Team {
    var teamName = ""
    val competitors = mutableListOf<Competitor>()
    csvReader().open(fileName) {
        var currentRecord = readNext()
        require(currentRecord != null) { "No records" }
        teamName = currentRecord[0]
        require(teamName.isNotBlank()) { "Team name($fileName application) is blank" }
        currentRecord = readNext() // Skip definition line
        currentRecord = readNext()
        while (currentRecord != null) {
            competitors.add(
                getCompetitorFromListOfStrings(
                    currentRecord.toMutableList().dropLastWhile { it.isBlank() })
            )
            currentRecord = readNext()
        }
    }
    return Team(teamName, competitors)
}

