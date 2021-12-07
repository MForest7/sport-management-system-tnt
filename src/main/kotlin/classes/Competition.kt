package classes

import parsers.IncompleteCheckpoint
import parsers.readListOfIncompleteCheckpointsFromDirectoryWithCheckPointsResults
import parsers.readListOfIncompleteCheckpointsFromDirectoryWithParticipantsResults

class Competition(
    val checkpoints: MutableList<CheckPoint>,
    val competitors: List<CompetitorInCompetition>,
    val numberMatching: Map<String, CompetitorInCompetition>
) {
    var start = checkpoints.first()
    var finish = checkpoints.last()

    fun setCheckpointsFromIncomplete(incompleteCheckpoints: List<IncompleteCheckpoint>) {
        val checkpoints = incompleteCheckpoints.map(::convertIncompleteToCheckpoint).toMutableList()
        checkpoints.add(0, this.checkpoints[0])
        this.checkpoints = checkpoints
        start = this.checkpoints.first()
        finish = this.checkpoints.last()
    }

    private fun convertIncompleteToCheckpoint(incomplete: IncompleteCheckpoint): CheckPoint = CheckPoint(
        incomplete.name,
        incomplete.timeMatching.map { (number, time) ->
            val competitorWithNumber = numberMatching[number]
            require(competitorWithNumber != null) { "Dont have competitor with number $number" }
            competitorWithNumber to time
        }.toMap().toMutableMap()
    )

    fun loadResults(config: Config) {
        val listOfCheckpointNames = config.checkPoints
        require(config.splitsFolder != null) { "splits folder is null" }
        require(listOfCheckpointNames != null) { "no checkpoints" }
        require(config.typeOfSplits != null) { "type of splits is null" }
        this.setCheckpointsFromIncomplete(
            when (config.typeOfSplits) {
                TypeOfSplits.PARTICIPANTS -> readListOfIncompleteCheckpointsFromDirectoryWithParticipantsResults(
                    config.splitsFolder,
                    listOfCheckpointNames
                )
                TypeOfSplits.CHECKPOINTS -> readListOfIncompleteCheckpointsFromDirectoryWithCheckPointsResults(
                    config.splitsFolder,
                    listOfCheckpointNames
                )
            }
        )
    }
}

