package com.example.megaloma.memopad.db;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.NonNull;

@Database(entities = {MemoDetail.class}, version = 1)
public abstract class AppRoomDatabase extends RoomDatabase {

    volatile private static AppRoomDatabase INSTANCE;

    public abstract MemoDetailDao memoDetailDao();

    synchronized public static AppRoomDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context,AppRoomDatabase.class,"Memo2.db")
                        .addMigrations(MIGRATION_1_2)
                        .build();
        }
        return INSTANCE;
    }

    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
        }
    };

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
