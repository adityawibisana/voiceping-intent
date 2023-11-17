package com.smartwalkie.voicepingintent

import android.annotation.SuppressLint
import android.content.Context
import kotlinx.coroutines.flow.StateFlow

object VoicepingState {
    @SuppressLint("StaticFieldLeak")
    private lateinit var currentUsernameStateFlow: CurrentUserStateFlow
    lateinit var user: StateFlow<User>

    @SuppressLint("StaticFieldLeak")
    private lateinit var currentProcessorStateFlow: CurrentProcessorStateFlow
    lateinit var processor: StateFlow<Int>

    fun initialize(context: Context) {
        currentUsernameStateFlow = CurrentUserStateFlow(context)
        user = currentUsernameStateFlow.user

        currentProcessorStateFlow = CurrentProcessorStateFlow(context)
        processor = currentProcessorStateFlow.state
    }

    fun destroy() {
        if (::currentUsernameStateFlow.isInitialized) {
            currentUsernameStateFlow.destroy()
        }

        if (::currentUsernameStateFlow.isInitialized) {
            currentProcessorStateFlow.destroy()
        }
    }
}