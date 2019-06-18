package com.jonasgerdes.stoppelmap.news.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.os.Build
import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import com.jonasgerdes.stoppelmap.news.R

private const val NEWS_TOPIC = "news"


fun subscribeToNewsMessages(context: Context) {
    createNewsNotificationChannel(context)
    FirebaseMessaging.getInstance().subscribeToTopic(NEWS_TOPIC).addOnCompleteListener {
        if (it.isSuccessful) {
            Log.d("Firebase", "Subscription to news successful")
        } else {
            Log.d("Firebase", "Subscription to news failed: ${it.exception?.message}")
        }
    }
}

private fun createNewsNotificationChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val title = context.getString(R.string.news_notification_channel_news_title)
        val descriptionText = context.getString(R.string.news_notification_channel_news_description)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channelId = context.getString(R.string.news_notification_channel_news_id)
        val channel = NotificationChannel(channelId, title, importance)
        channel.description = descriptionText
        val notificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}