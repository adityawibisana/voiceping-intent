package com.smartwalkie.voicepingintent.loginusecase

class Channel {
    var name: String? = null
    var users: List<User?>? = null
}

class User {
    var username : String? = null
    var fullname: String? = null
    val displayName: String? get() {
        if (fullname != null) {
            return fullname
        }
        return username
    }
}