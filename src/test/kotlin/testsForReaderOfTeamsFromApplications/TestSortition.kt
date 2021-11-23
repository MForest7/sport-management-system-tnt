package testsForReaderOfTeamsFromApplications


import classes.Competitor
import classes.Team
import sortition.generateSortition
import kotlin.test.Test
import kotlin.test.assertContentEquals

internal class TestSortition {
    private val competitors =
        listOf(
            Competitor("М14", "ЖЕЛЕЗНЫЙ", "МИХАИЛ", "2007", "", "", ""),
            Competitor("VIP", "НИКИТИН", "ВАЛЕНТИН", "1941", "", "", ""),
            Competitor("VIP", "НИКИТИНА", "АЛЛА", "1939", "КМС", "", ""),
            Competitor("М14", "ТИХОМИРОВ", "ИВАН", "2007", "", "Негоден", "ЮГОРИЯ"),
            Competitor("", "ПУПКИН", "ВАСИЛИЙ", "1939", "КМС", "", ""),
            Competitor("KEK", "ШЛЕПКИН", "ВЛАДИСЛАВ", "2007", "", "", ""),
            Competitor("", "ШТУЧКИН", "ПЕТР", "1000", "1", "", ""),
            Competitor("LOOOL", "КУРОЧКИН", "ДАМИР", "2002", "МС", "Годен", "СОГАЗ"),
        )

    private val teams = listOf(
        Team(
            "Chuhiya",
            listOf(
                competitors[0],
                competitors[1],
                competitors[2],
            )
        ),
        Team(
            "Habohistan",
            listOf(
                competitors[3],
                competitors[4],
            )
        ),
        Team(
            "Russia",
            listOf(
                competitors[5],
                competitors[6],
                competitors[7],
            )
        )
    )

    private val competition = generateSortition(teams)


    @Test
    fun checkStructure() {
        assert(competition.competitors.size == competitors.size)
        assert(competition.checkpoints.size == 1)
        assert(competition.checkpoints[0].timeMatching.size == competitors.size)
        assert(competition.numberMatching.size == competitors.size)
    }

    @Test
    fun checkAllCompetitorsInCompetition() {
        competition.competitors.forEach { competitorInCompetition ->
            assert(competitorInCompetition in competitors)
        }
    }

    @Test
    fun checkAllCompetitorsNumbered() {
        competition.numberMatching.values.forEach { competitorInCompetition ->
            assert(competitorInCompetition in competitors)
        }
        competition.competitors.forEach { competitorIncompetition ->
            assert(
                competition.numberMatching[competitorIncompetition.number] == competitorIncompetition
            )
        }
    }

    @Test
    fun checkAllNumbersUsed() {
        assertContentEquals(
            competition.numberMatching.keys.sorted(),
            (1..competitors.size).map {
                it.toString()
            }.sorted()
        )
    }

    @Test
    fun checkTeamsSame() {
        teams.forEach { team ->
            team.competitors.forEach { competitor ->
                val current = competition.competitors.find { it == competitor }
                assert(current?.team == team)
            }
        }
    }
}