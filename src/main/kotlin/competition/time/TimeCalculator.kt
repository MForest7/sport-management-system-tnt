package writers

import competition.Competition
import competition.checkpoints.CheckPoint
import competition.competitors.CompetitorInCompetition
import competition.time.Time

interface TimeCalculator {
    fun getTime(competition: Competition, competitor: CompetitorInCompetition): Time?
}

object AllCheckpointsCalculator : TimeCalculator {
    override fun getTime(competition: Competition, competitor: CompetitorInCompetition): Time? {
        data class CheckPointWithCount(val checkPoint: CheckPoint, val next: Iterator<Time>?)

        val checkPointsOrder = competitor.group.checkPointNames
            .map { name -> competition.checkpoints.find { it.name == name } }
            .map { checkPoint -> checkPoint ?: return null }
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
