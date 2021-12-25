package classes

class Rules(val checkpoints: List<CheckPoint>, val groups: List<Group>) {
    constructor(groups: List<Group>) : this(
        checkpoints = groups.flatMap { it.checkPointNames }.distinct().map { CheckPoint(it, mutableMapOf()) },
        groups = groups
    )
}