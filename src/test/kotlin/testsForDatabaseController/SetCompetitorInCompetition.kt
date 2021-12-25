package testsForDatabaseController

import DB
import DatabaseController
import classes.Time
import getCompetitionForTests
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.File
import kotlin.test.assertEquals

class TestSetCompetitorInCompetition {
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

    @Test
    fun testCompetition() {
        var competition = getCompetitionForTests()
        db.setCompetition(competition)
        val controller = DatabaseController(db)
        controller.set(competition.competitors[0], Time("13:00:00"), competition.checkpoints[2], 0)
        val res = db.getCompetition()
        require(res != null)
        competition = res
        val actualTime = competition.checkpoints[2].timeMatching[competition.competitors[0]]!![0]
        val expectedTime = Time("13:00:00")
        assertEquals(expectedTime, actualTime)
    }
}