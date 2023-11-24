package com.smartwalkie.voicepingintent

import android.annotation.SuppressLint
import android.content.Context

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
        }

        action.openVoiceping(context)
    }
}