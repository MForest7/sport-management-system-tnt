package parsers

import classes.Config
import kotlinx.serialization.decodeFromString
import java.io.File
import kotlinx.serialization.json.Json

private fun normalizePath(path: String?): String {
    return if (path?.isEmpty() == true) "/" else if (path?.last() == '/') path else "$path/"
}

private fun normalizeConfig(config: Config): Config {
    val mode = config.mode
    val applicationsFolder = normalizePath(config.applicationsFolder)
    val sortitionFolder = normalizePath(config.sortitionFolder)
    val splitsFolder = normalizePath(config.splitsFolder)
    val typeOfSplits = config.typeOfSplits
    require(mode == "Sortition" || mode == "Results in teams" || mode == "Results in groups") {"Unknown mode"}

    return Config(
        mode,
        applicationsFolder,
        sortitionFolder,
        splitsFolder,
        typeOfSplits,
        config.resultsInTeams,
        config.resultsInGroups,
        config.checkPoints
    )
}

fun readJSONConfig(fileName: String): Config = normalizeConfig(Json.decodeFromString(File(fileName).readText()))