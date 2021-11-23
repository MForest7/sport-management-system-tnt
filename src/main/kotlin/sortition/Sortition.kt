package sortition

import classes.*


private data class CompetitorWithNumber(val competitor: Competitor, val number: String) : Competitor(competitor)

private data class CompetitorWithNumberAndTeam(val competitorWithNumber: CompetitorWithNumber, val team: Team)

private data class TeamWithNumberedCompetitors(val team: Team, val numberedCompetitors: List<CompetitorWithNumber>)

private fun countCompetitorsInListOfTeams(listOfTeams: List<Team>) =
    listOfTeams.sumOf { team -> team.competitors.size }

private fun getAllWishedGroupNamesInTeam(listOfTeams: List<Team>) =
    listOfTeams.flatMap { team -> team.competitors.map { competitor -> competitor.wishGroup } }.toSet()

private fun getUnusedGroupName(setOfGroupNames: Set<String>): String {
    var i = 1
    while ("M$i" in setOfGroupNames) i++
    return "M$i"
}

private fun appointEachCompetitorNumber(team: Team, numberGenerator: Iterator<String>): List<CompetitorWithNumber> =
    team.competitors.map { competitor -> CompetitorWithNumber(competitor, numberGenerator.next()) }

private fun getRealGroup(wishedGroup: String, groupForUngrouped: String): String =
    wishedGroup.ifBlank { groupForUngrouped }


private fun createCompetitorNumberGenerator(numberOfCompetitors: Int) =
    (1..numberOfCompetitors).map { it.toString() }.shuffled().iterator()


private fun numberInsideTeam(competitorNumbersGenerator: Iterator<String>) = { team: Team ->
    val numberedCompetitors = appointEachCompetitorNumber(team, competitorNumbersGenerator)
    TeamWithNumberedCompetitors(team, numberedCompetitors)
}

private fun numberAllCompetitors(listOfTeams: List<Team>): List<TeamWithNumberedCompetitors> {
    val competitorNumbersGenerator =
        createCompetitorNumberGenerator(countCompetitorsInListOfTeams(listOfTeams))
    return listOfTeams.map(numberInsideTeam(competitorNumbersGenerator))
}


private fun appointTime(competitorsInCompetition: List<CompetitorInCompetition>): Map<CompetitorInCompetition, Time> {
    val interval = 60
    var currentTime = 12 * 60 * 60
    return competitorsInCompetition.associateWith {
        Time(currentTime).also { currentTime += interval }
    }
}


private fun getRidOfListOfTeamsAndRememberInCompetitor(teamsWithNumberedCompetitors: List<TeamWithNumberedCompetitors>): List<CompetitorWithNumberAndTeam> =
    teamsWithNumberedCompetitors.flatMap { team ->
        team.numberedCompetitors.map { competitorWithNumber ->
            CompetitorWithNumberAndTeam(
                competitorWithNumber,
                team.team
            )
        }
    }


private fun mapGroupToListOfCompetitors(
    listOfCompetitorWithNumberAndTeam: List<CompetitorWithNumberAndTeam>,
    groupForUngrouped: String
) = listOfCompetitorWithNumberAndTeam.groupBy { competitorWithNumberAndTeam ->
    getRealGroup(
        competitorWithNumberAndTeam.competitorWithNumber.competitor.wishGroup,
        groupForUngrouped
    )
}


private fun createListOfCompInCompFromListOfCompetitorWithNumberAndTeam(
    currentGroup: Group,
    competitorsWithNumberAndTeam: List<CompetitorWithNumberAndTeam>
): List<CompetitorInCompetition> {
    return competitorsWithNumberAndTeam.map { competitorWithNumberAndTeam ->
        CompetitorInCompetition(
            competitorWithNumberAndTeam.competitorWithNumber.competitor,
            competitorWithNumberAndTeam.competitorWithNumber.number,
            currentGroup,
            competitorWithNumberAndTeam.team
        )
    }
}

private fun createCompInCompListFromGroupedCompWithNumberAndTeam(mappingGroupToCompetitors: Map<String, List<CompetitorWithNumberAndTeam>>) =
    mappingGroupToCompetitors.flatMap { (groupName, competitorsWithNumberAndTeam) ->
        val currentGroup = Group(groupName)
        createListOfCompInCompFromListOfCompetitorWithNumberAndTeam(currentGroup, competitorsWithNumberAndTeam)
    }


fun generateSortition(listOfTeams: List<Team>): Competition {
    val teamsWithNumberedCompetitors = numberAllCompetitors(listOfTeams)

    val groupForUngrouped = getUnusedGroupName(getAllWishedGroupNamesInTeam(listOfTeams))

    val listOfCompetitorWithNumberAndTeam = getRidOfListOfTeamsAndRememberInCompetitor(teamsWithNumberedCompetitors)

    val mappingGroupToCompetitors = mapGroupToListOfCompetitors(listOfCompetitorWithNumberAndTeam, groupForUngrouped)

    val competitorsInCompetition = createCompInCompListFromGroupedCompWithNumberAndTeam(mappingGroupToCompetitors)

    val competitorToTimeMatching = appointTime(competitorsInCompetition)

    val checkpoints = listOf(CheckPoint("0km", competitorToTimeMatching))

    val invertCompetitorToNumberMatching = competitorsInCompetition.associateBy { competitor -> competitor.number }

    return Competition(checkpoints, competitorsInCompetition, invertCompetitorToNumberMatching)
}
