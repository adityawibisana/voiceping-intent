package voiceping.intent.demo

import android.content.Context
import android.content.Intent

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
        getIntent().run {
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


    private fun getIntent() = Intent().apply {
        setPackage("com.media2359.voiceping.store")
    }
}