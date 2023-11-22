package com.smartwalkie.voicepingintent

import com.google.gson.Gson

/**
 * Useful for Flutter, because flutter only knows primitive method with primitive return type
 */
class VoicepingPrimitiveState(private val state: VoicepingState) {
    private val gson = Gson()

    fun getUser() : String {
        return gson.toJson(state.user.value, User::class.java)
    }

    fun getCurrentChannel() : String {
        return gson.toJson(state.currentChannel.value, CurrentChannel::class.java)
    }

    fun getProcessor() : String {
        val stateValue = state.processor.value
        val type = when (stateValue) {
            is ProcessorState.StateIdle -> ProcessorState.StateIdle::class.java
            is ProcessorState.StatePlaying -> ProcessorState.StatePlaying::class.java
            is ProcessorState.StateRecording -> ProcessorState.StateRecording::class.java
        }
        return gson.toJson(stateValue, type)
    }
}
