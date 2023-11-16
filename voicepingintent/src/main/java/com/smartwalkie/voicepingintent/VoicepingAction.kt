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

    fun goToNextChannel(context: Context) {
        voicepingIntentSender.goToNextChannel(context)
    }

    fun goToPrevChannel(context: Context) {
        voicepingIntentSender.goToPrevChannel(context)
    }

    fun searchChannel(context: Context, channelName: String) {
        voicepingIntentSender.searchChannel(context, channelName)
    }
}