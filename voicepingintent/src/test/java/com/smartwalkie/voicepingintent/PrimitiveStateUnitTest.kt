package com.smartwalkie.voicepingintent

import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Assert.assertEquals
import org.junit.Test

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

    @Test
    fun `should get the latest primitive value of the current channel`() {
        val voicepingState = VoicepingState()
        val currentChannelStateFlow = MutableStateFlow(CurrentChannel("aditya", 0))
        voicepingState.currentChannel = currentChannelStateFlow

        val voicepingPrimitiveState = VoicepingPrimitiveState(voicepingState)
        val currentChannelString = voicepingPrimitiveState.getCurrentChannel()
        assert(currentChannelString.contains("\"name\":\"aditya\""))
        assert(currentChannelString.contains("\"type\":0"))
    }
}