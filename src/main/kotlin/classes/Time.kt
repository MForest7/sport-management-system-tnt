package classes

data class Time(val time: Int) {
    constructor(str: String) : this(str.length)
}