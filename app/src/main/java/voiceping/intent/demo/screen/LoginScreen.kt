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
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.smartwalkie.voicepingintent.User
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
        currentUserStateFlow = MutableStateFlow(User("", "")),
        username = MutableStateFlow("username"),
        password = MutableStateFlow("password"),
        onLogoutClicked =  { },
        onLoginClicked = { _, _ -> run {} },
    )
}

@Composable
fun LoginScreen(
    codeViewModel: CodeViewModel,
    currentUserStateFlow: StateFlow<User>,
    username: MutableStateFlow<String>,
    password: MutableStateFlow<String>,
    onLogoutClicked: () -> Unit,
    onLoginClicked: (String, String) -> Unit
) {
    val code = codeViewModel.code.asStateFlow()
    val focusManager = LocalFocusManager.current

    Column(modifier = Modifier.padding(12.dp)) {
        CodeText(code = code.collectAsState().value)
        Spacer(modifier = Modifier.weight(1.0f))

        val updateCode = {
            codeViewModel.code.value = codeViewModel.getLoginIntentCode(username.value, password.value)
        }

        TextButton(onClick = {
            codeViewModel.code.value = CodeViewModel.RECEIVE_USER
        }, contentPadding = PaddingValues(0.dp)) {
            Text(text = "User: ${currentUserStateFlow.collectAsState().value.username} (click to get the code)")
        }

        val usernameState = username.collectAsState()
        UsernameTextField(
            focusManager = focusManager,
            provideText = { usernameState.value },
            onTextChange = {
                username.value = it
            }
        )

        Spacer(modifier = Modifier.padding(3.dp))

        val passwordState = password.collectAsState()
        PasswordTextField(
            focusManager = focusManager,
            provideText = { passwordState.value },
            onTextChange = { password.value = it }
        )

        Spacer(modifier = Modifier.padding(3.dp))
        ActionButton(text = "Login") {
            onLoginClicked.invoke(username.value, password.value)
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

@Composable
private fun UsernameTextField(
    focusManager: FocusManager,
    provideText: () -> String,
    onTextChange: (String) -> Unit,
) {
    OutlinedTextField(
        value = provideText(),
        onValueChange = {onTextChange(it)},
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
}

@Composable
private fun PasswordTextField(
    focusManager: FocusManager,
    provideText: () -> String,
    onTextChange: (String) -> Unit,
) {
    OutlinedTextField(
        value = provideText(),
        onValueChange = {
            onTextChange(it)
        },
        label = { Text(text = "Enter your password") },
        placeholder = { Text(text = "Password") },
        maxLines = 1,
        modifier = Modifier
            .fillMaxWidth()
            .onPreviewKeyEvent {
                when {
                    KeyEventType.KeyUp == it.type && Key.Enter == it.key -> {
                        focusManager.clearFocus(true)
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
}