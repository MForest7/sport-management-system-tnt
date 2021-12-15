package testsForApplicationReader

import classes.Applications
import classes.Competitor
import classes.Team
import org.junit.Test
import parsers.ApplicationsReader
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class testApplicationReader {
    @Test
    fun testEmptyDir() {
        val dir = "testData/testDataForApplicationsReader/testReadListOfTeamsFromDirectory/testEmptyDirectory"
        val data = ApplicationsReader(dir).read()
        assertEquals(data, Applications(listOf()))
    }

    @Test
    fun testRegular() {
        val teams = Applications(
            listOf(
                Team(
                    "ПСКОВ,РУСЬ", listOf(
                        Competitor("VIP", "НИКИТИН", "ВАЛЕНТИН", "1941", "", "", ""),
                        Competitor("VIP", "НИКИТИНА", "АЛЛА", "1939", "КМС", "", ""),
                        Competitor("М14", "ТИХОМИРОВ", "ИВАН", "2007", "", "", ""),
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
        )
        val dir = "testData/testDataForApplicationsReader/testReadListOfTeamsFromDirectory/testThreeApplications"
        val data = ApplicationsReader(dir).read()
        assertEquals(data, teams)
    }
}