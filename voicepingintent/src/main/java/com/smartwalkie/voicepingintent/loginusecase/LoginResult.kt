package com.smartwalkie.voicepingintent.loginusecase

interface LoginResult { }
class LoginSuccess : LoginResult {
    var channels: List<Channel?>? = null
}
data class LoginFailed(val message: String) : LoginResult { }
