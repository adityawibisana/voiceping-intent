package voiceping.intent.demo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartwalkie.voicepingintent.Voiceping
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class LoginViewModel :  ViewModel() {
    val username = MutableStateFlow("")
    val password = MutableStateFlow("")

    fun login() {
        viewModelScope.launch(Dispatchers.IO) {
            Voiceping.action.login(username = username.value, password = password.value)
        }
    }
}