package com.example.megaloma.memopad;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.megaloma.memopad.DBDesign.MemoTable;

public class SQLiteHelper extends SQLiteOpenHelper {

    // データーベースのバージョン
    private static final int DATABASE_VERSION = 1;

    // データーベース名
    private static final String DATABASE_NAME = "Memo.db";
    private static final String TABLE_NAME = "MEMO";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    MemoTable._ID + " INTEGER PRIMARY KEY, " +
                    MemoTable.MEMO_DATE + " TEXT, " +
                    MemoTable.MEMO_TITLE + " TEXT, " +
                    MemoTable.MEMO_CONTENT + " TEXT, " +
                    MemoTable.MEMO_FAV + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    SQLiteHelper(Context context) {
        super(context,DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    void memoInsert(SQLiteDatabase db, String date, String title, String content){
        ContentValues values = new ContentValues();
        values.put("write_date", date); //DB設定後修正
        values.put("title", title);
        values.put("content", content);
        db.insert(TABLE_NAME, null, values);
    }

    //SELECTを発行する
    String selectMemo(SQLiteDatabase db){
        String selectSql = "SELECT * FROM " + TABLE_NAME;
        try (Cursor cursor = db.rawQuery(selectSql, null)) {
            return readCursor(cursor);
        }
    }

    //検索結果を読み込む
    private String readCursor(Cursor cursor ){
        //カーソル開始位置を先頭にする
        cursor.moveToFirst();
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= cursor.getCount(); i++) {
            //SQL文の結果から、必要な値を取り出す
            sb.append(cursor.getString(1));
            sb.append(cursor.getString(2));
            sb.append(cursor.getString(3));
            cursor.moveToNext();
        }
        return sb.toString();
    }
}
