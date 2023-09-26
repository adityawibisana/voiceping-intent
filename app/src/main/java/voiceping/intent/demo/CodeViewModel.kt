package voiceping.intent.demo

import kotlinx.coroutines.flow.MutableStateFlow

class CodeViewModel {
    val code = MutableStateFlow("Press button to get all the code required to do the action.")
}