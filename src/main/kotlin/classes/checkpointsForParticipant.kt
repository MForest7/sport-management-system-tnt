package classes

data class CheckpointForParticipantRecord(val nameCheckPoint: String, val time: Time)

data class CheckpointsForParticipant(
    val personNumber: String,
    val timeMatching: List<CheckpointForParticipantRecord>
)