package voiceping.intent.demo

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import voiceping.intent.demo.ui.theme.VoicepingIntentDemoTheme
import voiceping.intent.demo.ui.theme.codeFontStyle


class MainActivity : ComponentActivity() {
    private val viewModel = MainActivityViewModel()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VoicepingIntentDemoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier
                        .fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun ActionButton(text: String, startPTT:() -> Unit) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        onClick = {
        startPTT.invoke()
    }) {
        Text(text = text)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CodeText(code: String, context: Context) {
    Column {
        Text(
            text = "Code:",
            modifier = Modifier.padding(12.dp).clickable {
                val clipboard = context.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager?
                val clip = ClipData.newPlainText("source code", code)
                clipboard!!.setPrimaryClip(clip)
            })
        TextField(value = code.trimIndent(), onValueChange = {}, textStyle = codeFontStyle, readOnly = true)
        }
}

@Preview
@Composable
fun MainScreen(
    @PreviewParameter(MainActivityViewModelPreviewParameterProvider::class)  viewModel: MainActivityViewModel) {
    val context = LocalContext.current.applicationContext
    Column(modifier = Modifier.fillMaxSize()) {
        var code by remember { mutableStateOf("Press button to get the source code") }

        CodeText(code, context = context)
        Spacer(Modifier.weight(1f))
        ActionButton(text = "Start PTT") {
            viewModel.startPTT(context)
            code = """
                Intent().run { 
                setPackage("com.media2359.voiceping.store")
                action = "android.intent.action.PTT.down"
                context.sendBroadcast(this)
            }
            """
        }
        ActionButton(text = "Stop PTT") {
            viewModel.stopPTT(context)
            code = """
                Intent().run { 
                setPackage("com.media2359.voiceping.store")
                action = "android.intent.action.PTT.up"
                context.sendBroadcast(this)
            }
            """
        }
    }
}

class MainActivityViewModelPreviewParameterProvider : PreviewParameterProvider<MainActivityViewModel> {
    override val values = sequenceOf(
        MainActivityViewModel()
    )
}