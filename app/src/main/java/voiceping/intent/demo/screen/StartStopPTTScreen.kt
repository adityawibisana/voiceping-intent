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
import com.smartwalkie.voicepingintent.Voiceping
import kotlinx.coroutines.flow.asStateFlow
import voiceping.intent.demo.CodeViewModel
import voiceping.intent.demo.view.ActionButton
import voiceping.intent.demo.view.CodeText

@Preview
@Composable
fun StartStopPTTScreenPreview() {
    StartStopPTTScreen(codeViewModel = CodeViewModel())
}

@Composable
fun StartStopPTTScreen(
    codeViewModel: CodeViewModel,
) {
    val context = LocalContext.current.applicationContext
    Column(modifier = Modifier.padding(12.dp)) {
        val code = codeViewModel.code.asStateFlow()

        CodeText(code.collectAsState().value)
        Spacer(Modifier.weight(1f))
        ActionButton(text = "Start PTT") {
            Voiceping.action.startPTT()
            codeViewModel.code.value = CodeViewModel.START_PTT_CODE.trim()
        }
        ActionButton(text = "Stop PTT") {
            Voiceping.action.stopPTT()
            codeViewModel.code.value = CodeViewModel.STOP_PTT_CODE.trim()
        }
    }
}