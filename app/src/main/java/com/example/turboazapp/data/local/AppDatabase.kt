package com.example.turboazapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.turboazapp.data.local.dao.AnnouncementDao
import com.example.turboazapp.data.local.dao.BrandDao
import com.example.turboazapp.data.local.dao.FavoriteDao
import com.example.turboazapp.data.local.dao.ModelDao
import com.example.turboazapp.data.local.dao.UserDao
import com.example.turboazapp.domain.model.AnnouncementEntity
import com.example.turboazapp.domain.model.BrandEntity
import com.example.turboazapp.domain.model.FavoriteEntity
import com.example.turboazapp.domain.model.ModelEntity
import com.example.turboazapp.domain.model.UserEntity

@Database(
    entities = [
        UserEntity::class,
        BrandEntity::class,
        ModelEntity::class,
        AnnouncementEntity::class,
        FavoriteEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun brandDao(): BrandDao
    abstract fun modelDao(): ModelDao
    abstract fun announcementDao(): AnnouncementDao
    abstract fun favoriteDao(): FavoriteDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = androidx.room.Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}