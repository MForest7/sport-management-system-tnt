package parsers


import classes.CsvReader
import classes.IncompleteCheckpoint
import classes.IncompleteCompetition
import logger


class CheckpointsResultsReader(
    override val dir: String,
    private val checkpointNames: List<String>
) : DirectoryReader<IncompleteCheckpoint, IncompleteCompetition> {
    override fun readUnit(csvReader: CsvReader): IncompleteCheckpoint {
        val checkpointName = csvReader.readFirstElementInFirstRow()
        val timeMatching = convertRecordsToCheckpointRecords(csvReader.readAllExceptFirst())
        logger.debug { "Checkpoint name = $checkpointName, competitors time = $timeMatching" }
        val checkpoint = IncompleteCheckpoint(checkpointName, timeMatching)
        logger.debug { "$checkpoint from file(${csvReader.filename})" }
        return checkpoint
    }

    private fun checkCheckPoints(checkpoints: List<IncompleteCheckpoint>) {
        // TODO: change to exceptions
        checkpoints.forEach { checkpoint ->
            require(checkpoint.name in checkpointNames) { "${checkpoint.name} not in allowed checkpoint names." }
        }
        require(checkpoints.distinctBy { checkpoint -> checkpoint.name } == checkpoints) {
            "One or more checkpoint are repeated in several files"
        }
    }

    override fun read(): IncompleteCompetition {
        val checkpoints = readUnmerged()
        checkCheckPoints(checkpoints)
        return IncompleteCompetition(checkpoints)
    }
}
