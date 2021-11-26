package classes

class Time(val time: Int) : Comparable<Time> {
    override operator fun compareTo(other: Time) = time.compareTo(other.time)

    val stringRepresentation: String
        get() = listOf<Int>(time / 3600, (time / 60) % 60, time % 60)
            .joinToString(":") { it.toString().padStart(2, '0') }

    val formatToGap: String
        get() = "+" + stringRepresentation.take(4).dropWhile { (it == '0') or (it == ':') } + stringRepresentation.drop(4)

    constructor(string: String) : this(
        string.split(':').map { it.toInt() }.let { it[0]*3600 + it[1]*60 + it[2] }
    )

    operator fun minus(other: Time) = Time(time - other.time)
}
