package com.example.megaloma.memopad.db;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "Memo")
public class MemoDetail {
    @PrimaryKey(autoGenerate = true)
    public int id = 0;

    public String write_date;
    public String title;
    public String content;
    int fav;
}
