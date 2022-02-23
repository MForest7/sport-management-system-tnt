package competition

import writers.AllCheckpointsCalculator
import writers.TimeCalculator

data class Group(
    val name: String,
    val checkPointNames: List<String>,
    val calculator: TimeCalculator = AllCheckpointsCalculator
)