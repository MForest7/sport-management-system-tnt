package parsers

import classes.AllCheckpointsCalculator
import classes.CheckPoint
import classes.Group

fun readGroups(dataForGroups: Map<String, List<String>>) : List<Group> {
    return dataForGroups.map { (groupName, checkpoints) -> Group(groupName, checkpoints, AllCheckpointsCalculator) }
}