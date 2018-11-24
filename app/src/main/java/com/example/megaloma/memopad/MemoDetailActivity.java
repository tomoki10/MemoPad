package com.example.megaloma.memopad;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class MemoDetailActivity extends AppCompatActivity {

    //DB呼び出し
    SQLiteHelper sqLiteHelper;
    SQLiteDatabase sqLiteDatabase;

    //保存ボタンの実装
    FloatingActionButton fab;

    //折りたたみツールバー用
    Toolbar toolbar;

    //メモのタイトルと内容
    EditText editTitle;
    EditText editContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_detail);
    }

    //画面で表示するオブジェクトを定義
    @Override
    protected void onResume() {
        super.onResume();
        //DB呼び出し
        sqLiteHelper     = new SQLiteHelper(this);
        sqLiteDatabase = sqLiteHelper.getWritableDatabase();

        //メモの全体を格納する
        List<String> selectRow;

        // メモのtitleを格納する
        String title = "";
        String content = "";

        //メイン画面のItemから遷移している場合
        if(getIntent().hasExtra("ID")){
            final int id = Objects.requireNonNull(getIntent().getExtras()).getInt("ID");
            selectRow = sqLiteHelper.selectMemo(sqLiteDatabase,"title, content", "id",String.valueOf(id));
            title = selectRow.get(0);
            content = selectRow.get(1);
        }

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);

        //メモのタイトルを取得
        editTitle = findViewById(R.id.memo_title);
        editTitle.setText(title);

        //メモの内容を取得
        editContent = findViewById(R.id.memo_content);
        editContent.setText(content);

        //保存ボタンの実装
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //メモのタイトルを取得
                String title = String.valueOf(editTitle.getText());

                //メモの内容を取得
                String content = String.valueOf(editContent.getText());

                //DB呼び出し
                SQLiteHelper sqLiteHelper     = new SQLiteHelper(getBaseContext());
                SQLiteDatabase sqLiteDatabase = sqLiteHelper.getWritableDatabase();

                //メイン画面のItemから遷移している場合
                if(getIntent().hasExtra("ID")){
                    final int selectId = Objects.requireNonNull(getIntent().getExtras()).getInt("ID");
                    sqLiteHelper.selectMemo(sqLiteDatabase,"title", "id", String.valueOf(selectId));
                    sqLiteHelper.updateMemo(sqLiteDatabase, String.valueOf(selectId), title, content);
                }
                //新規ボタンから遷移している場合
                else{
                    Calendar calender = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMdd", Locale.getDefault());
                    sqLiteHelper.insertMemo(sqLiteDatabase, sdf.format(calender.getTime()), title, content);
                }

                Intent intent = new Intent(getApplication(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    //画面で使用したオブジェクトの初期化
    @Override
    protected void onPause() {
        super.onPause();
        sqLiteHelper = null;
        sqLiteDatabase = null;
        fab = null;
        toolbar = null;
        editTitle = null;
        editContent = null;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent = new Intent(getApplication(), MainActivity.class);
        //削除が押下された場合
        if (id == R.id.action_settings_memo_detail) {
            //メイン画面のItemから遷移している場合
            //新規ボタンから遷移している場合、削除せず元の画面に戻る
            if(getIntent().hasExtra("ID")){
                final int deleteId = Objects.requireNonNull(getIntent().getExtras()).getInt("ID");
                sqLiteHelper.deleteMemo(sqLiteDatabase, "id", String.valueOf(deleteId));
            }
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //削除ボタンとして利用
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_memo_detail, menu);
        return true;
    }

}
