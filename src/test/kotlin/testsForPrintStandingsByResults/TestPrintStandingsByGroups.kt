package testsForPrintStandingsByResults

import classes.*
import standings.StandingsOfGroup
import standings.printStandingsByGroups
import java.io.File
import java.nio.charset.Charset
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

internal class TestPrintStandingsByGroups {
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

        val groups = listOf("VIP", "М14").associateWith { Group(it) }

        var acc = 0
        val competitors = teams.map { team ->
            team.competitors.map { it ->
                acc++
                CompetitorInCompetition(it, acc.toString(), groups.getOrDefault(it.wishGroup, Group(it.wishGroup)), team)
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
            CheckPoint("start", competitors.associateWith { startTimes[it.number.toInt() - 1] }),
            CheckPoint(
                "finish",
                competitors.dropLast(1).associateWith { finishTimes[it.number.toInt() - 1] })
        )

        return Competition(
            checkPoints,
            competitors,
            listOf("1", "2", "3", "4", "5", "6").associateWith { competitors[it.toInt() - 1] }
        )
    }
    @Test
    fun testSimpleGroup() {
        val competition = generateCompetition()

        val standingsInGroups = competition.competitors.groupBy { it.group }.map {
            StandingsOfGroup(competition, it.key, it.value)
        }

        File("./testData/testPrintStandingsByGroups/standingsSimpleGroup.csv").bufferedWriter().use { print("") }
        printStandingsByGroups(
            File("./testData/testPrintStandingsByGroups/standingsSimpleGroup.csv"),
            standingsInGroups
        )

        assertContentEquals(
            File("./testData/testPrintStandingsByGroups/expectedStandingsSimpleGroup.csv").readLines(),
            File("./testData/testPrintStandingsByGroups/standingsSimpleGroup.csv").readLines()
        )
    }
}