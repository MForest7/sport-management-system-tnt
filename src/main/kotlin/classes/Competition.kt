package classes

class Competition(
    val checkpoints: MutableList<CheckPoint>,
    val competitors: List<CompetitorInCompetition>,
    val numberMatching: Map<String, CompetitorInCompetition>
) {
    var start = checkpoints.first()
    val notFinished = mutableSetOf<CompetitorInCompetition>()
    val timeMatching = mutableMapOf<CompetitorInCompetition, Time>()

    fun setCheckpointsFromIncomplete(incompleteCompetition: IncompleteCompetition) {
        val checkpoints = incompleteCompetition.checkpoints.map {
            it.convertIncompleteToCheckpoint(numberMatching)
        }.toMutableList()
        this.checkpoints.addAll(checkpoints)
        start = this.checkpoints.first()
    }
}

