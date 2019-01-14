package com.example.megaloma.memopad;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.megaloma.memopad.db.AppRoomDatabase;
import com.example.megaloma.memopad.db.MemoDetail;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class MemoDetailActivity extends AppCompatActivity {

    //Room呼び出し
    AppRoomDatabase appRoomDatabase;
    //SQL非同期実行用
    MemoDetailGetTask memoDetailGetTask;
    MemoInsertTask memoInsertTask;
    MemoUpdateTask memoUpdateTask;
    MemoDeleteTask memoDeleteTask;

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

        //書き換え
        memoDetailGetTask = new MemoDetailGetTask();
        memoDetailGetTask.execute((Void) null);

        //保存ボタンの実装
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            //メモのタイトルを取得
            String title = String.valueOf(editTitle.getText());

            //メモの内容を取得
            String content = String.valueOf(editContent.getText());

            //挿入・更新用のDAO
            MemoDetail memoDetail = new MemoDetail();
            memoDetail.title = title;
            memoDetail.content = content;

            //メイン画面のItemから遷移している場合
            if(getIntent().hasExtra("ID")){
                //既存のメモを更新する
                memoDetail.id = Objects.requireNonNull(getIntent().getExtras()).getInt("ID");
                memoUpdateTask = new MemoUpdateTask();
                memoUpdateTask.execute(memoDetail);
            }
            //新規ボタンから遷移している場合
            else{
                //新規の場合は年月日を設定
                Calendar calender = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
                memoDetail.write_date = sdf.format(calender.getTime());
                memoInsertTask = new MemoInsertTask();
                memoInsertTask.execute(memoDetail);
            }

            Intent intent = new Intent(getApplication(), MainActivity.class);
            startActivity(intent);
        });
    }

    //画面で使用したオブジェクトの初期化
    @Override
    protected void onPause() {
        super.onPause();
        fab = null;
        toolbar = null;
        editTitle = null;
        editContent = null;
        memoDetailGetTask = null;
        memoInsertTask = null;
        memoUpdateTask = null;
        memoDeleteTask = null;
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
//                sqLiteHelper.deleteMemo(sqLiteDatabase, "id", String.valueOf(deleteId));
                MemoDetail memoDetail = new MemoDetail();
                memoDetail.id =  deleteId;
                memoDeleteTask = new MemoDeleteTask();
                memoDeleteTask.execute(memoDetail);
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

    //メモ削除用
    @SuppressLint("StaticFieldLeak")
    private class MemoDeleteTask extends AsyncTask<MemoDetail, Void, Boolean>{
        @Override
        protected Boolean doInBackground(MemoDetail... memoDetail) {
            appRoomDatabase = AppRoomDatabase.getDatabase(getApplicationContext());
            appRoomDatabase.memoDetailDao().deleteMemo(memoDetail[0]);
            return true;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            super.onPostExecute(success);
            memoDeleteTask = null;
        }
    }

    //メモ更新用
    @SuppressLint("StaticFieldLeak")
    private class MemoUpdateTask extends AsyncTask<MemoDetail, Void, Boolean>{
        MemoDetail mMemoDetail;

        @Override
        protected Boolean doInBackground(MemoDetail... memoDetail) {
            appRoomDatabase = AppRoomDatabase.getDatabase(getApplicationContext());
            //DAOごと更新するため、一度呼び出す
            mMemoDetail = appRoomDatabase.memoDetailDao().loadMemo(memoDetail[0].id);
            mMemoDetail.title = String.valueOf(editTitle.getText());
            mMemoDetail.content = String.valueOf(editContent.getText());
            appRoomDatabase.memoDetailDao().updateMemo(mMemoDetail);
            return true;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            super.onPostExecute(success);
            memoUpdateTask = null;
        }

    }

    //メモ挿入用
    @SuppressLint("StaticFieldLeak")
    private class MemoInsertTask extends AsyncTask<MemoDetail, Void, Boolean>{
        @Override
        protected Boolean doInBackground(MemoDetail... memoDetail) {
            appRoomDatabase = AppRoomDatabase.getDatabase(getApplicationContext());
            appRoomDatabase.memoDetailDao().InsertMemo(memoDetail[0]);
            return true;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            super.onPostExecute(success);
            memoInsertTask = null;
        }

    }

    //メモ詳細の取得
    @SuppressLint("StaticFieldLeak")
    private class MemoDetailGetTask extends AsyncTask<Void, Void, Boolean>{

        //メモの全体を格納する
        MemoDetail memoDetail;

        @Override
        protected Boolean doInBackground(Void... voids) {

            //初期化
            memoDetail = null;
            if(getIntent().hasExtra("ID")){
                int id = Objects.requireNonNull(getIntent().getExtras()).getInt("ID");
                appRoomDatabase = AppRoomDatabase.getDatabase(getApplicationContext());
                memoDetail = appRoomDatabase.memoDetailDao().loadMemo(id);
            }
            return true;
        }

        //画面への書き込みと変数の初期化
        @Override
        protected void onPostExecute(Boolean success) {
            super.onPostExecute(success);

            if(success){

                editTitle = findViewById(R.id.memo_title);
                editContent = findViewById(R.id.memo_content);

                if(getIntent().hasExtra("ID")){


                    //メモのタイトルと内容を設定
                    editTitle.setText(memoDetail.title);
                    editContent.setText(memoDetail.content);

                    //ツールバーにタイトルを設定
                    toolbar = findViewById(R.id.toolbar);
                    toolbar.setTitle(memoDetail.title);
                    setSupportActionBar(toolbar);
                }

            }
            //更新後にタスクを初期化
            memoDetailGetTask = null;
        }

    }

}