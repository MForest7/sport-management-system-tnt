package sortition

import classes.*

class Sortition(val listOfTeams: List<Team>, private val groupNameForUngrouped: String = "OTHER") {
    fun generateCompetition(): Competition {
        val numberedCompetitors = numberAllCompetitors(listOfTeams)
        val competitorsInCompetition = assignGroupsToNumberedCompetitors(numberedCompetitors)

        val competitorToTimeMatching = appointTime(competitorsInCompetition)
        val checkpoints = mutableListOf(CheckPoint(timeMatching = competitorToTimeMatching.toMutableMap()))

        val invertCompetitorToNumberMatching = competitorsInCompetition.associateBy { competitor -> competitor.number }
        return Competition(checkpoints, competitorsInCompetition, invertCompetitorToNumberMatching)
    }

    private class NumberedCompetitor(val competitor: Competitor, val number: String, val team: Team) {
        fun addGroup(group: Group) = CompetitorInCompetition(competitor, number, group, team)
    }

    private val countOfCompetitors: Int = listOfTeams.sumOf { team -> team.competitors.size }
    private val competitorNumbersGenerator = (1..countOfCompetitors).map { it.toString() }.shuffled().iterator()

    private fun numberAllCompetitors(listOfTeams: List<Team>): List<NumberedCompetitor> {
        return listOfTeams.map { team: Team ->
            team.competitors.map { competitor ->
                NumberedCompetitor(competitor, competitorNumbersGenerator.next(), team)
            }
        }.flatten()
    }

    private fun getNameOfRealGroup(competitor: Competitor) = competitor.wishGroup.ifBlank { groupNameForUngrouped }

    private fun assignGroupsToNumberedCompetitors(listOfNumberedCompetitors: List<NumberedCompetitor>): List<CompetitorInCompetition> {
        val mappingGroupToCompetitors = listOfNumberedCompetitors.groupBy {
                numberedCompetitor -> getNameOfRealGroup(numberedCompetitor.competitor)
        }
        return mappingGroupToCompetitors.map { (groupName, numberedCompetitors) ->
            val group = Group(groupName)
            numberedCompetitors.map { competitor -> competitor.addGroup(group) }
        }.flatten()
    }

    private fun appointTime(competitorsInCompetition: List<CompetitorInCompetition>): Map<CompetitorInCompetition, Time> {
        val interval = 60
        var currentTime = 12 * 60 * 60
        return competitorsInCompetition.associateWith {
            Time(currentTime).also { currentTime += interval }
        }
    }
}