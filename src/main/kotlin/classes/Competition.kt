package classes

import parsers.IncompleteCheckpoint
import parsers.readListOfIncompleteCheckpointsFromDirectoryWithParticipantsResults

class Competition(
    val checkpoints: List<CheckPoint>,
    val competitors: List<CompetitorInCompetition>,
    val numberMatching: Map<String, CompetitorInCompetition>
) {
    fun readListOfCheckpointsFromDirectoryWithParticipantsResults(dir: String): List<CheckPoint> {
        val incompleteCheckpoints =
            readListOfIncompleteCheckpointsFromDirectoryWithParticipantsResults(dir, checkpoints.map { it.name })

        return incompleteCheckpoints.map { convertIncompleteCheckpointToCheckpoint(it) }
    }

    private fun convertIncompleteCheckpointToCheckpoint(incomplete: IncompleteCheckpoint): CheckPoint {
        return CheckPoint(incomplete.name, incomplete.timeMatching.mapKeys {
            val a = numberMatching[it.key]
            require(a != null) { "Participant with number ${it.key} is undefined" }
            a
        })
    }
}