package voiceping.intent.demo.steps

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow

interface Step {
    val action: (context: Context) -> Unit
    val done: MutableStateFlow<Boolean>
    fun reloadDoneStatus(context: Context)
}