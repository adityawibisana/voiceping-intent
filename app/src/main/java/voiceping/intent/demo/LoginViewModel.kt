package voiceping.intent.demo

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartwalkie.voicepingintent.loginusecase.ActionLogin
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(private val actionLogin: ActionLogin) :  ViewModel() {
    val username = MutableStateFlow("")
    val password = MutableStateFlow("")
    fun login(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            actionLogin.login(context, username.value, password.value)
        }
    }
}