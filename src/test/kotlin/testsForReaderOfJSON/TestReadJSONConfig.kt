package testsForReaderOfJSON

import classes.Config
import classes.Mode
import classes.TypeOfSplits
import parsers.readJSONConfig
import kotlin.test.Test
import kotlin.test.assertEquals

internal class TestReadJSONConfig {
    @Test
    fun testReadJSONConfig() {
        assertEquals(
            Config(
                mode = Mode.SORTITION,
                applicationsFolder = "testData/testsDataFolderForReaderOfTeamsFromApplications/testReadListOfTeamsFromDirectory/testThreeApplications/",
                sortitionFolder = "/",
                splitsFolder = "/",
                typeOfSplits = TypeOfSplits.PARTICIPANTS,
                resultsInTeams = "",
                resultsInGroups = "",
                checkPoints = listOf("1km"),
            ),
            readJSONConfig("testData/testJSONReader/config.json")
        )
    }
}