package testsForReaderResultForEachCheckPoint


import competition.checkpoints.CheckpointsResultsReader
import competition.checkpoints.IncompleteCheckPointRecord
import competition.checkpoints.IncompleteCheckpoint
import competition.time.Time
import kotlin.test.Test
import kotlin.test.assertContentEquals


class TestMultipleCheckPoints {
    private val dirPath = "testData/testDataFolderReaderResultForEachCheckpoint/testReadMultipleCheckPoints/"

    @Test
    fun testSimpleApplication() {
        val checkPointNames = listOf("1km", "5km", "10km")
        val checkPoints =
            CheckpointsResultsReader(
                dirPath,
                checkPointNames
            ).read()
        assert(checkPoints.checkpoints.size == 3)
        val need = listOf(
            IncompleteCheckpoint(
                "5km", listOf(
                    IncompleteCheckPointRecord("23", Time("14:00:21")),
                    IncompleteCheckPointRecord("41", Time("05:10:00")),
                    IncompleteCheckPointRecord("10", Time("01:11:01")),
                )
            ), IncompleteCheckpoint(
                "1km", listOf(
                    IncompleteCheckPointRecord("1", Time("14:00:21")),
                    IncompleteCheckPointRecord("2", Time("05:10:00")),
                    IncompleteCheckPointRecord("3", Time("01:11:01"))
                )
            ), IncompleteCheckpoint(
                "10km", listOf(
                    IncompleteCheckPointRecord("322", Time("12:00:21")),
                    IncompleteCheckPointRecord("1123", Time("00:00:00")),
                    IncompleteCheckPointRecord("1", Time("01:11:01"))
                )
            )
        )

        assertContentEquals(need.sortedBy { it.name }, checkPoints.checkpoints.sortedBy { it.name })
    }
}