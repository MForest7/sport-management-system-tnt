package testSortition


import competition.Applications
import competition.Group
import competition.Rules
import competition.competitors.Competitor
import competition.sortition.Sortition
import competition.teams.Team
import writers.AllCheckpointsCalculator
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertContentEquals

internal class TestSortition {
    private val competitors =
        listOf(
            Competitor("М14", "ЖЕЛЕЗНЫЙ", "МИХАИЛ", "2007", "", "", "", 1),
            Competitor("VIP", "НИКИТИН", "ВАЛЕНТИН", "1941", "", "", "", 2),
            Competitor("VIP", "НИКИТИНА", "АЛЛА", "1939", "КМС", "", "", 3),
            Competitor("М14", "ТИХОМИРОВ", "ИВАН", "2007", "", "Негоден", "ЮГОРИЯ", 4),
            Competitor("", "ПУПКИН", "ВАСИЛИЙ", "1939", "КМС", "", "", 5),
            Competitor("KEK", "ШЛЕПКИН", "ВЛАДИСЛАВ", "2007", "", "", "", 6),
            Competitor("", "ШТУЧКИН", "ПЕТР", "1000", "1", "", "", 7),
            Competitor("LOOOL", "КУРОЧКИН", "ДАМИР", "2002", "МС", "Годен", "СОГАЗ", 8),
        )

    private val teams = listOf(
        Team(
            "Chuhiya",
            mutableListOf(
                competitors[0],
                competitors[1],
                competitors[2],
            )
        ),
        Team(
            "Habohistan",
            mutableListOf(
                competitors[3],
                competitors[4],
            )
        ),
        Team(
            "Russia",
            mutableListOf(
                competitors[5],
                competitors[6],
                competitors[7],
            )
        )
    )

    private val groups = listOf(
        Group("М14", listOf(), AllCheckpointsCalculator),
        Group("VIP", listOf(), AllCheckpointsCalculator),
        Group("KEK", listOf(), AllCheckpointsCalculator)
    )

    private val competition = Sortition(Applications(teams.toMutableList()), Rules(groups)).generateCompetition()


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
            assertContains(competitors, competitorInCompetition)
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