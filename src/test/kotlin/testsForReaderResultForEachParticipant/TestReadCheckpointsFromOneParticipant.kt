package testsForReaderResultForEachParticipant

import parsers.readCheckpointsFromOneParticipant
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

import classes.Time
import parsers.readSingleTeamFromFile
import kotlin.test.assertFailsWith

internal class TestReadCheckpointsFromOneParticipant {
    private val dirPath = "testData/testDataFolderReaderResultForEachParticipant/testReadCheckpointsFromOneParticipant/"

    @Test
    fun testSimpleApplication() {
        val checkpointsForParticipant = readCheckpointsFromOneParticipant(dirPath + "testSimpleApplication.csv")
        assertEquals("243", checkpointsForParticipant.personNumber)
        val need = mapOf(
            "1km" to Time("12:06:15"),
            "2km" to Time("12:10:36"),
            "Finish" to Time("12:14:51"),
        )
        assertContentEquals(need.toList(), checkpointsForParticipant.timeMatching.toList())
    }

    @Test
    fun testNoRecordsApplication() {
        assertFailsWith<IllegalArgumentException> { readSingleTeamFromFile(dirPath + "testNoRecordsApplication.csv") }
    }

    @Test
    fun testNoCheckpoints() {
        val checkpointsForParticipant = readCheckpointsFromOneParticipant(dirPath + "testNoCheckpoints.csv")
        assertEquals("abc def", checkpointsForParticipant.personNumber)
        val need = mapOf<String, Time>()
        assertContentEquals(need.toList(), checkpointsForParticipant.timeMatching.toList())
    }

    @Test
    fun testEmptyParticipantNumber() {
        assertFailsWith<IllegalArgumentException> { readSingleTeamFromFile(dirPath + "testEmptyParticipantNumber.csv") }
    }

}