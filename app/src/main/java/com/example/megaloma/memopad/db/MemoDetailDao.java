package com.example.megaloma.memopad.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.IGNORE;
import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface MemoDetailDao {

    @Query("select * from Memo WHERE id = :id")
    MemoDetail loadMemo(int id);

    @Query("SELECT * FROM Memo ORDER BY write_date DESC, id DESC")
    List<MemoDetail> loadMemoAll();


    @Insert(onConflict = IGNORE)
    void InsertMemo(MemoDetail memoDetail);

    @Update(onConflict = REPLACE)
    void updateMemo(MemoDetail memoDetail);

    @Delete
    void deleteMemo(MemoDetail memoDetail);


}
