package com.example.megaloma.memopad;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.megaloma.memopad.DBDesign.MemoTable;

public class SQLiteHelper extends SQLiteOpenHelper {

    // データーベースのバージョン
    private static final int DATABASE_VERSION = 1;

    // データーベース名
    private static final String DATABASE_NAME = "Memo.db";
    private static final String TABLE_NAME = "MEMO_DB";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    MemoTable._ID + " INTEGER PRIMARY KEY," +
                    MemoTable.MEMO_DATE + "TEXT, " +
                    MemoTable.MEMO_TITLE + " TEXT, " +
                    MemoTable.MEMO_CONTENT + " TEXT, " +
                    MemoTable.MEMO_FAV + "TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    public SQLiteHelper(Context context) {
        super(context,DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
