package com.example.megaloma.memopad;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.util.List;
import java.util.Objects;

public class MemoDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_detail);

        //DB呼び出し
        SQLiteHelper sqLiteHelper     = new SQLiteHelper(this);
        SQLiteDatabase sqLiteDatabase = sqLiteHelper.getWritableDatabase();

        //メモの全体を格納する
        List<String> selectRow;

        // メモのtitleを格納する
        String title = "";

        //メイン画面のItemから遷移している場合
        if(getIntent().hasExtra("ID")){
            final int id = Objects.requireNonNull(getIntent().getExtras()).getInt("ID");
            selectRow = sqLiteHelper.selectMemo(sqLiteDatabase,"title", "id",String.valueOf(id));
            title = selectRow.get(0);
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);


        //保存ボタンの実装
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //メモのタイトルを取得
                EditText editTitle = findViewById(R.id.memo_title);
                Log.d("TEST onClick",String.valueOf(editTitle.getText()));
                String title = String.valueOf(editTitle.getText());

                //メモの内容を取得
                EditText editContent = findViewById(R.id.memo_content);
                Log.d("TEST onClick",String.valueOf(editContent.getText()));
                String content = String.valueOf(editContent.getText());

                //DB呼び出し
                SQLiteHelper sqLiteHelper     = new SQLiteHelper(getBaseContext());
                SQLiteDatabase sqLiteDatabase = sqLiteHelper.getWritableDatabase();

                //メイン画面のItemから遷移している場合
                if(getIntent().hasExtra("ID")){
                    final int id = Objects.requireNonNull(getIntent().getExtras()).getInt("ID");
                    sqLiteHelper.selectMemo(sqLiteDatabase,"title", "id", String.valueOf(id));
                    sqLiteHelper.updateMemo(sqLiteDatabase, String.valueOf(id), title, content);
                }
                //新規ボタンから遷移している場合
                else{
                    sqLiteHelper.insertMemo(sqLiteDatabase, "20181112", title, content);
                }

                Intent intent = new Intent(getApplication(), MainActivity.class);
                startActivity(intent);

            }
        });

    }
}
