package com.smartwalkie.voicepingintent

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.RECEIVER_EXPORTED
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CurrentUserStateFlow(private val context: Context) {
    private val _user = MutableStateFlow(User("", ""))
    val user = _user.asStateFlow()

    private var receiver: BroadcastReceiver

    init {
        val intentFilter = IntentFilter().apply {
            addAction("com.voiceping.store.sync_finished")
            addAction("com.voiceping.store.user")
        }

        receiver = object: BroadcastReceiver() {
            override fun onReceive(c: Context?, intent: Intent?) {
                intent ?: return

                var username = intent.getStringExtra("username")
                if (username == null) username = ""

                var fullname = intent.getStringExtra("fullname")
                if (fullname == null) fullname = ""

                _user.value = User(username, fullname)
            }
        }
        ContextCompat.registerReceiver(context, receiver, intentFilter, RECEIVER_EXPORTED)
        Intent().run {
            setPackage("com.media2359.voiceping.store")
            action = "com.voiceping.store.get_user"
            context.sendBroadcast(this)
        }
    }

    internal fun destroy() {
        context.unregisterReceiver(receiver)
    }
}