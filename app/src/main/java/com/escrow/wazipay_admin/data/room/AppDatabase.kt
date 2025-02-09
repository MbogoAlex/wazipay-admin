package com.escrow.wazipay_admin.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.escrow.wazipay_admin.data.room.models.DarkMode
import com.escrow.wazipay_admin.data.room.models.UserDetails
import com.escrow.wazipay_admin.data.room.models.UserRole

@Database(entities = [UserDetails::class, DarkMode::class, UserRole::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun appDao(): AppDao

    companion object {
        @Volatile
        private var Instance: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase {
            return Instance ?: synchronized(this) {
                val builder = Room.databaseBuilder(context, AppDatabase::class.java, "wazipayadmin_db")
                    .fallbackToDestructiveMigration()
                builder.build().also { Instance = it }
            }
        }
    }
}