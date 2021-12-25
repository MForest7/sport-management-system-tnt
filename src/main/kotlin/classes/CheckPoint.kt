package classes

data class CheckPoint(
    val name: String = "",
    val timeMatching: Map<CompetitorInCompetition, MutableList<Time>>
)

data class IncompleteCheckPointRecord(val number: String, val time: Time)

data class IncompleteCheckpoint(val name: String, val timeMatching: List<IncompleteCheckPointRecord>) {
    fun convertIncompleteToCheckpoint(numberMatching: Map<String, CompetitorInCompetition>): CheckPoint {
        val timeAndCountMatching = timeMatching.groupBy { (number, _) -> number }
            .map { (number, records) -> numberMatching[number] to records.map { it.time }.sorted() }
            .filterIsInstance<Pair<CompetitorInCompetition, MutableList<Time>>>().toMap()
        return CheckPoint(this.name, timeAndCountMatching)
    }
}


class IncompleteCompetition(val checkpoints: List<IncompleteCheckpoint>) {
    operator fun plus(other: IncompleteCompetition): IncompleteCompetition {
        val checkpointNames = (this.checkpoints.map { it.name } + other.checkpoints.map { it.name }).distinct()
        return IncompleteCompetition(checkpointNames.map { checkpointName ->
            var timeMatching = mutableListOf<IncompleteCheckPointRecord>()
            val incompleteCheckpoint1 = this.checkpoints.find { it.name == checkpointName }
            val incompleteCheckpoint2 = other.checkpoints.find { it.name == checkpointName }
            if (incompleteCheckpoint1 != null) {
                timeMatching = incompleteCheckpoint1.timeMatching.toMutableList()
            }
            incompleteCheckpoint2?.timeMatching?.forEach { (number, time) ->
                val anotherRecord = timeMatching.find { it.number == number }?.time
                if (anotherRecord != null && anotherRecord != time) {
                    throw IllegalStateException("Wrong data in results")
                }
                timeMatching.add(IncompleteCheckPointRecord(number, time))
            }
            IncompleteCheckpoint(checkpointName, timeMatching.distinct())
        })
    }
}
