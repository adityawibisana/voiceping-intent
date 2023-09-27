package voiceping.intent.demo.view

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import voiceping.intent.demo.R
import voiceping.intent.demo.ui.theme.Purple80
import voiceping.intent.demo.ui.theme.Typography
import voiceping.intent.demo.ui.theme.codeFontStyle

@Preview
@Composable
fun CodeTextPreview() {
    CodeText(code = "val Intent = Intent()", context = LocalContext.current)
}

@Composable
fun CodeText(code: String, context: Context) {
    val copyCode = {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
        val clip = ClipData.newPlainText("source code", code)
        clipboard!!.setPrimaryClip(clip)
    }

    Column {
        Text(
            text = "The only code that you need:",
            modifier = Modifier
                .padding(12.dp))
        BasicTextField(value = code.trimIndent(), onValueChange = {}, textStyle = codeFontStyle, readOnly = true)
        Row {
            Spacer(modifier = Modifier.weight(1f))
            TextButton(onClick = copyCode) {
                Text(text = "Copy", modifier = Modifier.padding(12.dp))
            }
        }
    }
}

@Preview
@Composable
fun ActionButtonPreview() {
    ActionButton(text = "Start PTT") {
        
    }
}

@Composable
fun ActionButton(text: String, action:() -> Unit) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        onClick = {
            action.invoke()
        }) {
        Text(text = text)
    }
}

@Preview
@Composable
fun StepPreview() {
    Step("Install Voiceping", true, null)
}

@Preview
@Composable
fun StepNotDonePreview() {
    Step("Install Voiceping", false) {

    }
}

@Composable
fun Step(text: String, done: Boolean, action: (() -> Unit)?) {
    var spanStyle = SpanStyle()
    if (action != null && !done) {
        spanStyle = SpanStyle(Purple80)
    }

    Row(Modifier
        .clickable {
            if (!done) {
                action?.invoke()
            }
        }.padding(12.dp)
    ) {
        Text(buildAnnotatedString {
            withStyle(style = spanStyle) {
                append(text)
            }
        })
        Spacer(modifier = Modifier.weight(1.0f))
        if (done) {
            Image(
                painter = painterResource(id = R.drawable.baseline_check_circle_24),
                contentDescription = "Check"
            )
        }
    }
}