package classes

class Competition(
    val checkpoints: MutableList<CheckPoint>,
    val competitors: List<CompetitorInCompetition>,
    val numberMatching: Map<String, CompetitorInCompetition>
) {
    var start = checkpoints.first()
    var finish = checkpoints.last()

    fun setCheckpointsFromIncomplete(incompleteCheckpoints: List<IncompleteCheckpoint>) {
        val checkpoints = incompleteCheckpoints.map {
            it.convertIncompleteToCheckpoint(numberMatching)
        }.toMutableList()
        this.checkpoints.addAll(checkpoints)
        start = this.checkpoints.first()
        finish = this.checkpoints.last()
    }
}

