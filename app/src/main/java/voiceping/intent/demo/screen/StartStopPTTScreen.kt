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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import voiceping.intent.demo.CodeViewModel
import voiceping.intent.demo.view.ActionButton
import voiceping.intent.demo.view.CodeText

@Preview
@Composable
fun StartStopPTTScreenPreview() {
    StartStopPTTScreen(MutableStateFlow("The code")) {

    }
}

@Composable
fun StartStopPTTScreen(
    code : StateFlow<String>,
    updateCode: (String) -> Unit
) {
    val context = LocalContext.current.applicationContext
    Column(modifier = Modifier.padding(12.dp)) {
        CodeText(code.collectAsState().value)
        Spacer(Modifier.weight(1f))
        ActionButton(text = "Start PTT") {
            Voiceping.action.startPTT()
            updateCode(CodeViewModel.START_PTT_CODE.trim())
        }
        ActionButton(text = "Stop PTT") {
            Voiceping.action.stopPTT()
            updateCode(CodeViewModel.STOP_PTT_CODE.trim())
        }
    }
}