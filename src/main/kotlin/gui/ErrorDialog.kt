package gui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.AlertDialog
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import logger

object MyErrorDialog {
    private val openDialog = mutableStateOf(true)
    var exception: Exception? = null

    fun set(e: Exception) {
        exception = e
        openDialog.value = true
    }

    @Composable
    fun show() {
        exception?.let {
            logger.debug { "catch exception $exception" }

            openDialog.value = true
            alertDialog(it.message)
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    private fun alertDialog(message: String?) {
        if (openDialog.value) {

            AlertDialog(
                modifier = Modifier.wrapContentSize(Alignment.Center).width(550.dp),
                backgroundColor = Color.Red,
                onDismissRequest = {
                    openDialog.value = false
                    exception = null
                },
                confirmButton = {
                    Text(
                        message ?: "something went wrong",
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            )
        }
    }
}


