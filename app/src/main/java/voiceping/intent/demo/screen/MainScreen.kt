package voiceping.intent.demo.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import voiceping.intent.demo.view.ActionButton

@Preview
@Composable
fun PreviewMainScreen() {
    MainScreen(navController = NavController(LocalContext.current))
}

@Composable
fun MainScreen(navController: NavController) {
    Column {
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