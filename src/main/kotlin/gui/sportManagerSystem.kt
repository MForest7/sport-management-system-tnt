package gui

import DB
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
    NONE, LOAD_CONFIG, APPLICATIONS, TIME_PASSING, RESULTS_GROUPS
}

@Composable
fun sportManagerSystemApp(window: ComposeWindow) {
    logger.info { "App is started" }

    val myFileChooser = MyFileChooser(window)
    val lastPressedButton = remember { mutableStateOf(MyButtons.NONE) }
    val table = Table()
    val database = remember { DB("database") }



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
            table.drawTable(columns, database.getAllCompetitors(columns), database)
        }
        MyButtons.TIME_PASSING -> {
            TODO()
        }
        MyButtons.RESULTS_GROUPS -> {
            TODO()
        }
        MyButtons.NONE -> {}
        else -> TODO()
    }

    MyErrorDialog.show()
    logger.info { "App is ended" }
}