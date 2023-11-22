package com.smartwalkie.voicepingintent

import android.annotation.SuppressLint
import android.content.Context
import kotlinx.coroutines.flow.StateFlow

class VoicepingState {
    @SuppressLint("StaticFieldLeak")
    private lateinit var currentUsernameStateFlow: CurrentUserStateFlow
    lateinit var user: StateFlow<User>

    @SuppressLint("StaticFieldLeak")
    private lateinit var currentProcessorStateFlow: CurrentProcessorStateFlow
    lateinit var processor: StateFlow<ProcessorState>

    @SuppressLint("StaticFieldLeak")
    private lateinit var currentChannelStateFlow: CurrentChannelStateFlow
    lateinit var currentChannel: StateFlow<CurrentChannel>

    fun initialize(context: Context) {
        currentUsernameStateFlow = CurrentUserStateFlow(context)
        user = currentUsernameStateFlow.user

        currentProcessorStateFlow = CurrentProcessorStateFlow(context)
        processor = currentProcessorStateFlow.state

        currentChannelStateFlow = CurrentChannelStateFlow(context)
        currentChannel = currentChannelStateFlow.channel
    }

    private fun destroy() {
        if (::currentUsernameStateFlow.isInitialized) {
            currentUsernameStateFlow.destroy()
        }

        if (::currentUsernameStateFlow.isInitialized) {
            currentProcessorStateFlow.destroy()
        }

        if (::currentChannelStateFlow.isInitialized) {
            currentChannelStateFlow.destroy()
        }
    }
}