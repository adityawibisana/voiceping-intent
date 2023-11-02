package com.smartwalkie.voicepingintent

import android.content.Context
import android.content.Intent

class ActionLogout {
    fun logout(context: Context)  {
        Intent().run {
            setPackage("com.media2359.voiceping.store")
            action = "logout"
            context.sendBroadcast(this)
        }
    }
}