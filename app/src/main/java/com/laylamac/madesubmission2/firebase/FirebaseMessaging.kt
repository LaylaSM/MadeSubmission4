package com.laylamac.madesubmission2.firebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.laylamac.madesubmission2.R
import com.laylamac.madesubmission2.view.main.MainActivity

class FirebaseMessaging : FirebaseMessagingService(){

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        if (remoteMessage.notification != null) {
            sendNotification(remoteMessage.notification!!.body, remoteMessage.data)
        }
    }

    private fun sendNotification(body: String?, data: Map<String, String>?) {
        val channelId = "fcm_default_channel"
        val channelName = "MovieReminder"
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra("type", data?.get("type"))
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notifBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentText(body)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            notifBuilder.setChannelId(channelId)
            notificationManager.createNotificationChannel(channel)
        }
        val notifBuild = notifBuilder.build()
        notificationManager.notify(0, notifBuild)
    }
}