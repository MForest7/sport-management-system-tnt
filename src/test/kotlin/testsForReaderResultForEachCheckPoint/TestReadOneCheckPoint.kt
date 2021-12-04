package testsForReaderResultForEachCheckPoint


import classes.Time
import parsers.readListOfIncompleteCheckpointsFromDirectoryWithCheckPointsResults
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class TestReadOneCheckPoint {
    private val dirPath = "testData/testDataFolderReaderResultForEachCheckpoint/testReadOneCheckPoint/"

    @Test
    fun testSimpleApplication() {
        val checkPointNames = listOf("1km")
        val checkPoint =
            readListOfIncompleteCheckpointsFromDirectoryWithCheckPointsResults(
                dirPath,
                checkPointNames
            )
        assert(checkPoint.size == 1)
        assertEquals("1km", checkPoint[0].name)
        val need = mapOf(
            "241" to Time("12:04:17"),
            "242" to Time("12:05:11"),
            "243" to Time("12:06:15"),
        )
        assertContentEquals(need.toList(), checkPoint[0].timeMatching.toList())
    }
}