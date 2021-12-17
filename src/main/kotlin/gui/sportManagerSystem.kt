package gui

import DB
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.unit.dp
import logger
import startShell


enum class MyButtons {
    NONE, LOAD_CONFIG, APPLICATIONS, TIME_PASSING, RESULTS_GROUPS, RESULTS_TEAMS, ADD_COMPETITOR
}

@Composable
fun sportManagerSystemApp(window: ComposeWindow) {
    logger.info { "App is started" }

    val myFileChooser = remember { MyFileChooser(window) }
    val lastPressedButton = remember { mutableStateOf(MyButtons.NONE) }
    val database = remember { DB("database") }

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
            val table = Table(columns, database.getAllCompetitors(columns), database)
            var changedSelectionState = remember { mutableStateOf(false) }
            table.drawTable(0.dp, 80.dp, changedSelectionState.value)

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.padding(8.dp).offset(y = 40.dp)
            ) {
                Button(onClick = {
                    database.createEmptyCompetitor()
                    updateButton(MyButtons.APPLICATIONS)
                }) {
                    Text("Add competitor")
                }

                Button(onClick = {
                    changedSelectionState.value = !changedSelectionState.value
                    updateButton(MyButtons.APPLICATIONS)
                }) {
                    Text("Delete competitor")
                }

                Button(onClick = {
                    database.cleanCompetitors()
                    updateButton(MyButtons.APPLICATIONS)
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

        MyButtons.NONE -> {}
        else -> TODO()
    }

    MyErrorDialog.show()
    logger.info { "App is ended" }
}