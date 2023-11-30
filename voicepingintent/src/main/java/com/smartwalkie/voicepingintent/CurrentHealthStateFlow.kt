package com.smartwalkie.voicepingintent

import android.content.ActivityNotFoundException
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.provider.Settings
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow


class CurrentHealthStateFlow(val context: Context) {
    val voicepingPackageName = "com.media2359.voiceping.store"

    private val notInstalledHealthStatus = HealthStatus.VoicepingIsNotInstalled {
        try {
            startActivity(
                context,
                Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$voicepingPackageName")).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }, null)
        } catch (e: ActivityNotFoundException) {
            startActivity(
                context,
                Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$voicepingPackageName")).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                , null)
        }
    }

    private val _state = MutableStateFlow<HealthStatus>(notInstalledHealthStatus)
    val state = _state.asStateFlow()

    private val stateReceiver: BroadcastReceiver

    init {
        stateReceiver = object: BroadcastReceiver() {
            override fun onReceive(c: Context?, intent: Intent?) {
                intent ?: return

                val message = intent.getStringExtra("message")
                if (message.isNullOrBlank()) return

                val code = intent.getIntExtra("code", -1)
                if (code == -1) return

                when (code) {
                    0 -> _state.value = HealthStatus.VoicepingReady
                    1 -> _state.value = HealthStatus.VoicepingIsNotLoggedIn()
                    2 -> _state.value = HealthStatus.VoicepingMicPermissionIsNotGranted {
                        val i = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        val uri = Uri.fromParts("package", voicepingPackageName, null)
                        i.data = uri
                        startActivity(context, i, null)
                    }
                    3 -> _state.value = HealthStatus.VoicepingIsNotConnected("Voiceping is not connected to the internet.")
                    4 -> _state.value = HealthStatus.VoicepingServiceIsNotRunning()
                }
            }
        }
        val intentFilter = IntentFilter()
        intentFilter.addAction("com.voiceping.store.health")
        ContextCompat.registerReceiver(context, stateReceiver, intentFilter, ContextCompat.RECEIVER_EXPORTED)
        context.sendBroadcast(Intent("com.voiceping.store.health_status"))
    }
}

/**
 * Action is the default action for the given problem.
 */
sealed class HealthStatus {
    data object VoicepingReady : HealthStatus()
    data class VoicepingIsNotInstalled(val message: String = "Please install Voiceping / use latest Voiceping (at least version 3.2.0)", val action: (() -> Unit) = { }) : HealthStatus()
    data class VoicepingIsNotLoggedIn(val message: String = "Please login to Voiceping") : HealthStatus()
    data class VoicepingMicPermissionIsNotGranted(val message: String = "Please allow 'Microphone' permission for Voiceping", val action: () -> Unit = { }) : HealthStatus()
    data class VoicepingIsNotConnected(val message: String) : HealthStatus()
    data class VoicepingServiceIsNotRunning(val message: String = "Voiceping service is not running. Please login to Voiceping first.") : HealthStatus()
}
