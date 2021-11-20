import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

internal class ReadListOfTeamsFromDirectory {
    private val dirPath = "testData/testReadListOfTeamsFromDirectory/"

    @Test
    fun testThreeApplications() {
        val listOfTeams = readListOfTeamsFromDirectory(dirPath + "testThreeApplications/")
        assertContentEquals(
            listOf(
                Team(
                    "ПСКОВ,РУСЬ", listOf(
                        Competitor(Group("VIP"), "НИКИТИН", "ВАЛЕНТИН", "1941", "", "", ""),
                        Competitor(Group("VIP"), "НИКИТИНА", "АЛЛА", "1939", "КМС", "", ""),
                        Competitor(Group("М14"), "ТИХОМИРОВ", "ИВАН", "2007", "", "", ""),
                        Competitor(Group("М14"), "ЖЕЛЕЗНЫЙ", "МИХАИЛ", "2007", "", "", "")
                    )
                ), Team(
                    "ПИТЕР",
                    listOf(Competitor(Group("VIP"), "ПУПКИН", "ВАСЯ", "2013", "КМС", "", ""))
                ), Team(
                    "МОСКВА",
                    listOf(Competitor(Group("М14"), "ПУПКИН", "ВАСЯ", "2013", "КМС", "", ""))
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