package it.delbix.coffeecounter

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import it.delbix.coffeecounter.RichiesteServer.initPushNotificationManager
import it.delbix.coffeecounter.Settings.initSettings
import it.delbix.coffeecounter.utils.ScreenUtils

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initPushNotificationManager(applicationContext)
        FirebaseApp.initializeApp(this)
        askNotificationPermission()

        ScreenUtils.initialize(this)
        initSettings(this)


//        Log.d("Main", "sto per chiedere il token...")
//
//        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
//            if (!task.isSuccessful) {
//                Log.w("TOKEN", "Fetching FCM registration token failed", task.exception)
//                return@OnCompleteListener
//            }
//
//            // Get new FCM registration token
//            val token = task.result
//
//            // Log and toast
//            val msg = token
//            Log.d("TOKEN", msg)
//            val clipboardManager: AndroidClipboardManager = AndroidClipboardManager(this)
//            clipboardManager.copyToClipboard(msg)
////            Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
//        })


        setContent {
            App()
        }
    }

    // [START ask_post_notifications]
    // Declare the launcher at the top of your Activity/Fragment:
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            // FCM SDK (and your app) can post notifications.
        } else {
            Toast.makeText(baseContext, "Non ti arriveranno notifiche!! \nMolto male!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // FCM SDK (and your app) can post notifications.
                subscribeToNotifications()
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                // TODO: display an educational UI explaining to the user the features that will be enabled
                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                //       If the user selects "No thanks," allow the user to continue without notifications.
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        } else {
            //per le versioni di android <13 non serve il permesso, iscrivi direttamente
            subscribeToNotifications()
        }
    }

    private fun subscribeToNotifications(){
        FirebaseMessaging.getInstance().subscribeToTopic("notifications").addOnCompleteListener{
                task ->
            val msg = if (task.isSuccessful) "Notifiche attivate correttamente" else "ERRORE nella sottoscrizione alle notifiche!"
            Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
        }
    }
    // [END ask_post_notifications]

}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}