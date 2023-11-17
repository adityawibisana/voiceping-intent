package com.smartwalkie.voicepingintent

import android.content.Context
import kotlinx.coroutines.flow.StateFlow

object VoicepingState {
    private lateinit var currentUsernameStateFlow: CurrentUserStateFlow
    lateinit var user: StateFlow<User>

    fun initialize(context: Context) {
        currentUsernameStateFlow = CurrentUserStateFlow(context)
        user = currentUsernameStateFlow.user
    }

}