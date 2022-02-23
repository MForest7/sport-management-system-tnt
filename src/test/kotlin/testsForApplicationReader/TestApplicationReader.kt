package testsForApplicationReader

import competition.Applications
import competition.competitors.Competitor
import competition.teams.ApplicationsReader
import competition.teams.Team
import org.junit.Test
import java.io.File
import kotlin.test.assertEquals

class TestApplicationReader {
    @Test
    fun testEmptyDir() {
        val dir = "testData/testDataForApplicationsReader/testReadListOfTeamsFromDirectory/testEmptyDirectory"
        File(dir).mkdir()
        val data = ApplicationsReader(dir).read()
        assertEquals(data, Applications(mutableListOf()))
    }

    @Test
    fun testRegular() {
        val teams = Applications(
            mutableListOf(
                Team(
                    "ПСКОВ,РУСЬ", mutableListOf(
                        Competitor("VIP", "НИКИТИН", "ВАЛЕНТИН", "1941", "", "", "", 1),
                        Competitor("VIP", "НИКИТИНА", "АЛЛА", "1939", "КМС", "", "", 2),
                        Competitor("М14", "ТИХОМИРОВ", "ИВАН", "2007", "", "", "", 3),
                        Competitor("М14", "ЖЕЛЕЗНЫЙ", "МИХАИЛ", "2007", "", "", "", 4)
                    )
                ), Team(
                    "ПИТЕР",
                    mutableListOf(Competitor("VIP", "ПУПКИН", "ВАСЯ", "2013", "КМС", "", "", 5))
                ), Team(
                    "МОСКВА",
                    mutableListOf(Competitor("М14", "ПУПКИН", "ВАСЯ", "2013", "КМС", "", "", 6))
                )
            )
        )
        val dir = "testData/testDataForApplicationsReader/testReadListOfTeamsFromDirectory/testThreeApplications"
        val data = ApplicationsReader(dir).read()
        assertEquals(data, teams)
    }
}