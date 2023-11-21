package com.smartwalkie.voicepingintent.loginusecase

import com.smartwalkie.voicepingintent.Channel

sealed class LoginResult {
    data class LoginSuccess(val channels: List<Channel?>? = null) : LoginResult()
    data class LoginFailed(val message: String) : LoginResult()
}


