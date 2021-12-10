package classes

import kotlinx.serialization.Serializable

enum class Mode {
    SORTITION, RESULTS_TEAMS, RESULTS_GROUPS
}

enum class TypeOfSplits {
    PARTICIPANTS, CHECKPOINTS
}

@Serializable
class Config(
    val applicationsFolder: String,
    val sortitionFolder: String,
    val splitsFolder: String,
    val resultsInTeams: String,
    val resultsInGroups: String,
    val typeOfSplits: TypeOfSplits,
    val checkPoints: List<String>
) {

    private fun normalizePath(path: String) = path.dropLastWhile { it == '/' }.plus("/")

    fun normalize() = Config(
        normalizePath(applicationsFolder),
        normalizePath(sortitionFolder),
        normalizePath(splitsFolder),
        normalizePath(resultsInTeams),
        normalizePath(resultsInGroups),
        typeOfSplits,
        checkPoints
    )
}