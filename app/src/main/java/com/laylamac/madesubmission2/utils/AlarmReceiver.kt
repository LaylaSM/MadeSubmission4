package com.laylamac.madesubmission2.utils

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.laylamac.madesubmission2.BuildConfig
import com.laylamac.madesubmission2.R
import com.laylamac.madesubmission2.model.MovieMdl
import com.laylamac.madesubmission2.view.main.MainActivity
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AlarmReceiver : BroadcastReceiver() {

    private val ID_DAILY = 101
    private val ID_RELEASE = 101
    private val TIME_FORMAT = "HH:mm"
    lateinit var title: String
    lateinit var message: String
    lateinit var type: String
    val TYPE_DAILY = "daily"
    val TYPE_RELEASE = "release"
    val EXTRA_MESSAGE = "message"
    val EXTRA_TYPE = "type"
    var notifyId = 0


    override fun onReceive(context: Context?, intent: Intent?) {
        type = intent?.getStringExtra(EXTRA_TYPE) as String
        message = intent.getStringExtra(EXTRA_MESSAGE) as String
        title = if (type.equals(TYPE_DAILY, ignoreCase = true)) TYPE_DAILY else TYPE_RELEASE
        notifyId = if (type.equals(TYPE_DAILY, ignoreCase = true)) ID_DAILY else ID_RELEASE

        if (type == TYPE_RELEASE) {
            getReleaseToday(context)
        } else if (type == TYPE_DAILY) {
            pushAlarmNotification(context as Context, title, message, notifyId, type, arrayListOf())
        }
        Toast.makeText(context, "$title : $message", Toast.LENGTH_LONG).show()

    }

    private fun pushAlarmNotification(
        context: Context,
        title: String,
        message: String,
        notifyId: Int,
        type: String,
        list: ArrayList<MovieMdl>
    ) {
        val CHANNEL_ID = "fcm_default_channel"
        val CHANNEL_NAME = context.getString(R.string.default_notification_channel_name)
        val intent = Intent(context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra(EXTRA_TYPE, type)
        if (type == TYPE_RELEASE){
            intent.putExtra("movieLis", list)
        }
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        val nmCompat = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notifBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_alarm_black_24dp)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setSound(defaultSound)
            .setContentIntent(pendingIntent)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000, 1000, 10000, 1000, 1000)
            notifBuilder.setChannelId(CHANNEL_ID)
            nmCompat.createNotificationChannel(channel)
        }
        val notifBuild = notifBuilder.build()
        nmCompat.notify(notifyId, notifBuild)
    }

    @SuppressLint("SimpleDateFormat")
    private fun getReleaseToday(context: Context?) {
        val listItemMovie = ArrayList<MovieMdl>()
        val todayDate = Date()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val params = RequestParams()
        params.put("api_key", BuildConfig.TMDB_API_KEY)
        params.put("language", "en-US")
        params.put("primary_release_date.gte", dateFormat.format(todayDate))
        params.put("primary_release_date.lte", dateFormat.format(todayDate))
        val client = AsyncHttpClient()
        val url = BuildConfig.URL_TODAY_RELEASE_MOVIE
        client.get(url, params, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {
                try {
                    val result = String(responseBody)
                    val responseObject = JSONObject(result)
                    val list = responseObject.getJSONArray("results")
                    for (i in 0 until list.length()) {
                        val listMovie = list.getJSONObject(i)
                        val listItem = MovieMdl(listMovie)
                        listItemMovie.add(listItem)
                    }
                    if (listItemMovie.size > 0) {
                        pushAlarmNotification(
                            context as Context,
                            title,
                            message,
                            notifyId,
                            type,
                            listItemMovie
                        )
                    }
                } catch (e: Exception) {
                    Log.d("Exception", e.message!!)
                }

            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray,
                error: Throwable
            ) {
                Log.d("Exception", error.message!!)

            }

        })
    }

    fun setRepeatAlarm(context: Context, type: String, time: String, message: String) {
        if (isDateInvalid(time, TIME_FORMAT)) return
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra(EXTRA_MESSAGE, message)
        intent.putExtra(EXTRA_TYPE, type)
        val listTime = time.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(listTime[0]))
        calendar.set(Calendar.MINUTE, Integer.parseInt(listTime[1]))
        calendar.set(Calendar.SECOND, 0)
        val pendingIntent = PendingIntent.getBroadcast(context, if (type.equals(TYPE_DAILY,
                ignoreCase = true)) ID_DAILY else ID_RELEASE, intent, 0)
        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
        Toast.makeText(context, context.getString(R.string.show_reminder_enabled, type), Toast.LENGTH_SHORT).show()
    }

    private fun isDateInvalid(time: String, format: String): Boolean {
        return try {
            val df = SimpleDateFormat(format, Locale.getDefault())
            df.isLenient = false
            df.parse(time)
            false
        } catch (e: ParseException) {
            true
        }
    }

    fun cancelAlarm(context: Context, type: String) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val requestCode = if (type.equals(TYPE_DAILY, ignoreCase = true))
            ID_DAILY else ID_RELEASE
        val pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0)
        pendingIntent.cancel()
        alarmManager.cancel(pendingIntent)
        Toast.makeText(context,context.getString(R.string.show_reminder_disabled, type), Toast.LENGTH_SHORT).show()
    }

}
