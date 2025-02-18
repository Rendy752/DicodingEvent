package com.example.dicodingevent.ui.settings

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.dicodingevent.R
import com.example.dicodingevent.ui.MainActivity
import com.example.dicodingevent.utils.Constants

class ReminderBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val sharedPrefs = context.getSharedPreferences("reminder_prefs", Context.MODE_PRIVATE)
        val eventName = sharedPrefs.getString("event_name", "")
        val eventTime = sharedPrefs.getString("event_time", "")

        if (!eventName.isNullOrEmpty() && !eventTime.isNullOrEmpty()) {
            showReminderNotification(context, eventName, eventTime)
        }
    }

    private fun showReminderNotification(context: Context, eventName: String, eventTime: String) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

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
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
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