package com.smartwalkie.voicepingintent

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.RECEIVER_EXPORTED
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CurrentUsernameStateFlow(context: Context) {
    private val _username = MutableStateFlow("")
    val username = _username.asStateFlow()

    init {
        val intentFilter = IntentFilter().apply {
            addAction("com.voiceping.store.sync_finished")
            addAction("com.voiceping.store.user")
        }

        val receiver = object: BroadcastReceiver() {
            override fun onReceive(c: Context?, intent: Intent?) {
                intent ?: return
                val username = intent.getStringExtra("username")
                username ?: return
                _username.value = username
            }
        }
        ContextCompat.registerReceiver(context, receiver, intentFilter, RECEIVER_EXPORTED)
        Intent().run {
            setPackage("com.media2359.voiceping.store")
            action = "com.voiceping.store.get_user"
            context.sendBroadcast(this)
        }
    }
}