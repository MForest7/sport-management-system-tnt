package parsers

import classes.*
import com.github.doyaaaaaken.kotlincsv.client.CsvFileReader
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import logger
import java.lang.Exception

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
        val checkpoints = convertRecordsToTimeMatching(csvReader.readAllExceptFirst())
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
        val countOfCheckpoints = checkpointNames.size

        val checkpointsForParticipant = readUnmerged()

        checkParticipantsUnique(checkpointsForParticipant)

        val checkpoints = List(countOfCheckpoints) { index ->
            val checkpointName = checkpointNames[index]
            val timeMatching = mutableMapOf<String, Time>()
            checkpointsForParticipant.forEach { participant ->
                participant.timeMatching[checkpointName]?.let { time ->
                    timeMatching[participant.personNumber] = time
                }
            }
            IncompleteCheckpoint(checkpointName, timeMatching)
        }
        return IncompleteCompetition(checkpoints)

    }
}
