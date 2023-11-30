package voiceping.intent.demo.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.smartwalkie.voicepingintent.ProcessorState
import com.smartwalkie.voicepingintent.Voiceping
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import voiceping.intent.demo.CodeViewModel
import voiceping.intent.demo.ui.theme.codeFontStyle
import voiceping.intent.demo.view.ActionButton
import voiceping.intent.demo.view.CodeText

@Preview
@Composable
fun StartStopPTTScreenPreview() {
    StartStopPTTScreen(code = MutableStateFlow("The code"), processorState = MutableStateFlow(ProcessorState.StateIdle)) {

    }
}

@Composable
fun StartStopPTTScreen(
    code : StateFlow<String>,
    processorState: StateFlow<ProcessorState>,
    updateCode: (String) -> Unit
) {
    Column(modifier = Modifier.padding(12.dp)) {
        CodeText(code.collectAsState().value)
        Spacer(Modifier.weight(1f))

        val processorStateValue = when (processorState.collectAsState().value) {
            is ProcessorState.StateIdle -> "-"
            is ProcessorState.StateRecording -> "Sending message"
            is ProcessorState.StatePlaying -> "Playing..."
        }

        BasicTextField(value = "Status: $processorStateValue", onValueChange = {}, textStyle = codeFontStyle, readOnly = true)
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