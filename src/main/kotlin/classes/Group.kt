package classes

data class Group(
    val name: String,
    val checkPointNames: List<String>,
    val calculator: TimeCalculator = AllCheckpointsCalculator
)