package voiceping.intent.demo.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.asStateFlow
import voiceping.intent.demo.CodeViewModel
import com.smartwalkie.voicepingintent.VoicepingIntentSender
import voiceping.intent.demo.view.ActionButton
import voiceping.intent.demo.view.CodeText

@Preview
@Composable
fun ChannelScreenPreview() {
    ChannelScreen(intentSender = VoicepingIntentSender(), codeViewModel = CodeViewModel())
}

@Composable
fun ChannelScreen(intentSender: VoicepingIntentSender,
                  codeViewModel: CodeViewModel) {
    val context = LocalContext.current.applicationContext

    val code = codeViewModel.code.asStateFlow()
    var searchResult by remember { mutableStateOf("") }

    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    Column(modifier = Modifier.padding(12.dp)) {
        CodeText(code = code.collectAsState().value, context = context)
        Spacer(modifier = Modifier.weight(1.0f))

        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = searchResult,
                onValueChange = {
                    searchResult = it
                    intentSender.searchChannel(context = context, it)
                    codeViewModel.code.tryEmit(codeViewModel.getSearchChannelIntentCode(it))
                },
                label = { Text(text = "Search a channel") },
                placeholder = { Text(text = "Type anything, eg: 'channel1'") },
                singleLine = true,
                modifier = Modifier
                    .weight(1f)
                    .padding(0.dp, 0.dp, 0.dp, 8.dp)
                    .focusRequester(focusRequester)
            )
            Button(onClick = {
                focusManager.clearFocus(true)
            }, modifier = Modifier.padding(12.dp, 0.dp, 0.dp, 0.dp)) {
                Text(text = "Search")
            }
        }

        ActionButton(text = "Prev Channel") {
            intentSender.goToPrevChannel(context)
            codeViewModel.code.tryEmit(CodeViewModel.PREV_CHANNEL_CODE)
        }
        ActionButton(text = "Next Channel") {
            intentSender.goToNextChannel(context)
            codeViewModel.code.tryEmit(CodeViewModel.NEXT_CHANNEL_CODE)
        }
    }
}