package com.smartwalkie.voicepingintent

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.core.content.ContextCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class VoicepingIntentSender {

    fun login(context: Context, username: String, password: String) {
        getIntent().run {
            action = "login"
            putExtra("username", username)
            putExtra("password", password)
            context.sendBroadcast(this)
        }
    }

    /**
     * Send PTT to the last heard message / last sent PTT
     */
    fun startPTT(context: Context) {
        Intent().run {
            action = "android.intent.action.PTT.down"
            context.sendBroadcast(this)
        }
    }

    /**
     * @channelType: 0 --> Group. 1 --> Private
     * @channelId: Id of the channel. To see the id, login to https://voiceoverping.net/ as company admin, and then find the desired channel
     */
    fun startPTT(channelId: Int, channelType: Int, context: Context) {
        getIntent().run {
            action = "android.intent.action.PTT.down"
            putExtra("channel_id", channelId)
            putExtra("channel_type", channelType)
            context.sendBroadcast(this)
        }
    }

    fun goToNextChannel(context: Context) {
        getIntent().run {
            action = "android.intent.action.CHANNELDOWN.up"
            context.sendBroadcast(this)
        }
    }

    fun goToPrevChannel(context: Context) {
        getIntent().run {
            action =  "android.intent.action.CHANNELUP.up"
            context.sendBroadcast(this)
        }
    }

    fun searchChannel(context: Context, channelName: String) {
        Intent().run {
            setPackage("com.media2359.voiceping.store")
            action = "android.led.ptt.select_channel"
            putExtra("name", channelName)
            putExtra("SenderPackageName", "your.apps.package.name")
            context.sendBroadcast(this)
        }
    }

    fun stopPTT(context: Context) {
        getIntent().run {
            action = "android.intent.action.PTT.up"
            context.sendBroadcast(this)
        }
    }

    suspend fun getCurrentUser(context: Context) = suspendCoroutine {
        val timeout = CoroutineScope(Job()).launch {
            delay(10_000)
            it.resumeWithException(Error("Timeout after 10 secs. Likely, Voiceping is not opened."))
        }

        val userReceiver = object : BroadcastReceiver() {
            override fun onReceive(c: Context?, intent: Intent?) {
                timeout.cancel()

                if (c == null) {
                    it.resumeWithException(Error("context null. Shouldn't happened"))
                    return
                }

                if (intent == null) {
                    it.resumeWithException(Error("intent is null. Shouldn't happened"))
                    return
                }

                val username = if (intent.getStringExtra("username") == null) {
                    ""
                } else intent.getStringExtra("username")!!

                val fullname = if (intent.getStringExtra("fullname") == null) {
                    ""
                } else intent.getStringExtra("fullname")!!

                it.resume(User(username, fullname))
                context.unregisterReceiver(this)
            }
        }


        val filter = IntentFilter()
        filter.addAction("com.voiceping.store.user")
        ContextCompat.registerReceiver(context, userReceiver, filter, ContextCompat.RECEIVER_EXPORTED)

        getIntent().run {
            action = "com.voiceping.store.get_user"
            context.sendBroadcast(this)
        }
    }

    fun logout(context: Context) {
        Intent().run {
            setPackage("com.media2359.voiceping.store")
            action = "logout"
            context.sendBroadcast(this)
        }
    }


    private fun getIntent() = Intent().apply {
        setPackage("com.media2359.voiceping.store")
    }
}