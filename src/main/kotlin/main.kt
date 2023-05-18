import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

import androidx.compose.ui.window.MenuBar;

@Preview
@Composable
fun App() {

    var sourceCode: String by remember { mutableStateOf(String()) }
    var sourceCodeOutput by remember { mutableStateOf(String()) }

    Row (
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {

        TextField(
            value = sourceCode,
            onValueChange = { sourceCode = it },
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
        )

        IconButton(
            onClick = {
                sendPostRequest(
                    sourceCode = getSourceCode(sourceCodeFile = ""),
                    arguments = "",
                    successCallback = {
                        it?.apply {
                            println(this.text)
                            sourceCodeOutput = this.text
                        }
                    },
                    failureCallback = {}
                )
            }
        ) {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = null
            )
        }

        Text(
            text = sourceCodeOutput,
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
        )

    }

}

@Composable
fun menuBareConfiguration() {
}

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