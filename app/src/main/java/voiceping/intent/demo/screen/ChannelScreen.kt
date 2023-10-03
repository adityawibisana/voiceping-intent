package voiceping.intent.demo.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
fun ChannelScreenPreview() {
    ChannelScreen(intentSender = VoicepingIntentSender(), codeViewModel = CodeViewModel())
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChannelScreen(intentSender: VoicepingIntentSender,
                  codeViewModel: CodeViewModel) {
    val context = LocalContext.current.applicationContext

    val code = codeViewModel.code.asStateFlow()
    var searchResult by remember { mutableStateOf("") }

    Column {
        CodeText(code = code.collectAsState().value, context = context)
        Spacer(modifier = Modifier.weight(1.0f))

        TextButton(onClick = { }) {
            Text(text = "Current channel: ")
            Text(text = "Is", )
        }

        OutlinedTextField(
            value = searchResult,
            onValueChange = {
                searchResult = it
                intentSender.searchChannel(context = context, it)
                codeViewModel.code.tryEmit(codeViewModel.getSearchChannelIntentCode(it))
            },
            label = { Text(text = "Type to search a channel") },
            placeholder = { Text(text = "Type anything, eg: 'channel1'") },
            maxLines = 1,
            modifier = Modifier
                .fillMaxWidth()
                .absolutePadding(12.dp, 0.dp, 12.dp, 12.dp)
        )

        ActionButton(text = "Prev Channel") {
            intentSender.goToPrevChannel(context)
            codeViewModel.code.tryEmit(CodeViewModel.PREV_CHANNEL_CODE)
        }
        ActionButton(text = "Next Channel") {
            intentSender.goToNextChannel(context)
            codeViewModel.code.tryEmit(CodeViewModel.NEXT_CHANNEL_CODE)
        }
        Spacer(modifier = Modifier.height(12.dp))
    }
}