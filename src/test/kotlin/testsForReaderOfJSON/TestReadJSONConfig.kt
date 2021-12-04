package testsForReaderOfTeamsFromApplications

import classes.Competitor
import classes.Config
import classes.Team
import parsers.readJSONConfig
import parsers.readListOfTeamsFromDirectory
import java.io.File
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

internal class TestReadJSONConfig {
    @Test
    fun testReadJSONConfig() {
        assertEquals(
            Config(
                mode="Sortition",
                applicationsFolder="testData/testsDataFolderForReaderOfTeamsFromApplications/testReadListOfTeamsFromDirectory/testThreeApplications/",
                sortitionFolder="/",
                splitsFolder="/",
                typeOfSplits="",
                resultsInTeams="",
                resultsInGroups="",
                checkPoints=listOf("1km"),
            ),
            readJSONConfig("testData/testJSONReader/config.json")
        )
    }
}