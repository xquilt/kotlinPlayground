//package ui.EntireView

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun EntireView() {

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

        val coroutineIOScope = CoroutineScope(Dispatchers.IO)
        IconButton(
            onClick = {
                coroutineIOScope.launch {
                    sendPostRequest(
                        // TODO: The user should be able to pass on arguments.
                        sourceCode = sourceCode,
                        arguments = "",
                        successCallback = {
                            it?.apply {
                                sourceCodeOutput = if(this.body()?.text?.isNotEmpty() ?: true) this.body()?.text?.formatCodeOutput() ?: "" else this.body()?.exception?.parseOutput()?.joinToString("\n") ?: ""
                            }
                        },
                        failureCallback = {}
                    )
                }
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


@Preview
@Composable
fun EntireViewPreview() {
    EntireView()
}