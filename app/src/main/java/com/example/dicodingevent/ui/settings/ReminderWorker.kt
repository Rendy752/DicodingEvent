package com.example.dicodingevent.ui.settings

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.dicodingevent.R
import com.example.dicodingevent.ui.MainActivity
import com.example.dicodingevent.utils.Constants

class ReminderWorker(private val context: Context, workerParams: WorkerParameters):
    Worker(context, workerParams) {

    override fun doWork(): Result {
        val sharedPrefs = context.getSharedPreferences("reminder_prefs", Context.MODE_PRIVATE)
        val eventName = sharedPrefs.getString("event_name", "")?: ""
        val eventTime = sharedPrefs.getString("event_time", "")?: ""

        showReminderNotification(eventName, eventTime)

        return Result.success()
    }

    private fun showReminderNotification(eventName: String, eventTime: String) {
        val notificationManager =
            context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val channelId = "reminder_channel"
        val channelName = "Daily Reminder"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notificationBuilder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_notifications_black_24dp)
            .setContentTitle("Upcoming Event Reminder")
            .setContentText("$eventName at $eventTime")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        notificationManager.notify(Constants.REMINDER_NOTIFICATION_ID, notificationBuilder.build())
    }
}