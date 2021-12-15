package gui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.offset
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.unit.dp
import logger
import startShell

@Composable
@Preview
fun sportManagerSystemApp(window: ComposeWindow) {
    logger.info { "App is started" }

    val myFileChooser = MyFileChooser(window)

    MaterialTheme {
        Button(onClick = {
            startShell(myFileChooser.pickFile("json"))
        }, modifier = Modifier.offset(x = 10.dp, y = 10.dp)) {
            Text("Load config.json and start executing")
        }
    }


    logger.info { "App is ended" }
}