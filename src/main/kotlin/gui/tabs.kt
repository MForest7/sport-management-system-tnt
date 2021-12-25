package gui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

val CHEBUPELI = Tab.Builder("CHEBUPELI")
    .withContent @Composable {
        Box(
            Modifier.size(500.dp, 500.dp).background(Color.Cyan)
        ) { }
    }
    .build()

val CHEBUPIZZA = Tab.Builder("CHEBUPIZZA")
    .withContent @Composable {
        Box(
            Modifier.padding(start = 600.dp).size(500.dp, 500.dp).background(Color.Red)
        ) { }
    }
    .build()

val CHEBUREK = Tab.Builder("CHEBUREK")
    .withTabs(listOf(CHEBUPELI, CHEBUPIZZA))
    .withContent @Composable {
        println("chebupeli")
    }
    .build()

/*val KEK = TabWithTable(
    name = "kek",
    table = Table(mutableListOf("aboba", "amogus", "sus"), "")
)

val LOL = Tab.Builder("LOL")
    .withTabs(listOf(CHEBUREK))
    .build()

val MAIN = Tab.Builder("MAIN")
    .withTabs(listOf(KEK, LOL))
    .build()
*/