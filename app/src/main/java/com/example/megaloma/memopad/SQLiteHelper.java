package com.example.megaloma.memopad;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import static com.example.megaloma.memopad.DBDesign.MemoTable;

public class SQLiteHelper extends SQLiteOpenHelper {

    // データーベースのバージョン
    private static final int DATABASE_VERSION = 1;

    // データーベース名
    private static final String DATABASE_NAME = "Memo.db";
    private static final String TABLE_NAME = "MEMO";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    MemoTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
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
    List<String> selectMemo(SQLiteDatabase db, String colName){
        String selectSql = "SELECT "+ colName +" FROM " + TABLE_NAME;
        try (Cursor cursor = db.rawQuery(selectSql, null)) {
            return readCursor(cursor, "");
        }
    }

    //条件指定でSELECTを発行する
    List<String> selectMemo(SQLiteDatabase db, String colName, String whereCol,String colCondition){
        String selectSql = String.format("SELECT %s FROM %s WHERE %s = %s", colName, TABLE_NAME, whereCol, colCondition);
        Log.d("TEST",selectSql);
        try (Cursor cursor = db.rawQuery(selectSql, null)) {
            return readCursor(cursor,"where");
        }
    }


    //検索結果を読み込む
    private List<String> readCursor(Cursor cursor, String condition){
        //カーソル開始位置を先頭にする
        cursor.moveToFirst();
        List<String> list = new ArrayList<>();

        switch(condition){
            //条件なしの場合
            case "":
                for (int i = 0; i < cursor.getCount(); i++) {
                    //SQL文の結果から、必要な値を取り出す
                    list.add(cursor.getString(0));
                    cursor.moveToNext();
                }
                break;
            case "where":
                for (int i = 0; i < cursor.getCount(); i++) {
                    list.add(cursor.getString(i));
                    cursor.moveToNext();
                }
                break;
        }
        return list;
    }
}
