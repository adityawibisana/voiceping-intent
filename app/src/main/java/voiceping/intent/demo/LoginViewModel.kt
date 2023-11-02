package voiceping.intent.demo

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartwalkie.voicepingintent.loginusecase.ActionLogin
import kotlinx.coroutines.launch

class LoginViewModel(private val actionLogin: ActionLogin) :  ViewModel() {
    fun login(context: Context, username: String, password: String) {
        viewModelScope.launch {
            actionLogin.login(context, username, password)
        }
    }
}