package classes

import kotlinx.serialization.Serializable

@Serializable
data class Config(
    val mode: String,
    val applications: String?,
    val splits: String?,
    val results: String?,
    val checkPoints: List<String>
)