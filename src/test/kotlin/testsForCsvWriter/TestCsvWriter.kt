package testsForCsvWriter

import classes.CsvWriter
import org.junit.Test
import kotlin.test.assertEquals

internal class TestCsvWriter {
    @Test
    fun testRow() {
        val file = createTempFile()
        val writer = CsvWriter(file.absolutePath)
        val row = listOf("Try", "Catch", "Finally")
        writer.writeRow(row)
        val expected = row.joinToString(",")
        val got = file.readText().strip()
        assertEquals(expected, got)
    }

    @Test
    fun testRows() {
        val file = createTempFile("test", ".csv")
        val writer = CsvWriter(file.absolutePath)
        val rows = listOf(
            listOf("Try", "Catch", "Finally"),
            listOf("Lol", "Kek", "Chebureck"),
        )
        writer.writeRows(rows)
        val expected = rows.map { it.joinToString(",") }
        assertEquals(expected, file.readLines())
    }

    @Test
    fun testRewrite() {
        val file = createTempFile()
        val writer = CsvWriter(file.absolutePath)
        val row = listOf("Try", "Catch", "Finally")
        writer.writeRow(row)
        writer.writeRow(row)
        val expected = row.joinToString(",")
        val got = file.readText().strip()
        assertEquals(expected, got)
    }
}