package classes

import kotlinx.serialization.Serializable


@Serializable
data class Config(
    val checkPoints: List<String>,
    val groups: Map<String, List<String>>
)
