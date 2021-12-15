package classes

data class CheckPoint(
    val name: String,
    val timeMatching: MutableMap<CompetitorInCompetition, Time>
)

data class IncompleteCheckpoint(val name: String, val timeMatching: Map<String, Time>) {
    fun convertIncompleteToCheckpoint(numberMatching: Map<String, CompetitorInCompetition>) =
        CheckPoint(
            this.name,
            this.timeMatching.map { (number, time) ->
                val competitorWithNumber = numberMatching[number]
                require(competitorWithNumber != null) { "Dont have competitor with number $number" }
                competitorWithNumber to time
            }.toMap().toMutableMap()
        )
}


class IncompleteCompetition(val checkpoints: List<IncompleteCheckpoint>) {
    operator fun plus(other: IncompleteCompetition): IncompleteCompetition {
        val checkpointNames = (this.checkpoints.map { it.name } + other.checkpoints.map { it.name }).distinct()
        return IncompleteCompetition(checkpointNames.map { checkpointName ->
            var timeMatching = mutableMapOf<String, Time>()
            val incompleteCheckpoint1 = this.checkpoints.find { it.name == checkpointName }
            val incompleteCheckpoint2 = other.checkpoints.find { it.name == checkpointName }
            if (incompleteCheckpoint1 != null) {
                timeMatching = incompleteCheckpoint1.timeMatching.toMutableMap()
            }
            incompleteCheckpoint2?.timeMatching?.forEach { (number, time) ->
                val anotherRecord = timeMatching[number]
                if (anotherRecord != null && anotherRecord != time) {
                    throw IllegalStateException("Wrong data in results")
                }
                timeMatching[number] = time
            }
            IncompleteCheckpoint(checkpointName, timeMatching)
        })
    }
}
