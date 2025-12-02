package com.example.turboazapp.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.turboazapp.R
import com.example.turboazapp.presentation.ui.fragment.MainActivity

class NotificationWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    override fun doWork(): Result {
        // Notification göstər
        showScheduledNotification()
        return Result.success()
    }

    private fun showScheduledNotification() {
        val channelId = "turbo_scheduled_channel"
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Android 8.0+ üçün Channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Turbo.az Xatırlatmalar",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Planlaşdırılmış bildirişlər"
            }
            notificationManager.createNotificationChannel(channel)
        }

        // MainActivity-ə keçid
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        // Notification yarat
        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.turboicon)
            .setContentTitle("🚗 Turbo.az")
            .setContentText("Yeni elanlar sizi gözləyir! 🔥")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("Maraq dairənizə uyğun yeni avtomobil elanları əlavə edilib. İndi baxın!")
            )
            .build()

        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }
}