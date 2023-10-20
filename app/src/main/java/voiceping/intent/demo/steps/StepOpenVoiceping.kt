package voiceping.intent.demo.steps

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.RECEIVER_EXPORTED
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager.NameNotFoundException
import android.os.Build
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


class StepOpenVoiceping : Step {
    override val action: (context: Context) -> Unit
        get() = {
            try {
                val intent = it.packageManager.getLaunchIntentForPackage("com.media2359.voiceping.store")
                it.startActivity(intent)
            } catch (e: NameNotFoundException) {
                // TODO: Add warning or something
            }
        }

    override val done: MutableStateFlow<Boolean> = MutableStateFlow(false)

    // the check is done by simply calling channel name. If Voiceping is not responding, then it means Voiceping is not opened.
    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    override fun reloadDoneStatus(context: Context) {
        // add receiver to get the current channel information
        val currentChannelReceiver : BroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                done.tryEmit(true)
            }
        }
        val channelInfoIntent = IntentFilter("android.led.ptt.channel_info")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.registerReceiver(currentChannelReceiver, channelInfoIntent, RECEIVER_EXPORTED)
        } else {
            context.registerReceiver(currentChannelReceiver, channelInfoIntent)
        }

        // check channel name, by calling this:
        Intent().run {
            setPackage("com.media2359.voiceping.store")
            action = "android.intent.action.current_channel"
            context.sendBroadcast(this)
        }

        // if within 1 sec no response from voiceping, we will assume that Voiceping is not opened yet.
        CoroutineScope(Job()).launch(Dispatchers.IO) {
            delay(1000)
            done.tryEmit(false)
            context.unregisterReceiver(currentChannelReceiver)
        }
    }
}