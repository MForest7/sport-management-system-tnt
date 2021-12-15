package testsForReaderOfJSON

import classes.Config
import parsers.readJSONConfig
import kotlin.test.Test
import kotlin.test.assertEquals


internal class TestReadJSONConfig {
    @Test
    fun testReadJSONConfig() {
        val expect = Config(
            applicationsFolder = "testData/testsDataFolderForReaderOfTeamsFromApplications/testReadListOfTeamsFromDirectory/testThreeApplications/",
            sortitionFolder = "/",
            participantsFolder = "/participants/",
            checkpointsFolder = "/checkpoints/",
            resultsInTeams = "/teamsresults/",
            resultsInGroups = "/groupsresults",
            checkPoints = listOf("1km"),
        ).normalize()
        val got = readJSONConfig("testData/testJSONReader/config.json")
        assertEquals(expect, got)
    }
}