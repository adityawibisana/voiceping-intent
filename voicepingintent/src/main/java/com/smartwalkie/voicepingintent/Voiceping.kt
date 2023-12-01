package com.smartwalkie.voicepingintent

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.core.content.ContextCompat
import androidx.lifecycle.asLiveData

object Voiceping {
    @SuppressLint("StaticFieldLeak")
    lateinit var action : VoicepingAction
    val state = VoicepingState()

    private var isInitialized = false

    fun initialize(context: Context) {
        if (!isInitialized) {
            action = VoicepingAction(context.applicationContext)
            state.initialize(context.applicationContext)
            isInitialized = true

            val restartReceiver = object : BroadcastReceiver() {
                override fun onReceive(c: Context?, intent: Intent?) {
                    action.openVoiceping(context)
                }
            }
            val intentFilter = IntentFilter().apply {
                addAction("com.voiceping.store.restarted")
                addAction("com.voiceping.store.killed")
            }
            ContextCompat.registerReceiver(
                context.applicationContext,
                restartReceiver,
                intentFilter,
                ContextCompat.RECEIVER_EXPORTED
            )

            state.health.asLiveData().observeForever {
                if (it is HealthStatus.VoicepingIsNotRunning) {
                    action.openVoiceping(context)
                }
            }
        }

        action.openVoiceping(context)
    }
}