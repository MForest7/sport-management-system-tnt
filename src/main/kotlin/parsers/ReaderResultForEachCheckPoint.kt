package parsers


import classes.IncompleteCheckpoint
import com.github.doyaaaaaken.kotlincsv.client.CsvFileReader
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import logger

private fun CsvFileReaderWithFileName.getCheckpointNameFromRecord(record: List<String>?): String =
    getFirstFieldFromRecord(record)

private fun CsvFileReaderWithFileName.createCheckpointFromFile(): IncompleteCheckpoint {
    val checkpointName = getCheckpointNameFromRecord(readSingleRecord())
    val timeMatching = convertRecordsToTimeMatching(readAllRemainingRecords())
    logger.debug { "Checkpoint name = $checkpointName, competitors time = $timeMatching" }
    return IncompleteCheckpoint(checkpointName, timeMatching)
}

private fun readCheckpoint(fileName: String): IncompleteCheckpoint {
    val createCheckpoint = { csvFileReader: CsvFileReader ->
        CsvFileReaderWithFileName(fileName, csvFileReader).createCheckpointFromFile()
    }
    val checkpoint = csvReader().open(fileName, createCheckpoint)
    logger.debug { "$checkpoint from file($fileName)" }
    return checkpoint
}

private fun checkCheckPoints(checkpointNames: List<String>, checkpoints: List<IncompleteCheckpoint>) {
    checkpoints.forEach { checkpoint ->
        require(checkpoint.name in checkpointNames) { "${checkpoint.name} not in allowed checkpoint names." }
    }
    require(checkpoints.distinctBy { checkpoint -> checkpoint.name } == checkpoints) {
        "One or more checkpoint are repeated in several files"
    }
}

fun readListOfIncompleteCheckpointsFromDirectoryWithCheckPointsResults(
    dir: String,
    checkpointNames: List<String>
): List<IncompleteCheckpoint> {
    val checkpoints = getMappedListOfFilesFromDir(dir, ::readCheckpoint)
    checkCheckPoints(checkpointNames, checkpoints)
    return checkpoints
}
