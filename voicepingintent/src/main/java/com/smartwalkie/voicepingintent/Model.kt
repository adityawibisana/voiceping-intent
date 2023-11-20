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

class CurrentChannel(name: String, type: Int) {
    val name: String = ""
    val type: Int = TYPE_UNKNOWN

    companion object {
        const val TYPE_UNKNOWN = -1
        const val TYPE_PRIVATE = 1
        const val TYPE_GROUP = 0
    }
}