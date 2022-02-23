package testsForReaderResultForEachCheckPoint


import competition.checkpoints.CheckpointsResultsReader
import competition.checkpoints.IncompleteCheckPointRecord
import competition.time.Time
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class TestReadOneCheckPoint {
    private val dirPath = "testData/testDataFolderReaderResultForEachCheckpoint/testReadOneCheckPoint/"

    @Test
    fun testSimpleApplication() {
        val checkPointNames = listOf("1km")
        val checkPoint =
            CheckpointsResultsReader(
                dirPath,
                checkPointNames
            ).read()
        assert(checkPoint.checkpoints.size == 1)
        assertEquals("1km", checkPoint.checkpoints[0].name)
        val need = listOf(
            IncompleteCheckPointRecord("241", Time("12:04:17")),
            IncompleteCheckPointRecord("242", Time("12:05:11")),
            IncompleteCheckPointRecord("243", Time("12:06:15")),
        )
        assertContentEquals(need.toList(), checkPoint.checkpoints[0].timeMatching)
    }
}