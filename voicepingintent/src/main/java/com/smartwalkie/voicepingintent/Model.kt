package com.smartwalkie.voicepingintent

class Channel {
    var name: String? = null
    var users: List<User?>? = null
}

class User(username: String, fullname: String) {
    var username : String? = username
    var fullname: String? = fullname
    val displayName: String? get() {
        if (fullname != null) {
            return fullname
        }
        return username
    }
}