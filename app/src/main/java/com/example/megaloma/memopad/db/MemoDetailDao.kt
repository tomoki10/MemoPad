package com.example.megaloma.memopad.db

import android.arch.persistence.room.*
import android.arch.persistence.room.OnConflictStrategy.IGNORE
import android.arch.persistence.room.OnConflictStrategy.REPLACE

@Dao
interface MemoDetailDao {

    @Query("select * from Memo WHERE id = :id")
    fun loadMemo(id: Int): MemoDetail

    @Query("SELECT * FROM Memo ORDER BY write_date DESC, id DESC")
    fun loadMemoAll(): List<MemoDetail>

    @Insert(onConflict = IGNORE)
    fun insertMemo(memoDetail: MemoDetail)

    @Update(onConflict = REPLACE)
    fun updateMemo(memoDetail: MemoDetail)

    @Delete
    fun deleteMemo(memoDetail: MemoDetail)

}
