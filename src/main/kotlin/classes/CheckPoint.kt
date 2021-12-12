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
        if (this.checkpoints.map { it.name }.intersect(other.checkpoints.map { it.name }.toSet()).isNotEmpty()) {
            throw(IllegalArgumentException("Results intersects"))
        }
        return IncompleteCompetition(this.checkpoints + other.checkpoints)
    }
}
