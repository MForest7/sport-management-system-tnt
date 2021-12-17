package gui

import DB
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Colors
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import logger
import startShell


enum class MyButtons {
    NONE, LOAD_CONFIG, APPLICATIONS, TIME_PASSING, RESULTS_GROUPS, RESULTS_TEAMS, ADD_COMPETITOR, DELETE_COMPETITOR,
    SELECT_ROW, SORTED_BY_COLUMN, CLEAN_ALL
}

fun mySort(data: List<MutableList<String>>, index: Int): List<MutableList<String>> {
    if (data.find { it[index].toIntOrNull() == null } == null) {
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

    fun updateButton(button: MyButtons) {
        lastPressedButton.value = MyButtons.NONE
        lastPressedButton.value = button
    }

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
            val sortedData = mySort(database.getAllCompetitors(columns).map { it.toMutableList() }, columnToSort.value)
            val table = Table(columns, sortedData, database)
            table.drawTable(0.dp, 80.dp, changedSelectionState.value, selected.value, columnToSort, lastPressedButton)

            println(selected)

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.padding(8.dp).offset(y = 40.dp)
            ) {
                Button(onClick = {
                    lastPressedButton.value = MyButtons.ADD_COMPETITOR
                }) {
                    Text("Add competitor")
                }

                Button(colors = ButtonDefaults.buttonColors(backgroundColor = if (changedSelectionState.value) Color.Red else Color.Blue),
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
            TODO()
        }
        MyButtons.RESULTS_GROUPS -> {
            TODO()
        }
        MyButtons.RESULTS_TEAMS -> {
            TODO()
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
            println("delete")
            println(changedSelectionState)
            println(selected)
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
}