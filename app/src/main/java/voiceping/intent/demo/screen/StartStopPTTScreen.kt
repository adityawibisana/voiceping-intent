package voiceping.intent.demo.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.flow.asStateFlow
import voiceping.intent.demo.CodeViewModel
import voiceping.intent.demo.VoicepingIntentSender
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
    Column(modifier = Modifier.fillMaxSize()) {
        val code = codeViewModel.code.asStateFlow()

        CodeText(code.collectAsState().value, context = context)
        Spacer(Modifier.weight(1f))
        ActionButton(text = "Start PTT") {
            intentSender.startPTT(context)
            codeViewModel.code.tryEmit("""
                Intent().run { 
                setPackage("com.media2359.voiceping.store")
                action = "android.intent.action.PTT.down"
                context.sendBroadcast(this)
            }
            """)
        }
        ActionButton(text = "Stop PTT") {
            intentSender.stopPTT(context)
            codeViewModel.code.tryEmit("""
                Intent().run {
                setPackage("com.media2359.voiceping.store")
                action = "android.intent.action.PTT.up"
                context.sendBroadcast(this)
            }
            """)
        }
    }
}