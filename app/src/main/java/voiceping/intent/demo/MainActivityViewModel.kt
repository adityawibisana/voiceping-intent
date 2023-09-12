package voiceping.intent.demo

import android.content.Context
import android.content.Intent

class MainActivityViewModel {

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
    fun startPTT(channelType:  Int, channelId: Int, context: Context) {
        getIntent().run {
            action = "android.intent.action"
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