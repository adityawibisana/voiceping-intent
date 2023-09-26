package voiceping.intent.demo.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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

@Composable
fun ChannelScreen(intentSender: VoicepingIntentSender,
                  codeViewModel: CodeViewModel) {
    val context = LocalContext.current.applicationContext

    val code = codeViewModel.code.asStateFlow()
    Column {
        CodeText(code = code.collectAsState().value, context = context)
        Spacer(modifier = Modifier.weight(1.0f))
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