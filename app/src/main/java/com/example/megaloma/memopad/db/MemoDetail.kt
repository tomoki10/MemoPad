package com.example.megaloma.memopad.db

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "Memo")
class MemoDetail {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    var write_date: String? = null
    var title: String? = null
    var content: String? = null
    var fav: Int = 0
}
