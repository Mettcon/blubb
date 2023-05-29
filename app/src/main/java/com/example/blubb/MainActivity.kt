@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.blubb

//import android.icu.util.Calendar
//import kotlinx.coroutines.flow.internal.NoOpContinuation.context
//import java.util.Calendar
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.media.AudioManager
import android.os.Bundle
import android.widget.TimePicker
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.content.getSystemService
import com.example.blubb.ui.theme.BlubbTheme
import java.time.LocalTime
import java.util.Locale

//import kotlin.coroutines.jvm.internal.CompletedContinuation.context


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BlubbTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
                    MainView()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainView(
    modifier: Modifier = Modifier
) {
    
    val formatter = remember { SimpleDateFormat("hh:mm a", Locale.getDefault()) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BehaviourEntry()
    }
}



//
//val Actions = hashMapOf<String, Unit>(
//    "Silent" to setRingerMode(AudioManager.RINGER_MODE_SILENT),
//    "Vibrate" to setRingerMode(AudioManager.RINGER_MODE_VIBRATE),
//    "Normal" to setRingerMode(AudioManager.RINGER_MODE_NORMAL),
//
//    )

//private fun setRingerMode(RingerMode: Int) {
//    // available inputs  are
//    // AudioManager.RINGER_MODE_NORMAL,
//    // AudioManager.RINGER_MODE_SILENT,
//    // AudioManager.RINGER_MODE_VIBRATE
//    // permission needed
//    // <uses-permission android:name="android.permission.MODIFY_AUDIO_SETTINGS"/>
//    val audioManager =
//        MyApplication.appContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
//    (Context.AUDIO_SERVICE) as AudioManager
//    audioManager.ringerMode = RingerMode
//}

@Composable
fun BehaviourEntry() {
    var showTimePicker by rememberSaveable { mutableStateOf(false) }
    val pickedTime = rememberTimePickerState()
    val context = LocalContext.current
    var switchState by remember { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClickLabel = "Click to activate", onClick = { showTimePicker = true })
    ) {
        Text("mute", Modifier.padding(8.dp))
        Text("${pickedTime.hour}:${pickedTime.minute}")
        Switch(
            checked = switchState,
            onCheckedChange = { isChecked ->
                switchState = isChecked
                if (isChecked) {
                    registerAlarm(context, pickedTime)
                } else {
                    unregisterAlarm(context)
                }
            },
            Modifier.padding(8.dp)
        )
    }

    if (showTimePicker) {
        Dialog(onDismissRequest = { showTimePicker = false }) {
            TimeInput(pickedTime)
        }
    }
}

//@SuppressLint("UnspecifiedImmutableFlag")
private fun registerAlarm(context: Context, pickedTime: TimePickerState) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(context, MuteReceiver::class.java)
    val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

    val calendar = Calendar.getInstance().apply {
        timeInMillis = System.currentTimeMillis()
        set(Calendar.HOUR_OF_DAY, pickedTime.hour)
        set(Calendar.MINUTE, pickedTime.minute)
    }

    alarmManager.setExactAndAllowWhileIdle(
        AlarmManager.RTC_WAKEUP,
        calendar.timeInMillis,
        pendingIntent
    )
}

private fun unregisterAlarm(context: Context) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(context, MuteReceiver::class.java)
    val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
    alarmManager.cancel(pendingIntent)
}
@Preview(showBackground = true)
@Composable
fun BehaviourEntryPreview() {
    BehaviourEntry()
}

@Preview(showBackground = true)
@Composable
fun MainViewPreview() {
    MainView()
}

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        MyApplication.appContext = applicationContext
    }

    companion object {

        lateinit var appContext: Context

    }
}