package classes

import kotlinx.serialization.Serializable


@Serializable
class Config(
    val applicationsFolder: String,
    val sortitionFolder: String,
    val participantsFolder: String,
    val checkpointsFolder: String,
    val resultsInTeams: String,
    val resultsInGroups: String,
    val checkPoints: List<String>
) {

    private fun normalizePath(path: String) = path.dropLastWhile { it == '/' }.plus("/")

    fun normalize() = Config(
        normalizePath(applicationsFolder),
        normalizePath(sortitionFolder),
        normalizePath(participantsFolder),
        normalizePath(checkpointsFolder),
        normalizePath(resultsInTeams),
        normalizePath(resultsInGroups),
        checkPoints
    )
}