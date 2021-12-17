package gui

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
    NONE, LOAD_CONFIG, INFORMATION, TIME_PASSING
}

@Composable
fun sportManagerSystemApp(window: ComposeWindow) {
    logger.info { "App is started" }

    val myFileChooser = MyFileChooser(window)
    val myErrorDialog = MyErrorDialog()
    val exception = remember { mutableStateOf<Exception?>(null) }
    val lastPressedButton = remember { mutableStateOf(MyButtons.NONE) }
    val table = Table()

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
            lastPressedButton.value = MyButtons.INFORMATION
        }) {
            Text("Information about participants")
        }

        Button(onClick = {
            lastPressedButton.value = MyButtons.TIME_PASSING
        }) {
            Text("Time of passing distances")
        }

        Button(onClick = {
            try {
                TODO()
            } catch (e: Exception) {
                exception.value = Exception(e.message)
            }
        }) {
            Text("Results in groups")
        }

        Button(onClick = {
            try {
                TODO()
            } catch (e: Exception) {
                exception.value = Exception(e.message)
            }
        }) {
            Text("Results in teams")
        }

    }

    when (lastPressedButton.value) {
        MyButtons.LOAD_CONFIG -> {
            try {
                startShell(myFileChooser.pickFile("json"))
            } catch (e: Exception) {
                exception.value = Exception(e.message)
            }
            lastPressedButton.value = MyButtons.NONE
        }
        MyButtons.INFORMATION -> {
            table.drawTable(
                listOf("1 col", "2 col", "3 col"),
                listOf(listOf("kek", "abobus", "lolis"), listOf("", "amogus", "lol"))
            )
        }
        MyButtons.TIME_PASSING -> {
            table.drawTable(listOf("kek col", "lol col"), listOf(listOf("KEK", "ABOBUS"), listOf("AMOGUS", "LOL")))
        }
    }

    myErrorDialog.show(exception.value)
    logger.info { "App is ended" }
}