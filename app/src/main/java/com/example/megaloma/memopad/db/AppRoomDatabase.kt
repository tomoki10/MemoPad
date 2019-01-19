package com.example.megaloma.memopad.db

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.migration.Migration
import android.content.Context

@Database(entities = [MemoDetail::class], version = 1)
abstract class AppRoomDatabase : RoomDatabase() {

    abstract fun memoDetailDao(): MemoDetailDao

    companion object {

        @Volatile
        private var INSTANCE: AppRoomDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): AppRoomDatabase? =
                INSTANCE ?: synchronized(this){
                    Room.databaseBuilder(context, AppRoomDatabase::class.java, "Memo2.db")
                            .addMigrations(MIGRATION_1_2)
                            .build()
                }

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {}
        }

        fun destroyInstance() {
            INSTANCE = null
        }

    }
}
