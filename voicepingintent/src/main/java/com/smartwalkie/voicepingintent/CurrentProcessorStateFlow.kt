package com.smartwalkie.voicepingintent

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.core.content.ContextCompat
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CurrentProcessorStateFlow(val context: Context) {
    val STATE_IDLE = 0
    val STATE_RECORDING = 1
    val STATE_PLAYING = 2

    private val _state = MutableStateFlow(STATE_IDLE)
    val state: StateFlow<Int> get() = _state
    private lateinit var stateReceiver: BroadcastReceiver

    fun initialize() : StateFlow<Int> {
        val idleAction = "com.dfl.greenled.off"
        val recordActions = arrayListOf(
            "android.led.ptt.yellow",
            "android.led.ptt.red"
        )
        val playAction = "com.dfl.greenled.on"

        stateReceiver = object: BroadcastReceiver() {
            override fun onReceive(c: Context?, intent: Intent?) {
                intent ?: return

                when (intent.action) {
                    idleAction -> _state.value = STATE_IDLE
                    playAction -> _state.value = STATE_PLAYING
                    else -> _state.value = STATE_RECORDING
                }
            }
        }
        val intentFilter = IntentFilter()
        intentFilter.addAction(idleAction)
        intentFilter.addAction(playAction)
        recordActions.forEach {
            intentFilter.addAction(it)
        }
        ContextCompat.registerReceiver(context, stateReceiver, intentFilter, ContextCompat.RECEIVER_EXPORTED)
        return state
    }

    fun destroy() {
        if (!::stateReceiver.isInitialized) return
        context.unregisterReceiver(stateReceiver)
    }
}