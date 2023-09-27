package voiceping.intent.demo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import voiceping.intent.demo.screen.ChannelScreen
import voiceping.intent.demo.screen.LoginScreen
import voiceping.intent.demo.screen.MainScreen
import voiceping.intent.demo.screen.Route
import voiceping.intent.demo.screen.StartStopPTTScreen
import voiceping.intent.demo.ui.theme.VoicepingIntentDemoTheme


class MainActivity : ComponentActivity() {
    private val stepInstallVoiceping = StepInstallVoiceping()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intentSender = VoicepingIntentSender()

        setContent {
            VoicepingIntentDemoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier
                        .fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(navController, startDestination = Route.MAIN_SCREEN) {
                        composable(Route.MAIN_SCREEN) {
                            MainScreen(navController, stepInstallVoiceping)
                        }
                        composable(Route.START_STOP_PTT_SCREEN) {
                            StartStopPTTScreen(intentSender = intentSender, codeViewModel = CodeViewModel())
                        }
                        composable(Route.CHANNEL_SCREEN) {
                            ChannelScreen(intentSender = intentSender, codeViewModel = CodeViewModel())
                        }
                        composable(Route.LOGIN_SCREEN) {
                            LoginScreen(intentSender = intentSender, codeViewModel = CodeViewModel())
                        }
                    }
                }
            }
        }
    }

    override fun onPostResume() {
        super.onPostResume()
        stepInstallVoiceping.reloadDoneStatus(context = this)
    }
}