package com.example.turboazapp.domain.model

data class Notification(
    val id: String = "",
    val userId: String = "",
    val title: String = "",
    val message: String = "",
    val type: NotificationType = NotificationType.GENERAL,
    val carId: String? = null,
    val isRead: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

enum class NotificationType {
    GENERAL,        // Ümumi
    PRICE_DROP,     // Qiymət düşüb
    NEW_CAR,        // Yeni elan
    FAVORITE_SOLD,  // Sevimli elan satılıb
    MESSAGE         // Mesaj
}
