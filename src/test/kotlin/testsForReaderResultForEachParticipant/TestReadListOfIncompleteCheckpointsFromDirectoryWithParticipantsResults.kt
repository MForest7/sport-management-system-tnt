package testsForReaderResultForEachParticipant

import classes.Time
import classes.IncompleteCheckpoint
import classes.IncompleteCompetition
import parsers.readListOfIncompleteCheckpointsFromDirectoryWithParticipantsResults
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

internal class ReadListOfIncompleteCheckpointsFromDirectoryWithParticipantsResults {
    private val dirPath =
        "testData/testDataFolderReaderResultForEachParticipant/testReadListOfIncompleteCheckpointsFromDirectoryWithParticipantsResults/"

    @Test
    fun testThreeParticipants() {
        val listOfCheckpoints = readListOfIncompleteCheckpointsFromDirectoryWithParticipantsResults(
            dirPath + "testThreeParticipants/",
            listOf("1km", "2km", "finish")
        )
        val competition = IncompleteCompetition(
            listOf(
                IncompleteCheckpoint(
                    "1km", mapOf(
                        "first" to Time("12:00:00"),
                        "third" to Time("12:01:06"),
                    )
                ),
                IncompleteCheckpoint(
                    "2km", mapOf(
                        "first" to Time("13:05:06"),
                        "second" to Time("00:00:00"),
                    )
                ),
                IncompleteCheckpoint(
                    "finish", mapOf()
                )
            )
        )

        assertEquals(
            competition.checkpoints, listOfCheckpoints.checkpoints
        )
    }

    @Test
    fun testDirectoryDoesntExist() {
        assertFailsWith<IllegalArgumentException> {
            readListOfIncompleteCheckpointsFromDirectoryWithParticipantsResults(
                "",
                listOf("1km", "2km")
            )
        }
    }

    @Test
    fun testEmptyDirectory() {
        val listOfCheckpoints = readListOfIncompleteCheckpointsFromDirectoryWithParticipantsResults(
            dirPath + "testEmptyDirectory/",
            listOf("1km", "2km", "finish")
        )
        val competition = IncompleteCompetition(
            listOf(
                IncompleteCheckpoint("1km", mapOf()),
                IncompleteCheckpoint("2km", mapOf()),
                IncompleteCheckpoint("finish", mapOf()),
            )
        )
        assertEquals(
            competition.checkpoints, listOfCheckpoints.checkpoints
        )
    }


    @Test
    fun testRepeatedParticipants() {
        assertFailsWith<IllegalArgumentException> {
            readListOfIncompleteCheckpointsFromDirectoryWithParticipantsResults(
                dirPath + "testRepeatedParticipants/",
                listOf("1km", "2km", "Finish")
            )
        }
    }

}