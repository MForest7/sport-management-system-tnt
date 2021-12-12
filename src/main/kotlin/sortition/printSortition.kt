package sortition

import classes.Competition
import classes.CompetitorInCompetition
import classes.Group
import com.github.doyaaaaaken.kotlincsv.client.ICsvFileWriter
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import parsers.createDir

private fun groupToCompetitorMatching(competition: Competition): Map<Group, List<CompetitorInCompetition>> =
    competition.competitors.groupBy { competitor -> competitor.group }

private fun writeCompetitor(csvFileWriter: ICsvFileWriter, competitor: CompetitorInCompetition, competition: Competition) {
    val startTime = competition.checkpoints[0].timeMatching[competitor]
    requireNotNull(startTime)
    csvFileWriter.writeRow(
        competitor.number, competitor.surname, competitor.name, competitor.birth,
        competitor.title, startTime.stringRepresentation
    )
}

private fun writeCompetitorsInGroup(
    dir: String,
    group: Group,
    competitors: List<CompetitorInCompetition>,
    competition: Competition
) {
    csvWriter().open(dir + "/" + group.name) {
        this.writeRow(listOf(group.name))
        competitors.forEach { competitor ->
            writeCompetitor(this, competitor, competition)
        }
    }
}

fun printSortition(dir: String, competition: Competition) {
    createDir(dir)
    groupToCompetitorMatching(competition).forEach { (group, competitors) ->
        writeCompetitorsInGroup(dir, group, competitors, competition)
    }
}