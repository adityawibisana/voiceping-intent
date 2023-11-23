package com.smartwalkie.voicepingintent

import android.content.Intent
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Test

class CurrentUserStateTest {
    @Test
    fun shouldGetLatestCurrentUserStateFlow () {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext
        val currentUserStateFlow = CurrentUserStateFlow(appContext)

        val intent = Intent()
        intent.putExtra("username", "adit")
        intent.putExtra("fullname", "aditya")

        val receiver = currentUserStateFlow.receiver
        receiver.onReceive(null, intent)
        assertEquals("adit", currentUserStateFlow.user.value.username)
        assertEquals("aditya", currentUserStateFlow.user.value.fullname)
    }
}