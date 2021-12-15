import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.singleWindowApplication
import gui.sportManagerSystemApp


fun main() = singleWindowApplication(
    title = "Code Viewer",
    state = WindowState(width = 1280.dp, height = 768.dp)
) {
    sportManagerSystemApp(this.window)
}
