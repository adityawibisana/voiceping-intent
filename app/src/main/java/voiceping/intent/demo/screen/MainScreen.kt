package voiceping.intent.demo.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import voiceping.intent.demo.Step
import voiceping.intent.demo.StepInstallVoiceping
import voiceping.intent.demo.StepLogin
import voiceping.intent.demo.receivers.SyncFinishedReceiver
import voiceping.intent.demo.ui.theme.Typography
import voiceping.intent.demo.view.ActionButton
import voiceping.intent.demo.view.Step

@Preview
@Composable
fun PreviewMainScreen() {
    MainScreen(navController = NavController(LocalContext.current),
        StepInstallVoiceping(),
        StepLogin(SyncFinishedReceiver()) {
        }
    )
}

@Composable
fun MainScreen(navController: NavController,
               stepInstallVoiceping: Step,
               stepLogin: StepLogin
) {
    val context = LocalContext.current.applicationContext

    Column(modifier = Modifier.padding(12.dp)) {
        Text("Use Voiceping within your app! No additional library is required",
            style = Typography.bodyLarge,
            modifier = Modifier
                .padding(12.dp)
        )

        Spacer(modifier = Modifier.weight(1.0f))
        Column(modifier = Modifier
            .padding(12.dp)) {
            Text(text = "Here are the steps that you need to follow:")
            Step(text = "Install Voiceping", done = stepInstallVoiceping.done.collectAsState().value) {
                stepInstallVoiceping.action.invoke(context)
            }
            Step(text = "Login", done = stepLogin.done.collectAsState().value) {
                stepLogin.action.invoke(context)
            }
        }
        Spacer(modifier = Modifier.weight(1.0f))

        ActionButton(text = "Go To Login Screen") {
            navController.navigate(route = Route.LOGIN_SCREEN)
        }
        ActionButton(text = "Go To PTT Screen") {
            navController.navigate(route = Route.START_STOP_PTT_SCREEN)
        }
        ActionButton(text = "Go To Channel Screen") {
            navController.navigate(route = Route.CHANNEL_SCREEN)
        }
    }
}