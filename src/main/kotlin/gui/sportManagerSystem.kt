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

@Composable
fun sportManagerSystemApp(window: ComposeWindow) {
    logger.info { "App is started" }

    val myFileChooser = MyFileChooser(window)
    val myErrorDialog = MyErrorDialog()
    val exception = remember { mutableStateOf<Exception?>(null) }

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth().padding(8.dp)
    ) {
        Button(onClick = {
            try {
                startShell(myFileChooser.pickFile("json"))
            } catch (e: Exception) {
                exception.value = Exception(e.message)
            }
        }) {
            Text("Load config")
        }


        Button(onClick = {
            try {
                TODO()
            } catch (e: Exception) {
                exception.value = Exception(e.message)
            }
        }) {
            Text("Information about participants")
        }

        Button(onClick = {
            try {
                TODO()
            } catch (e: Exception) {
                exception.value = Exception(e.message)
            }
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

    myErrorDialog.show(exception.value)
    logger.info { "App is ended" }
}