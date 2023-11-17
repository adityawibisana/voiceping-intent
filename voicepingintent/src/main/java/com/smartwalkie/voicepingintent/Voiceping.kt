package com.smartwalkie.voicepingintent

import android.annotation.SuppressLint
import android.content.Context

object Voiceping {
    @SuppressLint("StaticFieldLeak")
    lateinit var action : VoicepingAction
    val state = VoicepingState

    fun initialize(context: Context) {
        action = VoicepingAction(context.applicationContext)
        state.initialize(context.applicationContext)
    }
}