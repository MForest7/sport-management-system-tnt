package testsForApplicationReader

import classes.Applications
import classes.Competitor
import classes.Team
import org.junit.Test
import parsers.ApplicationsReader
import java.io.File
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class testApplicationReader {
    @Test
    fun testEmptyDir() {
        val dir = "testData/testDataForApplicationsReader/testReadListOfTeamsFromDirectory/testEmptyDirectory"
        File(dir).mkdir()
        val data = ApplicationsReader(dir).read()
        assertEquals(data, Applications(listOf()))
    }

    @Test
    fun testRegular() {
        val teams = Applications(
            listOf(
                Team(
                    "ПСКОВ,РУСЬ", listOf(
                        Competitor("VIP", "НИКИТИН", "ВАЛЕНТИН", "1941", "", "", "", 1),
                        Competitor("VIP", "НИКИТИНА", "АЛЛА", "1939", "КМС", "", "", 2),
                        Competitor("М14", "ТИХОМИРОВ", "ИВАН", "2007", "", "", "", 3),
                        Competitor("М14", "ЖЕЛЕЗНЫЙ", "МИХАИЛ", "2007", "", "", "", 4)
                    )
                ), Team(
                    "ПИТЕР",
                    listOf(Competitor("VIP", "ПУПКИН", "ВАСЯ", "2013", "КМС", "", "", 5))
                ), Team(
                    "МОСКВА",
                    listOf(Competitor("М14", "ПУПКИН", "ВАСЯ", "2013", "КМС", "", "", 6))
                )
            )
        )
        val dir = "testData/testDataForApplicationsReader/testReadListOfTeamsFromDirectory/testThreeApplications"
        val data = ApplicationsReader(dir).read()
        assertEquals(data, teams)
    }
}