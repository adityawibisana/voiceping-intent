package voiceping.intent.demo

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CodeViewModel {
    private val _code = MutableStateFlow("Press button to get all the code required to do the action.")
    val code = _code.asStateFlow()

    fun updateCode(code: String) {
        _code.value = code
    }

    companion object {
        const val START_PTT_CODE = """
            Voiceping.action.startPTT()
        """

        const val STOP_PTT_CODE = """
            Voiceping.action.stopPTT()
        """

        const val NEXT_CHANNEL_CODE = """
            Voiceping.action.goToNextChannel()
        """

        const val PREV_CHANNEL_CODE = """
            Voiceping.action.goToPrevChannel()
        """

        const val RECEIVE_USER = """            
            Voiceping.state.user.collectAsState().value
        """

        const val CURRENT_CHANNEL = """
            Voiceping.state.currentChannel.collectAsState().value
        """

        fun getLoginIntentCode(username: String, password: String) : String {
            return """
            Voiceping.action.login(username = $username, password = $password)
        """.trimIndent()
        }

        fun getSearchChannelIntentCode(searchText: String): String {
            return """
            Voiceping.action.searchChannel($searchText)
        """.trimIndent()
        }

        fun getLogoutIntentCode() : String {
            return """
            Voiceping.action.logout()
        """.trimIndent()
        }
    }
}