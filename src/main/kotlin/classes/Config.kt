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
    val mode: Mode,
    val applicationsFolder: String,
    val sortitionFolder: String,
    val splitsFolder: String,
    val resultsInTeams: String,
    val resultsInGroups: String,
    val typeOfSplits: TypeOfSplits,
    val checkPoints: List<String>
) {

    private fun normalizedPath(path: String) = path.dropLastWhile { it == '/' }.plus("/")

    fun normalized() = Config(
        mode,
        normalizedPath(applicationsFolder),
        normalizedPath(sortitionFolder),
        normalizedPath(splitsFolder),
        normalizedPath(resultsInTeams),
        normalizedPath(resultsInGroups),
        typeOfSplits,
        checkPoints
    )
}