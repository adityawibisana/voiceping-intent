package com.smartwalkie.voicepingintent

import androidx.lifecycle.asLiveData
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Test

import org.junit.Assert.*

/**
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class PrimitiveStateUnitTest {
    @Test
    fun `should get latest primitive state value of the user`() {
        val voicepingState = VoicepingState()
        val userMutableStateFlow = MutableStateFlow(User("adit", "aditya"))
        voicepingState.user = userMutableStateFlow

        val voicepingPrimitiveState = VoicepingPrimitiveState(voicepingState)
        var userString = voicepingPrimitiveState.getUser()

        assert(userString.contains("\"username\":\"adit\""))
        assert(userString.contains("\"fullname\":\"aditya\""))

        userMutableStateFlow.value = User("wibi", "wibisana")
        userString = voicepingPrimitiveState.getUser()

        assert(userString.contains("\"username\":\"wibi\""))
        assert(userString.contains("\"fullname\":\"wibisana\""))
    }

    @Test
    fun `should get latest primitive state value of the processor`() {
        val voicepingState = VoicepingState()
        val processorStateFlow = MutableStateFlow<ProcessorState>(ProcessorState.StateIdle)
        voicepingState.processor = processorStateFlow

        val voicepingPrimitiveState = VoicepingPrimitiveState(voicepingState)
        var processorString = voicepingPrimitiveState.getProcessor()
        assertEquals("{}", processorString)

        processorStateFlow.value = ProcessorState.StatePlaying(
            from = "aditya",
            to = "wibisana",
            type = 1
        )
        processorString = voicepingPrimitiveState.getProcessor()
        assert(processorString.contains("\"from\":\"aditya\""))
        assert(processorString.contains("\"to\":\"wibisana\""))
        assert(processorString.contains("\"type\":1"))

        processorStateFlow.value = ProcessorState.StateRecording(
            from = "john",
            to = "doe",
            type = 0
        )
        processorString = voicepingPrimitiveState.getProcessor()
        assert(processorString.contains("\"from\":\"john\""))
        assert(processorString.contains("\"to\":\"doe\""))
        assert(processorString.contains("\"type\":0"))
    }
}