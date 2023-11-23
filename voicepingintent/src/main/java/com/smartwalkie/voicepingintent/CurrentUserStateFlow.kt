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
    val receiver = Receiver(_user)

    init {
        val intentFilter = IntentFilter().apply {
            addAction("com.voiceping.store.sync_finished")
            addAction("com.voiceping.store.user")
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

    class Receiver(private val _user: MutableStateFlow<User>) : BroadcastReceiver() {
        override fun onReceive(c: Context?, intent: Intent?) {
            intent ?: return

            var username = intent.getStringExtra("username")
            if (username == null) username = ""

            var fullname = intent.getStringExtra("fullname")
            if (fullname == null) fullname = ""

            _user.value = User(username, fullname)
        }
    }
}