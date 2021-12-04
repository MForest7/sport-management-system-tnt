package parsers

import classes.Config
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.File

private fun normalizePath(path: String?): String {
    return if (path?.isEmpty() == true) "/" else if (path?.last() == '/') path else "$path/"
}

private fun normalizeConfig(config: Config): Config {
    val mode = config.mode
    val applicationsFolder = normalizePath(config.applicationsFolder)
    val sortitionFolder = normalizePath(config.sortitionFolder)
    val splitsFolder = normalizePath(config.splitsFolder)
    val typeOfSplits = config.typeOfSplits

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