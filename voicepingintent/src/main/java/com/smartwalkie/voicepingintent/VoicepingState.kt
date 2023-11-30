package com.smartwalkie.voicepingintent

import android.content.Context
import kotlinx.coroutines.flow.StateFlow

class VoicepingState {
    private lateinit var currentUsernameStateFlow: CurrentUserStateFlow
    lateinit var user: StateFlow<User>

    private lateinit var currentProcessorStateFlow: CurrentProcessorStateFlow
    lateinit var processor: StateFlow<ProcessorState>

    private lateinit var currentChannelStateFlow: CurrentChannelStateFlow
    lateinit var currentChannel: StateFlow<CurrentChannel>

    private lateinit var currentHealthStateFlow: CurrentHealthStateFlow
    lateinit var health: StateFlow<HealthStatus>

    fun initialize(context: Context) {
        currentUsernameStateFlow = CurrentUserStateFlow(context)
        user = currentUsernameStateFlow.user

        currentProcessorStateFlow = CurrentProcessorStateFlow(context)
        processor = currentProcessorStateFlow.state

        currentChannelStateFlow = CurrentChannelStateFlow(context)
        currentChannel = currentChannelStateFlow.channel

        currentHealthStateFlow = CurrentHealthStateFlow(context)
        health = currentHealthStateFlow.state
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