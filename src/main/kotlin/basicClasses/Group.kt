package basicClasses

import classes.AllCheckpointsCalculator
import classes.TimeCalculator

data class Group(
    val name: String,
    val checkPointNames: List<String>,
    val calculator: TimeCalculator = AllCheckpointsCalculator
)