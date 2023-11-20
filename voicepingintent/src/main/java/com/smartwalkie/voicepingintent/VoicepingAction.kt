package com.smartwalkie.voicepingintent

import android.content.Context
import android.content.Intent
import com.smartwalkie.voicepingintent.loginusecase.ActionLogin
import com.smartwalkie.voicepingintent.loginusecase.LoginResult

class VoicepingAction(private val context: Context) {
    private val voicepingIntentSender = VoicepingIntentSender()

    /**
     * return LoginFailed if timed out for 10 secs
     */
    suspend fun login(username: String, password: String) : LoginResult {
        val action = ActionLogin(context)
        return action.login(context, username, password)
    }

    fun logout() {
        voicepingIntentSender.logout(context)
    }

    fun startPTT() {
        voicepingIntentSender.startPTT(context)
    }

    fun stopPTT() {
        voicepingIntentSender.stopPTT(context)
    }

    fun goToNextChannel() {
        voicepingIntentSender.goToNextChannel(context)
    }

    fun goToPrevChannel() {
        voicepingIntentSender.goToPrevChannel(context)
    }

    fun searchChannel(channelName: String) {
        voicepingIntentSender.searchChannel(context, channelName)
    }

    /**
     * Get currently logged in user.
     * Throw error when Voiceping is not responding for 10s
     * return non null object with empty "username" and "fullname" if VP is not logged in.
     */
    suspend fun getUser() : User {
        return voicepingIntentSender.getCurrentUser(context)
    }


    /**
     * Open Voiceping in headless mode.
     * Throw error if Voiceping is not installed
     */
    fun openVoiceping(context: Context) {
        val packageName = "com.media2359.voiceping.store"
        context.packageManager.getLaunchIntentForPackage(packageName)!!.let {
            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            it.putExtra("headless", true)
            context.startActivity(it)
        }
    }
}