package voiceping.intent.demo.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.asStateFlow
import voiceping.intent.demo.CodeViewModel
import voiceping.intent.demo.VoicepingIntentSender
import voiceping.intent.demo.view.ActionButton
import voiceping.intent.demo.view.CodeText

@Preview
@Composable
fun LoginScreenPreview()  {
    LoginScreen(intentSender = VoicepingIntentSender(), codeViewModel = CodeViewModel())
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(intentSender: VoicepingIntentSender, codeViewModel: CodeViewModel) {
    val context = LocalContext.current.applicationContext
    val code = codeViewModel.code.asStateFlow()

    Column {
        CodeText(code = code.collectAsState().value, context = context)
        Spacer(modifier = Modifier.weight(1.0f))

        var username by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        val updateCode = {
            codeViewModel.code.tryEmit(codeViewModel.getLoginIntentCode(username, password))
        }

        TextField(
            value = username,
            onValueChange = {
                username = it
                updateCode()
            },
            label = { Text(text = "Enter your username") },
            placeholder = { Text(text = "Username") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        )

        TextField(
            value = password,
            onValueChange = {
                password = it
                updateCode()
            },
            label = { Text(text = "Enter your password") },
            placeholder = { Text(text = "Password") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        )

        ActionButton(text = "Login") {
            intentSender.login(
                context = context,
                username = username,
                password = password)
            updateCode()
        }
    }
}