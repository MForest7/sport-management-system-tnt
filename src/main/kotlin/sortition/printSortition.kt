package sortition

import classes.Competition
import classes.CompetitorInCompetition
import classes.Group
import com.github.doyaaaaaken.kotlincsv.client.CsvWriter
import com.github.doyaaaaaken.kotlincsv.client.ICsvFileWriter
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import parsers.createDir

interface SortitionPrinter {
    fun printSortition(competition: Competition)
}

class CSVSortitionPrinter(private val dir: String): SortitionPrinter {
    private lateinit var competition: Competition

    override fun printSortition(competition: Competition) {
        this.competition = competition
        createDir(dir)
        groupToCompetitorMatching(competition).forEach { (group, competitors) ->
            writeCompetitorsInGroup(dir, group, competitors)
        }
    }

    private fun groupToCompetitorMatching(competition: Competition): Map<Group, List<CompetitorInCompetition>> =
        competition.competitors.groupBy { competitor -> competitor.group }

    private fun writeCompetitorsInGroup(dir: String, group: Group, competitors: List<CompetitorInCompetition>) {
        csvWriter().open(dir + "/" + group.name) {
            this.writeRow(listOf(group.name))
            competitors.forEach { competitor -> writeCompetitor(this, competitor) }
        }
    }

    private fun writeCompetitor(csvFileWriter: ICsvFileWriter, competitor: CompetitorInCompetition) {
        val startTime = competition.start.timeMatching[competitor]
        requireNotNull(startTime)
        csvFileWriter.writeRow(
            competitor.number, competitor.surname, competitor.name, competitor.birth,
            competitor.title, startTime.stringRepresentation
        )
    }
}
