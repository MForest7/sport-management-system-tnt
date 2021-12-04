package classes

import kotlinx.serialization.Serializable

enum class Mode {
    SORTITION, RESULTS_TEAMS, RESULTS_GROUPS
}

enum class TypeOfSplits {
    PARTICIPANTS, CHECKPOINTS
}

@Serializable
data class Config(
    val mode: Mode,
    val applicationsFolder: String?,
    val sortitionFolder: String?,
    val splitsFolder: String?,
    val typeOfSplits: TypeOfSplits?,
    val resultsInTeams: String?,
    val resultsInGroups: String?,
    val checkPoints: List<String>?
)