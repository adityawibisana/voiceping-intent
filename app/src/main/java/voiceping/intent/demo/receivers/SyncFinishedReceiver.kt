package voiceping.intent.demo.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import kotlinx.coroutines.flow.MutableStateFlow

class SyncFinishedReceiver : BroadcastReceiver() {
    val usernameStateFlow = MutableStateFlow("")
    val intentFilter = IntentFilter().apply {
        addAction("com.voiceping.store.sync_finished")
        addAction("com.voiceping.store.user")
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        intent ?: return

        val username = intent.getStringExtra("username")
        username ?: return
        usernameStateFlow.tryEmit(username)

        // HINT: try calling intent.getStringExtra("channels")
    }
}