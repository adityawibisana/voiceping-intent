package voiceping.intent.demo.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.smartwalkie.voicepingintent.CurrentChannel
import com.smartwalkie.voicepingintent.Voiceping
import kotlinx.coroutines.flow.asStateFlow
import voiceping.intent.demo.CodeViewModel
import com.smartwalkie.voicepingintent.VoicepingIntentSender
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import voiceping.intent.demo.view.ActionButton
import voiceping.intent.demo.view.CodeText

@Preview
@Composable
fun ChannelScreenPreview() {
    ChannelScreen(codeViewModel = CodeViewModel(), MutableStateFlow(CurrentChannel("Testing", 0)))
}

@Composable
fun ChannelScreen(codeViewModel: CodeViewModel,
                  currentChannel: StateFlow<CurrentChannel>) {
    val context = LocalContext.current.applicationContext

    val code = codeViewModel.code.asStateFlow()
    var searchResult by remember { mutableStateOf("") }

    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    Column(modifier = Modifier.padding(12.dp)) {
        CodeText(code = code.collectAsState().value)
        Spacer(modifier = Modifier.weight(1.0f))

        TextButton(onClick = {
            codeViewModel.code.value = CodeViewModel.CURRENT_CHANNEL
        }, contentPadding = PaddingValues(0.dp)) {
            Text(text = "Channel: ${currentChannel.collectAsState().value.name} (click to get the code)")
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = searchResult,
                onValueChange = {
                    searchResult = it
                    Voiceping.action.searchChannel(it)
                    codeViewModel.code.value = codeViewModel.getSearchChannelIntentCode(it)
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
            Voiceping.action.goToPrevChannel()
            codeViewModel.code.value = CodeViewModel.PREV_CHANNEL_CODE
        }
        ActionButton(text = "Next Channel") {
            Voiceping.action.goToNextChannel()
            codeViewModel.code.value = CodeViewModel.NEXT_CHANNEL_CODE
        }
    }
}