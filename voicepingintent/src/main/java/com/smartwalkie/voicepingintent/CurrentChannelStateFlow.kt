package com.smartwalkie.voicepingintent

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.core.content.ContextCompat
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CurrentChannelStateFlow(private val context: Context) {
    private val receiver : BroadcastReceiver
    private val _channel = MutableStateFlow(CurrentChannel("", CurrentChannel.TYPE_UNKNOWN))
    val channel = _channel.asStateFlow()

    init {
        receiver = object : BroadcastReceiver() {
            override fun onReceive(c: Context?, intent: Intent?) {
                c ?: return
                intent ?: return

                val name = intent.getStringExtra("name")
                name ?: return

                val type = intent.getIntExtra("type", CurrentChannel.TYPE_UNKNOWN)

                _channel.value = CurrentChannel(name, type)
            }
        }
        val intentFilter = IntentFilter()
        intentFilter.addAction("android.led.ptt.channel_info")
        ContextCompat.registerReceiver(context, receiver, intentFilter, ContextCompat.RECEIVER_EXPORTED)

        val intent = Intent("android.intent.action.current_channel")
        intent.`package` = "com.media2359.voiceping.store"
        context.sendBroadcast(intent)
    }

    fun destroy() {
        context.unregisterReceiver(receiver)
    }
}