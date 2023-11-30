package voiceping.intent.demo.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.smartwalkie.voicepingintent.HealthStatus
import com.smartwalkie.voicepingintent.ProcessorState
import com.smartwalkie.voicepingintent.Voiceping
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import voiceping.intent.demo.CodeViewModel
import voiceping.intent.demo.ui.theme.codeFontStyle
import voiceping.intent.demo.ui.theme.codeFontStyleError
import voiceping.intent.demo.ui.theme.codeFontStyleOk
import voiceping.intent.demo.view.ActionButton
import voiceping.intent.demo.view.CodeText

@Preview
@Composable
fun StartStopPTTScreenPreview() {
    StartStopPTTScreen(
        code = MutableStateFlow("The code"),
        healthState = MutableStateFlow(HealthStatus.VoicepingReady),
        processorState = MutableStateFlow(ProcessorState.StateIdle)) {
    }
}

@Composable
fun StartStopPTTScreen(
    code : StateFlow<String>,
    processorState: StateFlow<ProcessorState>,
    healthState: StateFlow<HealthStatus>,
    updateCode: (String) -> Unit
) {
    Column(modifier = Modifier.padding(12.dp)) {
        CodeText(code.collectAsState().value)
        Spacer(Modifier.weight(1f))

        var processorTextStyle : TextStyle = codeFontStyle
        val processorStateValue = when (val currentProcessorState = processorState.collectAsState().value) {
            is ProcessorState.StateIdle -> "-"
            is ProcessorState.StateRecording -> {
                processorTextStyle = codeFontStyleOk
                "Sending message. To:${currentProcessorState.to} Type:${if (currentProcessorState.type == 0) "group" else "private"}"
            }
            is ProcessorState.StatePlaying -> {
                processorTextStyle = codeFontStyleOk
                "Playing. From:${currentProcessorState.from} To: ${currentProcessorState.to} Type: ${if (currentProcessorState.type == 0) "Group" else "Private"}."
            }
        }

        var healthTextStyle = codeFontStyleError
        val healthStateValue = when (val currentHealthStateFlow = healthState.collectAsState().value) {
            is HealthStatus.VoicepingIsNotConnected -> currentHealthStateFlow.message
            is HealthStatus.VoicepingIsNotInstalled -> currentHealthStateFlow.message
            is HealthStatus.VoicepingIsNotLoggedIn -> currentHealthStateFlow.message
            is HealthStatus.VoicepingMicPermissionIsNotGranted -> currentHealthStateFlow.message
            is HealthStatus.VoicepingServiceIsNotRunning -> currentHealthStateFlow.message
            HealthStatus.VoicepingReady -> {
                healthTextStyle = codeFontStyleOk
                "Ready"
            }
        }
        
        ClickableText(
            modifier = Modifier.padding(12.dp),
            text = buildAnnotatedString {
                append("Health: $healthStateValue (click to get the code)") },
            style = healthTextStyle) {
            updateCode(CodeViewModel.CURRENT_HEALTH)
        }

        ClickableText(
            modifier = Modifier.padding(12.dp),
            text = buildAnnotatedString {
                append("Processor: $processorStateValue (click to get the code)") },
            style = processorTextStyle) {
            updateCode(CodeViewModel.CURRENT_PROCESSOR)
        }

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