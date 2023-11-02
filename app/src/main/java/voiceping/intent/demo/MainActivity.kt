package voiceping.intent.demo

import android.annotation.SuppressLint
import android.os.Build
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
import com.smartwalkie.voicepingintent.VoicepingIntentSender
import com.smartwalkie.voicepingintent.loginusecase.ActionLogin
import voiceping.intent.demo.receivers.SyncFinishedReceiver
import voiceping.intent.demo.screen.ChannelScreen
import voiceping.intent.demo.screen.LoginScreen
import voiceping.intent.demo.screen.MainScreen
import voiceping.intent.demo.screen.Route
import voiceping.intent.demo.screen.StartStopPTTScreen
import voiceping.intent.demo.steps.StepInstallVoiceping
import voiceping.intent.demo.steps.StepOpenVoiceping
import voiceping.intent.demo.ui.theme.VoicepingIntentDemoTheme

class MainActivity : ComponentActivity() {
    private val stepInstallVoiceping = StepInstallVoiceping()
    private val syncFinishedReceiver = SyncFinishedReceiver()
    private val stepOpenVoiceping = StepOpenVoiceping()
    private lateinit var loginViewModel : LoginViewModel

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intentSender = VoicepingIntentSender()
        loginViewModel = LoginViewModel(ActionLogin(this))

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(
                syncFinishedReceiver,
                syncFinishedReceiver.intentFilter,
                RECEIVER_EXPORTED
            )
        } else {
            registerReceiver(syncFinishedReceiver, syncFinishedReceiver.intentFilter)
        }
        // trigger, whether need to login or need
        intentSender.getCurrentUser(this)

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
                            MainScreen(navController,
                                stepInstallVoiceping = stepInstallVoiceping,
                                stepOpenVoiceping = stepOpenVoiceping)
                        }
                        composable(Route.START_STOP_PTT_SCREEN) {
                            StartStopPTTScreen(intentSender = intentSender, codeViewModel = CodeViewModel())
                        }
                        composable(Route.CHANNEL_SCREEN) {
                            ChannelScreen(intentSender = intentSender, codeViewModel = CodeViewModel())
                        }
                        composable(Route.LOGIN_SCREEN) {
                            LoginScreen(intentSender = intentSender,
                                codeViewModel = CodeViewModel(),
                                usernameStateFlow = syncFinishedReceiver.usernameStateFlow,
                                onLoginClicked = { u, p ->
                                    run {
                                        loginViewModel.login(this@MainActivity, u, p)
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    override fun onPostResume() {
        super.onPostResume()
        stepInstallVoiceping.reloadDoneStatus(context = this)
        stepOpenVoiceping.reloadDoneStatus(this)
    }
}