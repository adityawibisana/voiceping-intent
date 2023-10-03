package voiceping.intent.demo

import kotlinx.coroutines.flow.MutableStateFlow

class CodeViewModel {
    val code = MutableStateFlow("Press button to get all the code required to do the action.")

    fun getSearchChannelIntentCode(searchText: String): String {
        return """
        fun searchChannel(context: Context, channelName: String) {
            Intent().run {
                setPackage("com.media2359.voiceping.store")
                action = "android.led.ptt.select_channel"
                putExtra("name", "$searchText")
                putExtra("SenderPackageName", "your.apps.package.name")
                context.sendBroadcast(this)
            }
        }
        """.trimIndent()
    }

    fun getLoginIntentCode(username: String, password: String) : String {
        return """
        fun login(context: Context, username: String, password: String) {
            Intent().run {
                setPackage("com.media2359.voiceping.store")
                action = "login"
                putExtra("username", "$username")
                putExtra("password", "$password")
                context.sendBroadcast(this)
            }
        }
        """.trimIndent()
    }

    companion object {
        const val START_PTT_CODE = """
            Intent().run { 
                setPackage("com.media2359.voiceping.store")
                action = "android.intent.action.PTT.down"
                context.sendBroadcast(this)
            }
        """

        const val STOP_PTT_CODE = """
            Intent().run {
                setPackage("com.media2359.voiceping.store")
                action = "android.intent.action.PTT.up"
                context.sendBroadcast(this)
            }
        """

        const val NEXT_CHANNEL_CODE = """
            Intent().run {
                setPackage("com.media2359.voiceping.store")
                action = "android.intent.action.CHANNELDOWN.up"
                context.sendBroadcast(this)
            }
        """

        const val PREV_CHANNEL_CODE = """
            Intent().run {
                setPackage("com.media2359.voiceping.store")
                action = "android.intent.action.CHANNELUP.up"
                context.sendBroadcast(this)
            }
        """

        const val RECEIVE_USER = """
        // on your app's context:
        val intentFilter = IntentFilter().apply {
            addAction("com.voiceping.store.sync_finished")
            addAction("com.voiceping.store.user")
        }
        context.registerReceiver(yourReceiverInstance, intentFilter)
        
        // on yourReceiver's class:
        val username = intent.getStringExtra("username")
        """
    }
}