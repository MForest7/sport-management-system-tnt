package testsForReaderResultForEachCheckPoint


import classes.Time
import parsers.IncompleteCheckpoint
import parsers.readListOfIncompleteCheckpointsFromDirectoryWithCheckPointsResults
import kotlin.test.Test
import kotlin.test.assertContentEquals


class TestMultipleCheckPoints {
    private val dirPath = "testData/testDataFolderReaderResultForEachCheckpoint/testReadMultipleCheckPoints/"

    @Test
    fun testSimpleApplication() {
        val checkPointNames = listOf("1km", "5km", "10km")
        val checkPoints =
            readListOfIncompleteCheckpointsFromDirectoryWithCheckPointsResults(
                dirPath,
                checkPointNames
            )
        assert(checkPoints.size == 3)
        val need = listOf(
            IncompleteCheckpoint(
                "5km", mapOf(
                    "23" to Time("14:00:21"),
                    "41" to Time("05:10:00"),
                    "10" to Time("01:11:01"),
                )
            ), IncompleteCheckpoint(
                "1km", mapOf(
                    "1" to Time("14:00:21"),
                    "2" to Time("05:10:00"),
                    "3" to Time("01:11:01")
                )
            ), IncompleteCheckpoint(
                "10km", mapOf(
                    "322" to Time("12:00:21"),
                    "1123" to Time("00:00:00"),
                    "1" to Time("01:11:01")
                )
            )
        )

        assertContentEquals(need.sortedBy { it.name }, checkPoints.sortedBy { it.name })
    }
}