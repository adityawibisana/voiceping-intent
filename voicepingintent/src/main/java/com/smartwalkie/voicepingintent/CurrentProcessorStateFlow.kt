package com.smartwalkie.voicepingintent

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.core.content.ContextCompat
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CurrentProcessorStateFlow(private val context: Context) {
    private val _state = MutableStateFlow<ProcessorState>(ProcessorState.StateIdle)
    val state: StateFlow<ProcessorState> get() = _state
    private var stateReceiver: BroadcastReceiver

    init {
        val idleAction = "com.dfl.greenled.off"
        val recordActions = arrayListOf(
            "android.led.ptt.yellow",
            "android.led.ptt.red"
        )
        val playAction = "com.media2359.voiceping.store.play"

        stateReceiver = object: BroadcastReceiver() {
            override fun onReceive(c: Context?, intent: Intent?) {
                intent ?: return

                when (intent.action) {
                    idleAction -> _state.value = ProcessorState.StateIdle
                    playAction -> {
                        val from = intent.getStringExtra("from") ?: return
                        val to = intent.getStringExtra("to") ?: return
                        val type = intent.getIntExtra("type", CurrentChannel.TYPE_UNKNOWN)
                        _state.value = ProcessorState.StatePlaying(from, to, type)
                    }
                    else -> {
                        val from = intent.getStringExtra("from") ?: return
                        val to = intent.getStringExtra("to") ?: return
                        val type = intent.getIntExtra("type", CurrentChannel.TYPE_UNKNOWN)
                        _state.value = ProcessorState.StateRecording(from, to, type)
                    }
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
    }

    fun destroy() {
        context.unregisterReceiver(stateReceiver)
    }

    companion object {
        const val STATE_IDLE = 0
        const val STATE_RECORDING = 1
        const val STATE_PLAYING = 2
    }
}

sealed class ProcessorState {
    data class StatePlaying(val from: String, val to: String, val type: Int) : ProcessorState()
    data object StateIdle : ProcessorState()
    data class StateRecording(val from: String, val to: String, val type: Int) : ProcessorState()
}