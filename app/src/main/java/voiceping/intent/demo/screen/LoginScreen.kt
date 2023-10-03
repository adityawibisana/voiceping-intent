package voiceping.intent.demo.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.asStateFlow
import voiceping.intent.demo.CodeViewModel
import voiceping.intent.demo.VoicepingIntentSender
import voiceping.intent.demo.receivers.SyncFinishedReceiver
import voiceping.intent.demo.view.ActionButton
import voiceping.intent.demo.view.CodeText

@Preview
@Composable
fun LoginScreenPreview()  {
    LoginScreen(intentSender = VoicepingIntentSender(),
        codeViewModel = CodeViewModel(),
        syncFinishedReceiver = SyncFinishedReceiver()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    intentSender: VoicepingIntentSender,
    codeViewModel: CodeViewModel,
    syncFinishedReceiver: SyncFinishedReceiver
) {
    val context = LocalContext.current.applicationContext
    val code = codeViewModel.code.asStateFlow()
    val focusManager = LocalFocusManager.current

    intentSender.getCurrentUser(context = context)

    Column(modifier = Modifier.padding(12.dp)) {
        CodeText(code = code.collectAsState().value, context = context)
        Spacer(modifier = Modifier.weight(1.0f))

        var username by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        val updateCode = {
            codeViewModel.code.tryEmit(codeViewModel.getLoginIntentCode(username, password))
        }

        TextButton(onClick = {
            codeViewModel.code.tryEmit(CodeViewModel.RECEIVE_USER)
        }) {
            Text(text = "User: ${syncFinishedReceiver.usernameStateFlow.collectAsState().value}")
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
                .padding(12.dp)
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
                .padding(12.dp)
                .onPreviewKeyEvent {
                    when {
                        KeyEventType.KeyUp == it.type && Key.Enter == it.key -> {
                            intentSender.login(context, username, password)
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

        Spacer (modifier = Modifier.padding(6.dp))

        ActionButton(text = "Login") {
            intentSender.login(
                context = context,
                username = username,
                password = password)
            updateCode()
        }
    }
}