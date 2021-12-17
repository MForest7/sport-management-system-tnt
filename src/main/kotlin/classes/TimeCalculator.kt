package classes

interface TimeCalculator {
    fun getTime(competition: Competition, competitor: CompetitorInCompetition): Time?
}

object AllCheckpointsCalculator : TimeCalculator {
    override fun getTime(competition: Competition, competitor: CompetitorInCompetition): Time? {
        data class CheckPointWithCount(val checkPoint: CheckPoint, val next: Iterator<Time>?)

        val checkPointsOrder = competitor.group.checkPointNames
            .map { name -> competition.checkpoints.find { it.name == name } }.map { checkPoint -> checkPoint ?: return null }
            .map { checkPoint -> CheckPointWithCount(checkPoint, checkPoint.timeMatching[competitor]?.listIterator()) }
        val startTime = competition.start.timeMatching[competitor]?.getOrNull(0) ?: return null
        val finishTime = checkPointsOrder.fold(Time(0)) { time, checkPoint ->
            val nextTime = checkPoint.next?.next() ?: return null
            if (nextTime <= time) return null
            nextTime
        }
        return finishTime - startTime
    }
}

class KCheckpointsCalculator(private val minCheckpoints: Int) : TimeCalculator {
    override fun getTime(competition: Competition, competitor: CompetitorInCompetition): Time? {
        val checkPointsOrder = competitor.group.checkPointNames
            .map { name -> competition.checkpoints.find { it.name == name } }.filterIsInstance<CheckPoint>()
            .filter { checkpoint -> ! checkpoint.timeMatching[competitor].isNullOrEmpty() }
            .sortedBy { checkPoint -> checkPoint.timeMatching[competitor]?.minOf { it } }
        if (checkPointsOrder.size <= minCheckpoints) return null
        return checkPointsOrder[minCheckpoints].timeMatching[competitor]?.minOf { it }
    }
}