package voiceping.intent.demo

import android.annotation.SuppressLint
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
import com.smartwalkie.voicepingintent.Voiceping
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
    private val stepOpenVoiceping = StepOpenVoiceping()
    private lateinit var loginViewModel : LoginViewModel

    private val channelScreenViewModel : CodeViewModel by lazy { CodeViewModel() }
    private val loginScreenViewModel : CodeViewModel by lazy { CodeViewModel() }
    private val startStopPTTViewModel : CodeViewModel by lazy { CodeViewModel() }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Voiceping.initialize(this)

        loginViewModel = LoginViewModel()

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
                            StartStopPTTScreen(code = startStopPTTViewModel.code) {
                                startStopPTTViewModel.updateCode(it)
                            }
                        }
                        composable(Route.CHANNEL_SCREEN) {
                            ChannelScreen(channelScreenViewModel.code, Voiceping.state.currentChannel) {
                                channelScreenViewModel.updateCode(it)
                            }
                        }
                        composable(Route.LOGIN_SCREEN) {
                            LoginScreen(
                                code = loginScreenViewModel.code,
                                currentUserStateFlow = Voiceping.state.user,
                                username = loginViewModel.username,
                                password = loginViewModel.password,
                                onLogoutClicked = {
                                    Voiceping.action.logout()
                                },
                                onLoginClicked = { u, p ->
                                    run {
                                        loginViewModel.login()
                                    }
                                },
                                updateCode = {
                                    loginScreenViewModel.updateCode(it)
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