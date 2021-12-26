package readers

import basicClasses.CheckpointsForParticipant
import basicClasses.IncompleteCheckPointRecord
import basicClasses.IncompleteCheckpoint
import basicClasses.IncompleteCompetition
import logger

open class ParticipantsResultsReaderException(message: String) :
    Exception(message)

class ParticipantsResultsReaderExceptionSameNumber(message: String) :
    ParticipantsResultsReaderException(message)

class ParticipantsResultsReader(
    override val dir: String,
    private val checkpointNames: List<String>
) : DirectoryReader<CheckpointsForParticipant, IncompleteCompetition> {

    override fun readUnit(csvReader: CsvReader):
            CheckpointsForParticipant {
        val personNumber = csvReader.readFirstElementInFirstRow()
        val checkpoints = convertRecordsToParticipantRecords(csvReader.readAllExceptFirst())
        logger.debug { "Person number = $personNumber, checkpoints = $checkpoints" }
        val checkpointsForParticipant = CheckpointsForParticipant(personNumber, checkpoints)
        logger.debug { "CheckPoint($checkpointsForParticipant) from file(${csvReader.filename})" }
        return checkpointsForParticipant
    }

    private fun checkParticipantsUnique(participants: List<CheckpointsForParticipant>) {
        val countEntries = participants.groupingBy { it.personNumber }.eachCount()
        val mostOccurringElement = countEntries.maxByOrNull { it.value }
        if ((mostOccurringElement?.value ?: 0) > 1) {
            throw ParticipantsResultsReaderExceptionSameNumber(
                mostOccurringElement!!.key
            )
        }
    }

    override fun read(): IncompleteCompetition {
        val checkpointsForParticipant = readUnmerged()

        checkParticipantsUnique(checkpointsForParticipant)

        val checkpoints = checkpointNames.associateWith { mutableListOf<IncompleteCheckPointRecord>() }

        checkpointsForParticipant.forEach { participant ->
            participant.timeMatching.forEach { record ->
                require (checkpoints[record.nameCheckPoint] != null) { "Unknown checkpoint: ${record.nameCheckPoint}" }
                checkpoints[record.nameCheckPoint]?.add(IncompleteCheckPointRecord(participant.personNumber, record.time))
            }
        }

        return IncompleteCompetition(checkpoints.map { (name, matching) -> IncompleteCheckpoint(name, matching) })

    }
}
