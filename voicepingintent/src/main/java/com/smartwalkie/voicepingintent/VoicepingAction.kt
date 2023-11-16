package com.smartwalkie.voicepingintent

import android.content.Context

object VoicepingAction {
    private val voicepingIntentSender = VoicepingIntentSender()

    fun login(context: Context, username: String, password: String) {
        voicepingIntentSender.login(context, username, password)
    }

    fun logout(context: Context) {
        voicepingIntentSender.logout(context)
    }

    fun startPTT(context: Context) {
        voicepingIntentSender.startPTT(context)
    }

    fun stopPTT(context: Context) {
        voicepingIntentSender.stopPTT(context)
    }
}