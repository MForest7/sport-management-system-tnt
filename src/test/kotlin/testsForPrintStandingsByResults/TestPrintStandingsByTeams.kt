package testsForPrintStandingsByResults

import classes.*
import standings.GroupStandingsPrinter
import standings.StandingsInGroups
import standings.StandingsInTeams
import standings.TeamsStandingsPrinter
import java.io.File
import kotlin.test.Test
import kotlin.test.assertContentEquals

internal class TestPrintStandingsByTeams {
    private fun generateCompetition(): Competition {

        val teams = listOf(
            Team(
                "ПСКОВ,РУСЬ", listOf(
                    Competitor("VIP", "НИКИТИН", "ВАЛЕНТИН", "1941", "", "", ""),
                    Competitor("VIP", "НИКИТИНА", "АЛЛА", "1939", "КМС", "", ""),
                    Competitor("VIP", "ТИХОМИРОВ", "ИВАН", "2007", "", "", ""),
                    Competitor("М14", "ЖЕЛЕЗНЫЙ", "МИХАИЛ", "2007", "", "", "")
                )
            ), Team(
                "ПИТЕР",
                listOf(Competitor("VIP", "ПУПКИН", "ВАСЯ", "2013", "КМС", "", ""))
            ), Team(
                "МОСКВА",
                listOf(Competitor("М14", "ПУПКИН", "ВАСЯ", "2013", "КМС", "", ""))
            )
        )

        val groups =
            listOf("VIP", "М14").associateWith { Group(it, listOf("start", "finish"), AllCheckpointsCalculator) }

        var acc = 0
        val competitors = teams.map { team ->
            team.competitors.map {
                acc++
                CompetitorInCompetition(
                    it,
                    acc.toString(),
                    groups.getOrDefault(
                        it.wishGroup,
                        Group(it.wishGroup, listOf("start", "finish"), AllCheckpointsCalculator)
                    ),
                    team
                )
            }
        }.flatten()

        val startTimes = listOf(
            Time("12:00:00"),
            Time("13:00:00"),
            Time("14:00:00"),
            Time("15:00:00"),
            Time("16:00:00"),
            Time("17:00:00")
        )
        val finishTimes = listOf(
            Time("12:32:56"),
            Time("13:33:46"),
            Time("14:33:46"),
            Time("15:34:49"),
            Time("16:35:54"),
        )

        val checkPoints = listOf(
            CheckPoint(
                "start",
                competitors.associateWith { mutableListOf(startTimes[it.number.toInt() - 1]) }.toMutableMap()
            ),
            CheckPoint(
                "finish",
                competitors.dropLast(1).associateWith { mutableListOf(finishTimes[it.number.toInt() - 1]) }
                    .toMutableMap()
            )
        )

        return Competition(
            checkPoints.toMutableList(),
            competitors,
            listOf("1", "2", "3", "4", "5", "6").associateWith { competitors[it.toInt() - 1] }
        )
    }

    @Test
    fun testSimpleTeams() {
        val competition = generateCompetition()
        File("./testData/testPrintStandingsByTeams/standingsSimpleTeams.csv").bufferedWriter().use { print("") }
        TeamsStandingsPrinter(
            "./testData/testPrintStandingsByTeams/standingsSimpleTeams.csv"
        ).print(StandingsInTeams(competition))

        assertContentEquals(
            File("./testData/testPrintStandingsByTeams/expectedStandingsSimpleTeams.csv").readLines(),
            File("./testData/testPrintStandingsByTeams/standingsSimpleTeams.csv").readLines()
        )
    }

    @Test
    fun testEmptyCompetition() {
        val competition = Competition(
            mutableListOf(CheckPoint("start", mutableMapOf()), CheckPoint("finish", mutableMapOf())),
            listOf(),
            mapOf()
        )

        File("./testData/testPrintStandingsByTeams/standingsEmptyTeams.csv").bufferedWriter().use { print("") }
        GroupStandingsPrinter(
            "./testData/testPrintStandingsByTeams/standingsEmptyTeams.csv"
        ).print(StandingsInGroups(competition))

        assertContentEquals(
            File("./testData/testPrintStandingsByTeams/expectedStandingsEmptyTeams.csv").readLines(),
            File("./testData/testPrintStandingsByTeams/standingsEmptyTeams.csv").readLines()
        )
    }
}