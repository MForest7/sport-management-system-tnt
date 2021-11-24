package testsForReaderOfTeamsFromApplications

import classes.Competitor
import classes.Team
import parsers.readListOfTeamsFromDirectory
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

internal class TestReadListOfTeamsFromDirectory {
    private val dirPath = "testData/testsDataFolderForReaderOfTeamsFromApplications/testReadListOfTeamsFromDirectory/"

    @Test
    fun testThreeApplications() {
        val listOfTeams = readListOfTeamsFromDirectory(dirPath + "testThreeApplications/")
        assertContentEquals(
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
            ), listOfTeams
        )
    }

    @Test
    fun testDirectoryDoesntExist() {
        assertFailsWith<IllegalArgumentException> { readListOfTeamsFromDirectory("") }
    }

    @Test
    fun testEmptyDirectory() {
        assertEquals(true, readListOfTeamsFromDirectory(dirPath + "testEmptyDirectory").isEmpty())
    }

}