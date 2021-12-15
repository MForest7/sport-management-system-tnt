package parsers

import classes.Config
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.File


class JsonReader(private val fileName: String) {
    fun read(): Config = Json.decodeFromString<Config>(File(fileName).readText()).normalize()
}

