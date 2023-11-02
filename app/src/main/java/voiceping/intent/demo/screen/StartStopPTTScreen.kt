package voiceping.intent.demo.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.asStateFlow
import voiceping.intent.demo.CodeViewModel
import com.smartwalkie.voicepingintent.VoicepingIntentSender
import voiceping.intent.demo.view.ActionButton
import voiceping.intent.demo.view.CodeText

@Preview
@Composable
fun StartStopPTTScreenPreview() {
    StartStopPTTScreen(intentSender = VoicepingIntentSender(), codeViewModel = CodeViewModel())
}

@Composable
fun StartStopPTTScreen(
    intentSender: VoicepingIntentSender,
    codeViewModel: CodeViewModel,
) {
    val context = LocalContext.current.applicationContext
    Column(modifier = Modifier.padding(12.dp)) {
        val code = codeViewModel.code.asStateFlow()

        CodeText(code.collectAsState().value)
        Spacer(Modifier.weight(1f))
        ActionButton(text = "Start PTT") {
            intentSender.startPTT(context)
            codeViewModel.code.value = CodeViewModel.START_PTT_CODE.trim()
        }
        ActionButton(text = "Stop PTT") {
            intentSender.stopPTT(context)
            codeViewModel.code.value = CodeViewModel.STOP_PTT_CODE.trim()
        }
    }
}