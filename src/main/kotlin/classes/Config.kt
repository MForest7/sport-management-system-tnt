package classes

import kotlinx.serialization.Serializable

@Serializable
data class Config(
    val mode: String,
    val applicationsFolder: String?,
    val sortitionFolder: String?,
    val splitsFolder: String?,
    val typeOfSplits: String?,
    val resultsInTeams: String?,
    val resultsInGroups: String?,
    val checkPoints: List<String>?
)