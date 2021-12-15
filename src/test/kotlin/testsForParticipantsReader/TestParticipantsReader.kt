package testsForParticipantsReader

import classes.IncompleteCheckpoint
import classes.IncompleteCompetition
import classes.Time
import org.junit.Test
import parsers.DirectoryReaderException
import parsers.ParticipantsResultsReader
import parsers.ParticipantsResultsReaderExceptionSameNumber
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

internal class TestParticipantsReader {
    private val dirPath =
        "testData/testDataFolderReaderResultForEachParticipant/testReadListOfIncompleteCheckpointsFromDirectoryWithParticipantsResults/"

    @Test
    fun testThreeParticipants() {
        val listOfCheckpoints = ParticipantsResultsReader(
            dirPath + "testThreeParticipants/", listOf("1km", "2km", "finish")
        ).read()
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
        assertFailsWith<DirectoryReaderException> {
            ParticipantsResultsReader(
                "",
                listOf("1km", "2km")
            ).read()
        }
    }

    @Test
    fun testEmptyDirectory() {
        val listOfCheckpoints = ParticipantsResultsReader(
            dirPath + "testEmptyDirectory/",
            listOf("1km", "2km", "finish")
        ).read()
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
        assertFailsWith<ParticipantsResultsReaderExceptionSameNumber> {
            ParticipantsResultsReader(
                dirPath + "testRepeatedParticipants/",
                listOf("1km", "2km", "Finish")
            ).read()
        }
    }
}