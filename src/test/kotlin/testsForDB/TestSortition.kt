package testsForDB

import DB
import classes.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import sortition.Sortition
import java.io.File
import kotlin.test.assertEquals

class TestSortition {
    companion object {
        var counter = 1
    }

    lateinit var db: DB

    @Before
    fun init() {
        db = DB("./test$counter")
    }

    @After
    fun remove() {
        File("./test$counter.mv.db").delete()
        counter++
    }

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

    private val groups = listOf(
        Group("М14", listOf(), AllCheckpointsCalculator),
        Group("VIP", listOf(), AllCheckpointsCalculator),
        Group("KEK", listOf(), AllCheckpointsCalculator)
    )

    private val competition = Sortition(Applications(teams), groups).generateCompetition()


    @Test
    fun testCompetition() {
        competition.setCheckpointsFromIncomplete(
            IncompleteCompetition(
                listOf(
                    IncompleteCheckpoint(
                        "as", listOf(
                            IncompleteCheckPointRecord("3", Time(2))
                        )
                    )
                )
            )
        )
        db.setCompetition(competition)
        val result = db.getCompetition()
        require(result != null)
        assertEquals(result.timeMatching.size, competition.timeMatching.size)
        assertEquals(result.checkpoints.size, competition.checkpoints.size)
        assertEquals(result.competitors.size, competition.competitors.size)
    }
}