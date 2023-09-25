package voiceping.intent.demo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import voiceping.intent.demo.ui.theme.VoicepingIntentDemoTheme

class MainActivity : ComponentActivity() {
    private val viewModel = MainActivityViewModel()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VoicepingIntentDemoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize().padding(12.dp),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun StartPTT(startPTT:() -> Unit) {
    Button(
        modifier = Modifier.fillMaxWidth(),
        onClick = {
        startPTT.invoke()
    }) {
        Text(text = "Start PTT")
    }
}

@Composable
fun StopPTT(stopPTT:() -> Unit) {
    Button(
        modifier = Modifier.fillMaxWidth(),
        onClick = {
        stopPTT.invoke()
    }) {
        Text(text = "Stop PTT")
    }
}

@Preview
@Composable
fun MainScreen(
    @PreviewParameter(MainActivityViewModelPreviewParameterProvider::class)  viewModel: MainActivityViewModel) {
    val context = LocalContext.current.applicationContext
    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(Modifier.weight(1f))
        StartPTT {
            viewModel.startPTT(context)
        }
        StopPTT {
            viewModel.stopPTT(context)
        }
    }
}

class MainActivityViewModelPreviewParameterProvider : PreviewParameterProvider<MainActivityViewModel> {
    override val values = sequenceOf(
        MainActivityViewModel()
    )
}