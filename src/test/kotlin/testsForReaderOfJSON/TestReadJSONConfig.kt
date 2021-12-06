package testsForReaderOfJSON

import classes.Config
import classes.Mode
import parsers.readJSONConfig
import kotlin.test.Test
import kotlin.test.assertEquals

internal class TestReadJSONConfig {
    @Test
    fun testReadJSONConfig() {
        assertEquals(
            Config(
                mode = Mode.SORTITION,
                _applicationsFolder = "testData/testsDataFolderForReaderOfTeamsFromApplications/testReadListOfTeamsFromDirectory/testThreeApplications/",
                _sortitionFolder = "/",
                _splitsFolder = "/",
                typeOfSplits = null,
                _resultsInTeams = "",
                _resultsInGroups = "",
                checkPoints = listOf("1km"),
            ),
            readJSONConfig("testData/testJSONReader/config.json")
        )
    }
}