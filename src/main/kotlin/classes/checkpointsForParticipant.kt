package classes

data class CheckpointsForParticipant(
    val personNumber: String,
    val timeMatching: Map<String, Time>
)