package com.arysapp.task.core.alarm
import com.arysapp.task.core.notification.TaskNotificationManager
import android.app.Notification
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import android.util.Log
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AlarmRingtoneService : Service() {

    companion object {
        private const val TAG = "AlarmRingtoneService"
    }

    private var mediaPlayer: MediaPlayer? = null

    @Inject
    lateinit var taskRepeatHandler: TaskRepeatHandler

    private var currentTaskId: Long = -1L
    private var currentRepeatType: String? = null
    private var currentDateTime: String? = null
    private var currentHourTime: String? = null

    override fun onCreate() {
        super.onCreate()
        TaskNotificationManager.ensureChannelsExist(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        try {
            val action = intent?.action
            if (action == TaskNotificationManager.ACTION_STOP_ALARM) {
                Log.d(TAG, "Received STOP action")
                val taskId = intent.getLongExtra(AlarmIntentFactory.EXTRA_TASK_ID, -1L)
                val repeatType = intent.getStringExtra(AlarmIntentFactory.EXTRA_REPEAT_TYPE)
                val dateTime = intent.getStringExtra(AlarmIntentFactory.EXTRA_DATE_TIME)
                val hourTime = intent.getStringExtra(AlarmIntentFactory.EXTRA_HOUR_TIME)

                stopAlarmSound()
                @Suppress("DEPRECATION")
                stopForeground(true)
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        taskRepeatHandler.handleRepeat(taskId, repeatType, dateTime, hourTime)
                    } catch (e: Exception) {
                        Log.e(TAG, "handleRepeat error", e)
                    }
                }
                stopSelf()
                return START_NOT_STICKY
            }

            currentTaskId = intent?.getLongExtra(AlarmIntentFactory.EXTRA_TASK_ID, -1L) ?: -1L
            currentRepeatType = intent?.getStringExtra(AlarmIntentFactory.EXTRA_REPEAT_TYPE)
            currentDateTime = intent?.getStringExtra(AlarmIntentFactory.EXTRA_DATE_TIME)
            currentHourTime = intent?.getStringExtra(AlarmIntentFactory.EXTRA_HOUR_TIME)


            val title = intent?.getStringExtra(AlarmIntentFactory.EXTRA_TASK_TITLE) ?: "Task Reminder"
            val description = intent?.getStringExtra(AlarmIntentFactory.EXTRA_TASK_DESCRIPTION) ?: "زمان انجام فعالیت فرا رسیده است."

            // PendingIntent برای stop action
            val stopPending = TaskNotificationManager.createStopPendingIntent(
                this,
                currentTaskId,
                currentRepeatType,
                currentDateTime,
                currentHourTime
            )

            val notificationId = if (currentTaskId != -1L) currentTaskId.toInt() else 2002

            val notification: Notification = TaskNotificationManager.buildAlarmForegroundNotification(
                context = this,
                title = title,
                message = description,
                stopPendingIntent = stopPending,
                notificationId = notificationId,
                taskId = currentTaskId
            )

            if (Build.VERSION.SDK_INT >= 34) {
                startForeground(notificationId, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK)
            } else {
                startForeground(notificationId, notification)
            }

            playAlarmSound()

            return START_STICKY
        } catch (e: Exception) {
            Log.e(TAG, "onStartCommand error", e)
            stopSelf()
            return START_NOT_STICKY
        }
    }

    private fun playAlarmSound() {
        try {
            stopAlarmSound()
            val uri = TaskNotificationManager.getDefaultAlarmSound()
            mediaPlayer = MediaPlayer().apply {
                setDataSource(this@AlarmRingtoneService, uri)
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_ALARM)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build()
                )
                isLooping = true
                prepare()
                start()
            }
        } catch (e: Exception) {
            Log.e(TAG, "playAlarmSound failed", e)
            stopSelf()
        }
    }

    private fun stopAlarmSound() {
        try {
            mediaPlayer?.let {
                if (it.isPlaying) it.stop()
                it.release()
            }
        } catch (e: Exception) {
            Log.e(TAG, "stopAlarmSound error", e)
        } finally {
            mediaPlayer = null
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopAlarmSound()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
