package parsers

import classes.Config
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.File


fun readJSONConfig(fileName: String): Config = Json.decodeFromString<Config>(File(fileName).readText()).normalize()