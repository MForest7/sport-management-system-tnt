package classes

import kotlinx.serialization.Serializable

enum class Mode {
    SORTITION, RESULTS_TEAMS, RESULTS_GROUPS
}

@Serializable
data class Config(
    val mode: Mode,
    val applicationsFolder: String?,
    val sortitionFolder: String?,
    val splitsFolder: String?,
    val typeOfSplits: String?,
    val resultsInTeams: String?,
    val resultsInGroups: String?,
    val checkPoints: List<String>?
)