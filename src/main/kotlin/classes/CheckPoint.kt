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

fun mergeResultsTogether(
    list1: List<IncompleteCheckpoint>,
    list2: List<IncompleteCheckpoint>
): List<IncompleteCheckpoint> {
    if (list1.map { it.name }.intersect(list2.map { it.name }.toSet()).isNotEmpty()) {
        throw(IllegalArgumentException("Results intersects"))
    }
    return list1 + list2
}