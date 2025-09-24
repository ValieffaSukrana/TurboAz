package com.example.turboazapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.turboazapp.domain.model.AnnouncementEntity

@Dao
interface AnnouncementDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnnouncement(announcement: AnnouncementEntity)

    @Query("SELECT * FROM announcements")
    suspend fun getAllAnnouncements(): List<AnnouncementEntity>

    @Query("SELECT * FROM announcements WHERE userId = :userId")
    suspend fun getAllAnnouncementsByUserId(userId: Int): List<AnnouncementEntity>

    @Query(
        """
        SELECT * FROM announcements
        WHERE (:brandId is null or brandId = :brandId)
        and (:modelId is null or modelId = :modelId)
        and (:city is null or city = :city)
        and (price between :minPrice and :maxPrice)
        ORDER BY createDate DESC
    """
    )
    suspend fun searchAnnouncements(
        brandId: Int,
        modelId: Int,
        minPrice: Double,
        maxPrice: Double,
        city: String
    ): List<AnnouncementEntity>
}