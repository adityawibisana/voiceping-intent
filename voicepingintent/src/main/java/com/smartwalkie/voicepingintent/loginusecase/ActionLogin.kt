package com.smartwalkie.voicepingintent.loginusecase

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.RECEIVER_NOT_EXPORTED
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class ActionLogin(context: Context) {
    private lateinit var syncFinishedReceiver: BroadcastReceiver
    private val channelStorage: ChannelsStorage

    init {
        channelStorage = ChannelsStorage(context)
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    suspend fun login(context: Context, username: String, password: String) = suspendCoroutine<LoginResult?> {
        val timeout = CoroutineScope(Job()).launch(Dispatchers.IO) {
            delay(10_000)
            it.resume(LoginFailed("Timeout. Please try again."))
        }

        val intentFilter = IntentFilter().apply {
            addAction("com.voiceping.store.sync_finished")
        }
        syncFinishedReceiver = object : BroadcastReceiver() {
            override fun onReceive(c: Context?, intent: Intent?) {
                context.unregisterReceiver(syncFinishedReceiver)
                if (intent == null) {
                    it.resume(LoginSuccess())
                    return
                } else {
                    intent.run {
                        val channels = intent.getStringExtra("channels")
                        if (channels == null) {
                            timeout.cancel()
                            channelStorage.saveChannels(null)
                            it.resume(LoginSuccess())
                            return
                        } else {
                            val itemType = object : TypeToken<List<Channel>>() {}.type
                            try {
                                val parsed = Gson().fromJson<List<Channel>>(channels, itemType)
                                it.resume(LoginSuccess().apply {
                                    timeout.cancel()
                                    channelStorage.saveChannels(parsed)
                                    this.channels = parsed
                                })
                                return
                            }  catch (e: Exception) {
                                timeout.cancel()
                                channelStorage.saveChannels(null)
                                it.resume(LoginSuccess())
                            }
                            return
                        }
                    }
                }
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.registerReceiver(syncFinishedReceiver, intentFilter, RECEIVER_NOT_EXPORTED)
        } else {
            context.registerReceiver(syncFinishedReceiver, intentFilter)
        }

        Intent().apply {
            setPackage("com.media2359.voiceping.store")
            action = "login"
            putExtra("username", username)
            putExtra("password", password)
            context.sendBroadcast(this)
        }
    }
}