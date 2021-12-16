package gui

import androidx.compose.ui.awt.ComposeWindow
import java.io.File
import javax.swing.JFileChooser
import javax.swing.filechooser.FileNameExtensionFilter

class NoChosenFileException(message: String) : Exception(message)

class MyFileChooser(private val window: ComposeWindow) {
    fun pickFile(extension: String = ""): String {
        val fc = JFileChooser()
        fc.currentDirectory = File(System.getProperty("user.dir"))
        fc.fileFilter = FileNameExtensionFilter("*.$extension", extension)
        fc.showDialog(window, "Attach")
        if (fc.selectedFile == null) {
            throw NoChosenFileException("File was not chosen")
        }
        require(fc.selectedFile.absolutePath.endsWith(extension)) { "Wrong extension of file(expected $extension)" }

        return fc.selectedFile.absolutePath
    }
}