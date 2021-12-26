import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.singleWindowApplication
import gui.GUI
import mu.KotlinLogging

val logger = KotlinLogging.logger { }


fun main() = singleWindowApplication(
    title = "Code Viewer",
    state = WindowState(width = 1280.dp, height = 768.dp)
) {
    GUI.run(window)
}
