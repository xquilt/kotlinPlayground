import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.*
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

import androidx.compose.ui.window.MenuBar;

@Composable
fun menuBareConfiguration() { }

fun main() = application {
    var userConnected by remember { mutableStateOf("Untitled") }
    Window(
        onCloseRequest = ::exitApplication,
        title = "${userConnected} - User"
    ) {
        App()
        MenuBar {
            Menu("File") {
                Item("New Window", onClick = {
                    println()
                })
            }
        }

    }
}

@Preview
@Composable
fun App() {
    EntireView()
}