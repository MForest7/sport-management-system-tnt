package classes

class Competition(
    val checkpoints: List<CheckPoint>,
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
        checkpoints.forEach { checkPoint ->
            val image = this.checkpoints.find { it.name == checkPoint.name }
            if (image != null) {
                checkPoint.timeMatching.forEach { competitor, times ->
                    val oldValue = image.timeMatching.getOrDefault(competitor, listOf())
                    image.timeMatching[competitor] = (oldValue + times).sorted().distinct().toMutableList()
                }
            }
        }
    }
}

