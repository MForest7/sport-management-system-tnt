package gui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

enum class Tabs(val content: GUI.Tab) {
    CHEBUPELI(GUI.Tab("CHEBUPELI") @Composable {
        androidx.compose.foundation.layout.Box(
            androidx.compose.ui.Modifier.size(500.dp, 500.dp).background(androidx.compose.ui.graphics.Color.Cyan)
        ) {  }
    }),
    CHEBUPIZZA(GUI.Tab("CHEBUPIZZA") @Composable {
        androidx.compose.foundation.layout.Box(
            androidx.compose.ui.Modifier.padding(start = 600.dp).size(500.dp, 500.dp).background(androidx.compose.ui.graphics.Color.Red)
        ) {  }
    }),
    CHEBUREK(GUI.Tab("CHEBUREK", nextTabsEnum = listOf(CHEBUPELI, CHEBUPIZZA)) @Composable {
        kotlin.io.println("chebupeli")
    }),
    KEK(GUI.Tab("kek") @Composable {
        kotlin.io.println("lol kek cheburek")
    }),
    LOL(GUI.Tab("LOL", nextTabsEnum = listOf(CHEBUREK)) @Composable {
        androidx.compose.foundation.layout.Box(
            androidx.compose.ui.Modifier.size(400.dp, 400.dp).background(Color.Blue)
        ) { }
    }),
    MAIN(GUI.Tab(nextTabsEnum = listOf(KEK, LOL)) {
    }),
}