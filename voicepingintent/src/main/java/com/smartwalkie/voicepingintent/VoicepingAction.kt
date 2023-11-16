package com.smartwalkie.voicepingintent

import android.content.Context
import com.smartwalkie.voicepingintent.loginusecase.ActionLogin
import com.smartwalkie.voicepingintent.loginusecase.LoginResult
import kotlin.coroutines.suspendCoroutine

object VoicepingAction {
    private val voicepingIntentSender = VoicepingIntentSender()

    /**
     * return LoginFailed if timed out for 10 secs
     */
    suspend fun login(context: Context, username: String, password: String) : LoginResult {
        val action = ActionLogin(context)
        return action.login(context, username, password)
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

    /**
     * Get currently logged in user.
     * Throw error when Voiceping is not responding for 10s
     * return non null object with empty "username" and "fullname" if VP is not logged in.
     */
    suspend fun getUser(context: Context) : User {
        return voicepingIntentSender.getCurrentUser(context)
    }
}