package com.smartwalkie.voicepingintent

import android.content.Context

object Voiceping {
    val action = VoicepingAction
    val state = VoicepingState

    fun initialize(context: Context) {
        state.initialize(context)
    }
}