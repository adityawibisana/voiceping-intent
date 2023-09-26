package voiceping.intent.demo

import kotlinx.coroutines.flow.MutableStateFlow

class CodeViewModel {
    val code = MutableStateFlow("Press button to get all the code required to do the action.")

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
                action = "android.intent.action.CHANNELDOWN.up"
                context.sendBroadcast(this)
            }
        """

        const val PREV_CHANNEL_CODE = """
            Intent().run {
                action = "android.intent.action.CHANNELUP.up"
                context.sendBroadcast(this)
            }
        """
    }
}