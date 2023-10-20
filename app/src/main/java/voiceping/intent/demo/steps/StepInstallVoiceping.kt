package voiceping.intent.demo.steps

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class StepInstallVoiceping: Step {
    private val packageName = "com.media2359.voiceping.store"

    override val done = MutableStateFlow(false)
    override val action: (context: Context) -> Unit
        get() = {
            val intent = Intent()
            intent.action = Intent.ACTION_VIEW
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

            try {
                intent.data = Uri.parse("market://details?id=$packageName")
                it.startActivity(intent)
            } catch (_: ActivityNotFoundException) {
                intent.data = Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
                it.startActivity(intent)
            }
        }

    override fun reloadDoneStatus(context: Context) {
        CoroutineScope(Job()).launch(Dispatchers.IO) {
            done.emit(isAppInstalled(context, packageName))
        }
    }

    private fun isAppInstalled(context: Context, packageName: String): Boolean {
        return try {
            context.packageManager.getApplicationInfo(packageName, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }
}