package voiceping.intent.demo.steps

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import voiceping.intent.demo.receivers.SyncFinishedReceiver

class StepLogin(private val syncFinishedReceiver: SyncFinishedReceiver,
                override val action: (context: Context) -> Unit,
) : Step {
    override val done = MutableStateFlow(false)

    init {
        CoroutineScope(Job()).launch(Dispatchers.IO) {
            syncFinishedReceiver.usernameStateFlow.collect() {
                done.emit(syncFinishedReceiver.usernameStateFlow.value.isNotBlank())
            }
        }
    }

    override fun reloadDoneStatus(context: Context) {

    }
}