package parsers

import classes.IncompleteCheckpoint
import classes.Time
import com.github.doyaaaaaken.kotlincsv.client.CsvFileReader
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import logger


private fun CsvFileReaderWithFileName.getPersonNumberFromRecord(record: List<String>?): String =
    getFirstFieldFromRecord(record)



data class CheckpointsForParticipant(val personNumber: String, val timeMatching: Map<String, Time>)

private fun CsvFileReaderWithFileName.createCheckpointsFromFile(): CheckpointsForParticipant {
    val personNumber = getPersonNumberFromRecord(readSingleRecord())
    val checkpoints = convertRecordsToTimeMatching(readAllRemainingRecords())
    logger.debug { "Person number = $personNumber, checkpoints = $checkpoints" }
    return CheckpointsForParticipant(personNumber, checkpoints)
}

fun readCheckpointsFromOneParticipant(fileName: String): CheckpointsForParticipant {
    val createCheckpoint = { csvFileReader: CsvFileReader ->
        CsvFileReaderWithFileName(fileName, csvFileReader).createCheckpointsFromFile()
    }
    val checkpointsForParticipant = csvReader().open(fileName, createCheckpoint)
    logger.debug { "CheckPoint($checkpointsForParticipant) from file($fileName)" }
    return checkpointsForParticipant
}


fun readListOfIncompleteCheckpointsFromDirectoryWithParticipantsResults(
    dir: String,
    checkpointNames: List<String>
): List<IncompleteCheckpoint> {
    val countOfCheckpoints = checkpointNames.size

    val checkPointsForParticipant = getMappedListOfFilesFromDir(dir, ::readCheckpointsFromOneParticipant)

    require(checkPointsForParticipant.distinctBy { it.personNumber } == checkPointsForParticipant) {
        "One or more participants are repeated in several files"}

    //merge checkpoints
    val checkpoints = List(countOfCheckpoints) { index ->
        val checkpointName = checkpointNames[index]
        val timeMatching = mutableMapOf<String, Time>()
        checkPointsForParticipant.forEach { participant ->
            participant.timeMatching[checkpointName]?.let { time ->
                timeMatching[participant.personNumber] = time
            }
        }
        IncompleteCheckpoint(checkpointName, timeMatching)
    }
    return checkpoints
}
