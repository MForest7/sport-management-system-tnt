package classes

class Time(private val time: String) : Comparable<Time> {
    override operator fun compareTo(other: Time) = time.compareTo(other.time)

    private val intRepresentation: Int
        get() = time.split(':').map { it.toInt() }.let { it[0]*3600 + it[1]*60 + it[2] }

    constructor(value: Int) : this(
        listOf<Int>(
            value % 60, (value / 60) % 60, value / 3600
        ).joinToString(":") { it.toString().padStart(2, '0') }
    )

    operator fun minus(other: Time) = Time(intRepresentation - other.intRepresentation)
}