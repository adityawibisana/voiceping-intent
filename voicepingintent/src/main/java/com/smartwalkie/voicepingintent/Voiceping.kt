package com.smartwalkie.voicepingintent

import android.annotation.SuppressLint
import android.content.Context

object Voiceping {
    @SuppressLint("StaticFieldLeak")
    val action = VoicepingAction
    val state = VoicepingState

    fun initialize(context: Context) {
        state.initialize(context.applicationContext)
        action.initialize(context.applicationContext)
    }
}