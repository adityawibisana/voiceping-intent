package voiceping.intent.demo.screen

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import voiceping.intent.demo.steps.Step
import voiceping.intent.demo.steps.StepInstallVoiceping
import voiceping.intent.demo.steps.StepOpenVoiceping
import voiceping.intent.demo.ui.theme.Typography
import voiceping.intent.demo.view.ActionButton
import voiceping.intent.demo.view.Step

@Preview
@Composable
fun PreviewMainScreen() {
    MainScreen(navController = NavController(LocalContext.current),
        StepInstallVoiceping(),
        StepOpenVoiceping()
    )
}

@Composable
fun MainScreen(navController: NavController,
               stepInstallVoiceping: Step,
               stepOpenVoiceping: Step
) {
    val context = LocalContext.current.applicationContext

    Column(modifier = Modifier.padding(12.dp)) {
        val welcomeText = buildAnnotatedString {
            withStyle(style = SpanStyle(color = Color.Black)) {
                append("Control Voiceping within your app, with ")
            }
            withStyle(style = SpanStyle(color = Color.Blue, textDecoration = TextDecoration.Underline)) {
                append("this library")
                addStringAnnotation(
                    tag = "URL",
                    annotation = "https://github.com/adityawibisana/voiceping-intent",
                    start = 0,
                    end = 11
                )
            }
        }
        ClickableText(welcomeText,
            style = Typography.bodyLarge,
            modifier = Modifier
                .padding(12.dp),
            onClick = { offset ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/adityawibisana/voiceping-intent"))
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(intent)
            }
        )

        Spacer(modifier = Modifier.weight(1.0f))
        Column(modifier = Modifier
            .padding(12.dp)) {
            Text(text = "Here are the steps that you need to follow:")
            Step(text = "Install Voiceping", done = stepInstallVoiceping.done.collectAsState().value) {
                stepInstallVoiceping.action.invoke(context)
            }
            Step(text = "Open and login to Voiceping", done = stepOpenVoiceping.done.collectAsState().value) {
                stepOpenVoiceping.action.invoke(context)
            }
        }
        Spacer(modifier = Modifier.weight(1.0f))
        ActionButton(text = "Go To PTT Screen") {
            navController.navigate(route = Route.START_STOP_PTT_SCREEN)
        }
        ActionButton(text = "Go To Channel Screen") {
            navController.navigate(route = Route.CHANNEL_SCREEN)
        }
        ActionButton(text = "Go To Login Screen") {
            navController.navigate(route = Route.LOGIN_SCREEN)
        }
    }
}