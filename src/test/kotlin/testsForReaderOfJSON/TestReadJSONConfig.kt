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
                applications="testData/testReadListOfTeamsFromDirectory/testThreeApplications/",
                splits="/",
                results="/",
                checkPoints=listOf()
            ),
            readJSONConfig("testData/testJSONReader/config.json")
        )
    }
}