package classes

class Time(val time: String) : Comparable<Time> {
    override operator fun compareTo(other: Time) = time.compareTo(other.time)

    val intRepresentation: Int
        get() = time.split(':').map { it.toInt() }.let { it[0]*3600 + it[1]*60 + it[2] }

    constructor(value: Int) : this(
        listOf<Int>(value / 3600, (value / 60) % 60, value % 60)
            .joinToString(":") { it.toString().padStart(2, '0') }
    )

    operator fun minus(other: Time) = Time(intRepresentation - other.intRepresentation)
}