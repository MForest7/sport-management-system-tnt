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
    private val _applicationsFolder: String?,
    private val _sortitionFolder: String?,
    private val _splitsFolder: String?,
    val typeOfSplits: TypeOfSplits?,
    val resultsInTeams: String?,
    val resultsInGroups: String?,
    val checkPoints: List<String>?
) {
    val applicationsFolder = normalizePath(_applicationsFolder)
    val sortitionFolder = normalizePath(_sortitionFolder)
    val splitsFolder = normalizePath(_splitsFolder)

    private fun normalizePath(path: String?): String {
        return if (path?.isEmpty() == true) "/" else if (path?.last() == '/') path else "$path/"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Config

        if (mode != other.mode) return false
        if (typeOfSplits != other.typeOfSplits) return false
        if (checkPoints != other.checkPoints) return false
        if (applicationsFolder != other.applicationsFolder) return false
        if (sortitionFolder != other.sortitionFolder) return false
        if (splitsFolder != other.splitsFolder) return false
        if (resultsInTeams != other.resultsInTeams) return false
        if (resultsInGroups != other.resultsInGroups) return false

        return true
    }

    override fun hashCode(): Int {
        var result = mode.hashCode()
        result = 31 * result + (typeOfSplits?.hashCode() ?: 0)
        result = 31 * result + (checkPoints?.hashCode() ?: 0)
        result = 31 * result + applicationsFolder.hashCode()
        result = 31 * result + sortitionFolder.hashCode()
        result = 31 * result + splitsFolder.hashCode()
        result = 31 * result + resultsInTeams.hashCode()
        result = 31 * result + resultsInGroups.hashCode()
        return result
    }
}