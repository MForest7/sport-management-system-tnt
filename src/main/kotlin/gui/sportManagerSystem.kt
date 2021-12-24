package gui

import DB
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import classes.CheckPoint
import classes.Team
import logger
import standings.StandingsInTeams
import standings.timeOf
import startShell


/*enum class MyButtons {
    NONE, LOAD_CONFIG, APPLICATIONS, TIME_PASSING, RESULTS_GROUPS, RESULTS_TEAMS, ADD_COMPETITOR, DELETE_COMPETITOR,
    SELECT_ROW, SORTED_BY_COLUMN, CLEAN_ALL, SHOW_TEAM, SHOW_CHECKPOINT
}

fun mySort(data: List<MutableList<String>>, index: Int): List<MutableList<String>> {
    if (data.find { it[index].toIntOrNull() == null } == null) { // only digits
        return data.sortedBy { it[index].toInt() }
    }
    return data.sortedBy { it[index] }
}

@Composable
fun sportManagerSystemApp(window: ComposeWindow) {
    logger.info { "App is started" }

    val myFileChooser = remember { MyFileChooser(window) }
    val lastPressedButton = remember { mutableStateOf(MyButtons.NONE) }
    val database = remember { DB("database") }
    val changedSelectionState = remember { mutableStateOf(false) }
    val selected = remember { mutableStateOf(mutableSetOf<Int>()) }
    val columnToSort = remember { mutableStateOf(0) }
    val teamToShow = remember { mutableStateOf<Team?>(null) }
    val checkpointToShow = remember { mutableStateOf<CheckPoint?>(null) }

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth().padding(8.dp)
    ) {
        Button(onClick = {
            lastPressedButton.value = MyButtons.LOAD_CONFIG
        }) {
            Text("Load config")
        }


        Button(onClick = {
            lastPressedButton.value = MyButtons.APPLICATIONS
        }) {
            Text("Applications")
        }

        Button(onClick = {
            lastPressedButton.value = MyButtons.TIME_PASSING
        }) {
            Text("Time of passing distances")
        }

        Button(onClick = {
            lastPressedButton.value = MyButtons.RESULTS_GROUPS
        }) {
            Text("Results in groups")
        }

        Button(onClick = {
            lastPressedButton.value = MyButtons.RESULTS_TEAMS
        }) {
            Text("Results in teams")
        }

    }

    when (lastPressedButton.value) {
        MyButtons.LOAD_CONFIG -> {
            try {
                startShell(myFileChooser.pickFile("json"))
            } catch (e: Exception) {
                MyErrorDialog.exception = Exception(e.message)
            }
            lastPressedButton.value = MyButtons.NONE
        }

        MyButtons.APPLICATIONS -> {
            val columns = listOf("id", "name", "surname", "birth", "team", "wishGroup", "title")
            val data = database.getCompetition().competitors.map {
                listOf(it.id.toString(), it.name, it.surname, it.birth, it.team.name, it.wishGroup, it.title)
            }
            val sortedData = mySort(data.map { it.toMutableList() }, columnToSort.value)
            val table = Table(columns, sortedData, database)
            table.drawTable(
                0.dp,
                80.dp,
                changedSelectionState.value,
                selected.value,
                columnToSort,
                lastPressedButton,
                true
            )

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.padding(8.dp).offset(y = 40.dp)
            ) {
                Button(onClick = {
                    lastPressedButton.value = MyButtons.ADD_COMPETITOR
                }) {
                    Text("Add competitor")
                }

                Button(colors = ButtonDefaults.buttonColors(backgroundColor = if (changedSelectionState.value) Color.Red else Color.Magenta),
                    onClick = {
                        lastPressedButton.value = MyButtons.DELETE_COMPETITOR
                    }) {
                    Text("Delete competitor")
                }

                Button(onClick = {
                    lastPressedButton.value = MyButtons.CLEAN_ALL
                }) {
                    Text("Clean all")
                }
            }
        }
        MyButtons.TIME_PASSING -> {
            val competition = database.getCompetition()
            val checkpoints = competition.checkpoints.drop(1)
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.padding(8.dp).offset(y = 40.dp)
            ) {
                checkpoints.forEach { checkpoint ->
                    Button(onClick = {
                        lastPressedButton.value = MyButtons.SHOW_CHECKPOINT
                        checkpointToShow.value = checkpoint
                    }) {
                        Text(checkpoint.name)
                    }
                }
            }
            val checkpoint = checkpointToShow.value
            if (checkpoint != null) {
                val columns = listOf("id", "number", "time")
                val data = mutableListOf<List<String>>()
                competition.competitors.forEach { competitor ->
                    checkpoint.timeMatching[competitor]
                    checkpoint.timeMatching[competitor]?.forEach { time ->
                        data.add(listOf(competitor.id.toString(), competitor.number, time.stringRepresentation))
                    }
                }
                val table = Table(columns, data, database)
                table.drawTable(
                    0.dp,
                    80.dp,
                    changedSelectionState.value,
                    selected.value,
                    columnToSort,
                    lastPressedButton,
                    false
                )
            }
        }
        MyButtons.SHOW_CHECKPOINT -> {
            lastPressedButton.value = MyButtons.TIME_PASSING
        }
        MyButtons.RESULTS_GROUPS -> {
            TODO()
        }
        MyButtons.RESULTS_TEAMS -> {
            val competition = database.getCompetition()
            StandingsInTeams(competition)
            val teams = competition.competitors.map { it.team }.distinct()
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.padding(8.dp).offset(y = 40.dp)
            ) {
                teams.forEach { team ->
                    Button(onClick = {
                        lastPressedButton.value = MyButtons.SHOW_TEAM
                        teamToShow.value = team
                    }) {
                        Text(team.name)
                    }
                }
            }
            val team = teamToShow.value
            if (team != null) {
                val columns = listOf("id", "number", "name", "surname", "time")
                val data = competition.competitors.filter { it.team == team }.map {
                    listOf(
                        it.id.toString(),
                        it.number,
                        it.name,
                        it.surname,
                        competition.timeOf(it)?.stringRepresentation ?: "DNF"
                    )
                }
                val table = Table(columns, data, database)
                table.drawTable(
                    0.dp,
                    80.dp,
                    changedSelectionState.value,
                    selected.value,
                    columnToSort,
                    lastPressedButton,
                    false
                )
            }

        }
        MyButtons.SHOW_TEAM -> {
            lastPressedButton.value = MyButtons.RESULTS_TEAMS
        }
        MyButtons.ADD_COMPETITOR -> {
            database.createEmptyCompetitor()
            lastPressedButton.value = MyButtons.APPLICATIONS
        }
        MyButtons.DELETE_COMPETITOR -> {
            changedSelectionState.value = !changedSelectionState.value
            if (!changedSelectionState.value) {
                selected.value.forEach {
                    database.deleteCompetitor(it)
                }
                selected.value.clear()
            }
            lastPressedButton.value = MyButtons.APPLICATIONS
        }
        MyButtons.SELECT_ROW -> {
            lastPressedButton.value = MyButtons.APPLICATIONS
        }
        MyButtons.SORTED_BY_COLUMN -> {
            lastPressedButton.value = MyButtons.APPLICATIONS
        }
        MyButtons.CLEAN_ALL -> {
            database.cleanCompetitors()
            lastPressedButton.value = MyButtons.APPLICATIONS
        }

        MyButtons.NONE -> {}
        else -> TODO()
    }

    MyErrorDialog.show()
    logger.info { "App is ended" }
}*/