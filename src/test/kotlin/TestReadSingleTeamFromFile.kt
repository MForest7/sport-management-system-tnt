import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

internal class TestReadSingleTeamFromFile {
    private val dirPath = "testData/testReadSingleTeamFromFile/"

    @Test
    fun testSimpleApplication() {
        val team = readSingleTeamFromFile(dirPath + "testSimpleApplication.csv")
        assertEquals("ПСКОВ,РУСЬ", team.name)
        val need = listOf(
            Competitor(Group("VIP"), "НИКИТИН", "ВАЛЕНТИН", "1941", "", "", ""),
            Competitor(Group("VIP"), "НИКИТИНА", "АЛЛА", "1939", "КМС", "1 группа крови", "СОГАЗ МЕД"),
            Competitor(Group("М14"), "ТИХОМИРОВ", "ИВАН", "2007", "", "", ""),
            Competitor(Group("М14"), "ЖЕЛЕЗНЫЙ", "МИХАИЛ", "2007", "", "", "")
        )
        assertContentEquals(need, team.competitors)
    }

    @Test
    fun testNoRecordsApplication() {
        assertFailsWith<IllegalArgumentException> { readSingleTeamFromFile(dirPath + "testNoRecordsApplication.csv") }
    }

    @Test
    fun testNoCompetitors() {
        val team = readSingleTeamFromFile(dirPath + "testNoCompetitors.csv")
        assertEquals("ПСКОВ,РУСЬ", team.name)
        assertContentEquals(listOf(), team.competitors)
    }

    @Test
    fun testEmptyTeamName() {
        assertFailsWith<IllegalArgumentException> { readSingleTeamFromFile(dirPath + "testEmptyTeamName.csv") }
    }

}