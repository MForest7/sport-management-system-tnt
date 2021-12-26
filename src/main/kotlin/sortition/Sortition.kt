package sortition

import basicClasses.*

class Sortition(
    private val applications: Applications,
    private val rules: Rules
) {
    init {
        require(rules.groups.isNotEmpty()) { "No available groups!" }
    }

    fun generateCompetition(): Competition {
        val numberedCompetitors = numberAllCompetitors(applications.teams)
        val competitorsInCompetition =
            assignGroupsToNumberedCompetitors(numberedCompetitors).sortedBy { it.number.toInt() }

        val competitorToTimeMatching = appointTime(competitorsInCompetition)
        val checkpoints = listOf(CheckPoint(timeMatching = competitorToTimeMatching.toMutableMap())) + rules.checkpoints

        val invertCompetitorToNumberMatching = competitorsInCompetition.associateBy { competitor -> competitor.number }
        return Competition(checkpoints, competitorsInCompetition, invertCompetitorToNumberMatching)
    }

    private class NumberedCompetitor(val competitor: Competitor, val number: String, val team: Team) {
        fun addGroup(group: Group) = CompetitorInCompetition(competitor, number, group, team)
    }

    private val countOfCompetitors: Int = applications.teams.sumOf { team -> team.competitors.size }
    private val competitorNumbersGenerator = (1..countOfCompetitors).map { it.toString() }.shuffled().iterator()

    private fun numberAllCompetitors(listOfTeams: List<Team>): List<NumberedCompetitor> {
        return listOfTeams.map { team: Team ->
            team.competitors.map { competitor ->
                NumberedCompetitor(competitor, competitorNumbersGenerator.next(), team)
            }
        }.flatten()
    }

    private fun assignGroupsToNumberedCompetitors(listOfNumberedCompetitors: List<NumberedCompetitor>): List<CompetitorInCompetition> {
        val mappingGroupToCompetitors = listOfNumberedCompetitors.groupBy { numberedCompetitor -> numberedCompetitor.competitor.wishGroup }
        return mappingGroupToCompetitors.map { (wishGroupName, numberedCompetitors) ->
            val group = rules.groups.firstOrNull { it.name == wishGroupName } ?: rules.groups.random()
            numberedCompetitors.map { competitor -> competitor.addGroup(group) }
        }.flatten()
    }

    private fun appointTime(competitorsInCompetition: List<CompetitorInCompetition>): Map<CompetitorInCompetition, MutableList<Time>> {
        val interval = 60
        var currentTime = 12 * 60 * 60
        return competitorsInCompetition.associateWith {
            listOf(Time(currentTime)).also { currentTime += interval }.toMutableList()
        }
    }
}