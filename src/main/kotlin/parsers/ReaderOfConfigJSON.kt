package parsers

import classes.Config
import kotlinx.serialization.decodeFromString
import java.io.File
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromJsonElement
import java.io.FileNotFoundException

private fun normalizePath(path: String?): String {
    return if (path?.isEmpty() == true) "/" else if (path?.last() == '/') path else "$path/"
}

private fun normalizeConfig(config: Config): Config {
    val mode = config.mode
    val applications = normalizePath(config.applications)
    val splits = normalizePath(config.splits)
    val results = normalizePath(config.results)

    when (mode) {
        "Sortition" -> {
            require(File(applications).isDirectory) { "Directory $applications for application does not exist" }
        }
        "Competition" -> {
            require(File(splits).isDirectory) { "Directory $splits for splits does not exist" }
            createDir(results)
        }
        else -> {
            throw IllegalArgumentException("Mode $mode is not defined. Use Mode 'Sortition' or 'Competition'")
        }
    }

    return Config(mode, applications, splits, results, config.checkPoints)
}

fun readJSONConfig(fileName: String) : Config = normalizeConfig(Json.decodeFromString(File(fileName).readText()))