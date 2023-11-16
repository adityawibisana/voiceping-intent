package com.smartwalkie.voicepingintent

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.smartwalkie.voicepingintent.loginusecase.ActionLogin
import com.smartwalkie.voicepingintent.loginusecase.LoginResult

object VoicepingAction {
    const val STATE_IDLE = 0
    const val STATE_RECORDING = 1
    const val STATE_PLAYING = 2

    private val voicepingIntentSender = VoicepingIntentSender()
    private val _state = MutableLiveData(STATE_IDLE)
    val state: LiveData<Int> get() = _state

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

    /**
     * Initialize for one time.
     * This will allows you to listen voiceping state
     */
    fun initializeState(context: Context) {
        val idleAction = "com.dfl.greenled.off"
        val recordActions = arrayListOf(
            "android.led.ptt.yellow",
            "android.led.ptt.red"
        )
        val playAction = "com.dfl.greenled.on"

        val receiver = object: BroadcastReceiver() {
            override fun onReceive(c: Context?, intent: Intent?) {
                intent ?: return

                when (intent.action) {
                    idleAction -> _state.postValue(STATE_IDLE)
                    playAction -> _state.postValue(STATE_PLAYING)
                    else -> _state.postValue(STATE_RECORDING)
                }
            }
        }
        val intentFilter = IntentFilter()
        intentFilter.addAction(idleAction)
        intentFilter.addAction(playAction)
        recordActions.forEach {
            intentFilter.addAction(it)
        }
        ContextCompat.registerReceiver(context, receiver, intentFilter, ContextCompat.RECEIVER_EXPORTED)
    }
}