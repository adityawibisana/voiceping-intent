package com.smartwalkie.voicepingintent.loginusecase

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ChannelsStorage(val context: Context) {
    private val channelsTag = "channels"
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("Login", Context.MODE_PRIVATE)

    private val _channels = MutableStateFlow(loadChannels())
    val channels = _channels.asStateFlow()

    fun saveChannels(channels: List<Channel?>?) {
        sharedPreferences.edit(true) {
            _channels.tryEmit(channels)
            putString(this@ChannelsStorage.channelsTag, Gson().toJson(channels))
        }
    }

    private fun loadChannels() : List<Channel?>? {
        val saved = sharedPreferences.getString(channelsTag, null) ?: return null
        val itemType = object : TypeToken<List<Channel>>() {}.type
        return try {
            Gson().fromJson<List<Channel>>(saved, itemType)
        }  catch (e: Exception) {
            null
        }
    }
}