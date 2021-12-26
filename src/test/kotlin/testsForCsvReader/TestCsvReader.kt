package testsForCsvReader

import org.junit.Test
import readers.CsvReader
import readers.CsvReaderEmptyFileException


internal class TestCsvReader {
    private val testDataDir = "testData/testDataForCsvReader/"

    @Test
    fun `empty test`() {
        val path = testDataDir + "empty.csv"
        val reader = CsvReader(path)
        try {
            reader.readAll()
        } catch (e: Exception) {
            assert(false)
        }
        try {
            reader.readFirstElementInFirstRow()
        } catch (e: Exception) {
            assert(e is CsvReaderEmptyFileException)
        }
        try {
            reader.readFirstRow()
        } catch (e: Exception) {
            assert(e is CsvReaderEmptyFileException)
        }
    }

    @Test
    fun `regular test`() {
        val path = testDataDir + "regular.csv"
        val reader = CsvReader(path)
        assert(reader.readFirstRow() == listOf("This", "is", "simple"))
        assert(
            reader.readAll() ==
                    listOf(
                        listOf("This", "is", "simple"),
                        listOf("Just", "read", "and"),
                        listOf("300", "hundred", "bucks"),
                        listOf("2", "u", "2")
                    )
        )
        assert(reader.readFirstElementInFirstRow() == "This")
    }

    @Test
    fun `single row test`() {
        val row = listOf("kek", "lol", "test", "cringe")
        val path = testDataDir + "singleRow.csv"
        val reader = CsvReader(path)
        assert(reader.readFirstRow() == row)
        assert(reader.readAll() == listOf(row))
        assert(reader.readFirstElementInFirstRow() == row[0])
    }
}