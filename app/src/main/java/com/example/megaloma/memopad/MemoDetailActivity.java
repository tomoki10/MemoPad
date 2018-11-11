package com.example.megaloma.memopad;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.Objects;

public class MemoDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("AAA");
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //DB呼び出し
        SQLiteHelper sqLiteHelper     = new SQLiteHelper(this);
        SQLiteDatabase sqLiteDatabase = sqLiteHelper.getWritableDatabase();

        //メイン画面のItemから遷移している場合
        if(getIntent().hasExtra("ID")){
            final int id = Objects.requireNonNull(getIntent().getExtras()).getInt("ID");
            sqLiteHelper.selectMemo(sqLiteDatabase,"title", "id",String.valueOf(id));
        }
        //新規ボタンから遷移している場合
        else{
            //後で記載追加
        }

    }
}
