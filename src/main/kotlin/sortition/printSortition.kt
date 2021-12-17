package sortition

import classes.Competition
import classes.CompetitorInCompetition
import classes.CsvWriter
import classes.Group
import com.github.doyaaaaaken.kotlincsv.client.ICsvFileWriter
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter


class SortitionPrinter(private val dir: String) {
    private fun groupToCompetitorMatching(competition: Competition): Map<Group, List<CompetitorInCompetition>> =
        competition.competitors.groupBy { competitor -> competitor.group }

    private fun transformCompetitorIntoRow(
        competitor: CompetitorInCompetition,
        competition: Competition
    ): List<String> {
        val startTime = competition.start.timeMatching[competitor]?.get(0)
        requireNotNull(startTime)
        return listOf(
            competitor.number, competitor.surname, competitor.name, competitor.birth,
            competitor.title, startTime.stringRepresentation
        )
    }

    private fun writeCompetitorsInGroup(
        group: Group,
        competitors: List<CompetitorInCompetition>,
        competition: Competition
    ) {
        val writer = CsvWriter(dir + "/" + group.name + ".csv")
        val rows = mutableListOf<List<String>>()
        rows.add(listOf(group.name))
        rows.addAll(
            competitors.map { competitor ->
                transformCompetitorIntoRow(competitor, competition)
            }
        )
        writer.writeRows(rows)
    }

    fun print(competition: Competition) {
        groupToCompetitorMatching(competition).forEach { (group, competitors) ->
            writeCompetitorsInGroup(group, competitors, competition)
        }
    }
}
