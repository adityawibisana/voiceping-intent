package voiceping.intent.demo.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import voiceping.intent.demo.CodeViewModel
import voiceping.intent.demo.view.ActionButton
import voiceping.intent.demo.view.CodeText

@Preview
@Composable
fun LoginScreenPreview()  {
    LoginScreen(
        codeViewModel = CodeViewModel(),
        usernameStateFlow = MutableStateFlow("User1"),
        onLogoutClicked =  { },
        onLoginClicked = { _, _ -> run {} }
    )
}

@Composable
fun LoginScreen(
    codeViewModel: CodeViewModel,
    usernameStateFlow: StateFlow<String>,
    onLogoutClicked: () -> Unit,
    onLoginClicked: (String, String) -> Unit,
) {
    val code = codeViewModel.code.asStateFlow()
    val focusManager = LocalFocusManager.current

    Column(modifier = Modifier.padding(12.dp)) {
        CodeText(code = code.collectAsState().value)
        Spacer(modifier = Modifier.weight(1.0f))

        var username by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        val updateCode = {
            codeViewModel.code.value = codeViewModel.getLoginIntentCode(username, password)
        }

        TextButton(onClick = {
            codeViewModel.code.value = CodeViewModel.RECEIVE_USER
        }, contentPadding = PaddingValues(0.dp)) {
            Text(text = "User: ${usernameStateFlow.collectAsState().value} (click to get the code)")
        }

        OutlinedTextField(
            value = username,
            onValueChange = {
                username = it
                updateCode()
            },
            label = { Text(text = "Enter your username") },
            placeholder = { Text(text = "Username") },
            maxLines = 1,
            modifier = Modifier
                .fillMaxWidth()
                .onPreviewKeyEvent {
                    when {
                        KeyEventType.KeyUp == it.type &&
                                (Key.Tab == it.key || Key.DirectionDown == it.key) -> {
                            focusManager.moveFocus(FocusDirection.Next)
                            true
                        }

                        else -> false
                    }
                }
        )
        Spacer(modifier = Modifier.padding(3.dp))
        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                updateCode()
            },
            label = { Text(text = "Enter your password") },
            placeholder = { Text(text = "Password") },
            maxLines = 1,
            modifier = Modifier
                .fillMaxWidth()
                .onPreviewKeyEvent {
                    when {
                        KeyEventType.KeyUp == it.type && Key.Enter == it.key -> {
                            onLoginClicked.invoke(username, password)
                            true
                        }

                        KeyEventType.KeyUp == it.type && Key.DirectionUp == it.key -> {
                            focusManager.moveFocus(FocusDirection.Previous)
                            true
                        }

                        else -> false
                    }
                }
        )
        Spacer(modifier = Modifier.padding(3.dp))
        ActionButton(text = "Login") {
            onLoginClicked.invoke(username, password)
            updateCode()
        }

        TextButton(onClick = {
            onLogoutClicked.invoke()
            codeViewModel.code.value = codeViewModel.getLogoutIntentCode()
        }) {
            Spacer(modifier = Modifier.weight(1f))
            Text(text = "Logout",
                style = TextStyle(textDecoration = TextDecoration.Underline))
        }
    }
}