package com.example.megaloma.memopad

import android.annotation.SuppressLint
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import com.example.megaloma.memopad.db.AppRoomDatabase
import com.example.megaloma.memopad.db.MemoDetail
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import java.util.Objects.requireNonNull

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class MemoDetailActivity : AppCompatActivity() {

    //Room呼び出し
    private var appRoomDatabase: AppRoomDatabase? = null
    //SQL非同期実行用
    private var memoDetailGetTask: MemoDetailGetTask? = null

    //保存ボタンの実装
    private var fab: FloatingActionButton? = null

    //折りたたみツールバー用
    private var toolbar: Toolbar? = null

    //メモのタイトルと内容
    private var editTitle: EditText? = null
    private var editContent: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.memo_detail_frame)
    }

    //画面で表示するオブジェクトを定義
    override fun onResume() {
        super.onResume()

        appRoomDatabase = AppRoomDatabase.getDatabase(applicationContext)

        //書き換え
        memoDetailGetTask = MemoDetailGetTask()
        memoDetailGetTask!!.execute(null as Void?)

        //保存ボタンの実装
        fab = findViewById(R.id.detail_fab)
        fab!!.setOnClickListener { view ->
            //メモのタイトルを取得
            val title = editTitle!!.text.toString()

            //メモの内容を取得
            val content = editContent!!.text.toString()

            //挿入・更新用のDAO
            var memoDetail = MemoDetail()

            //メイン画面のItemから遷移している場合
            if (intent.hasExtra("ID")) {
                //既存のメモを更新する
                memoDetail.id = requireNonNull(intent.extras).getInt("ID")
                GlobalScope.launch {
                    memoDetail = requireNonNull<AppRoomDatabase>(appRoomDatabase).memoDetailDao().loadMemo(memoDetail.id)
                    memoDetail.title = title
                    memoDetail.content = content
                    appRoomDatabase!!.memoDetailDao().updateMemo(memoDetail)
                }
            }
            //新規ボタンから遷移している場合
            else {
                //新規の場合は年月日を設定
                val calender = Calendar.getInstance()
                val sdf = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
                memoDetail.write_date = sdf.format(calender.time)
                memoDetail.title = title
                memoDetail.content = content

                GlobalScope.launch {
                    appRoomDatabase!!.memoDetailDao().insertMemo(memoDetail)
                }
            }

            val intent = Intent(application, MainActivity::class.java)
            startActivity(intent)
        }
    }

    //画面で使用したオブジェクトの初期化
    override fun onPause() {
        super.onPause()
        fab = null
        toolbar = null
        memoDetailGetTask = null
    }

    //画面内のオブジェクトを破棄
    override fun onDestroy() {
        super.onDestroy()
        editTitle = null
        editContent = null
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        val intent = Intent(application, MainActivity::class.java)
        //削除が押下された場合
        if (id == R.id.action_settings_memo_detail) {
            //メイン画面のItemから遷移している場合
            //新規ボタンから遷移している場合、削除せず元の画面に戻る
            if (getIntent().hasExtra("ID")) {
                val memoDetail = MemoDetail()
                //削除対象のIDを取得
                memoDetail.id = requireNonNull(getIntent().extras).getInt("ID")
                GlobalScope.launch {
                    appRoomDatabase!!.memoDetailDao().deleteMemo(memoDetail)
                }
            }
            startActivity(intent)
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    //削除ボタンとして利用
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_memo_detail, menu)
        return true
    }

    //メモ詳細の取得
    @SuppressLint("StaticFieldLeak")
    private inner class MemoDetailGetTask : AsyncTask<Void, Void, Boolean>() {

        //メモの全体を格納する
        internal var memoDetail: MemoDetail? = null

        override fun doInBackground(vararg voids: Void): Boolean? {

            //初期化
            memoDetail = null
            if (intent.hasExtra("ID")) {
                val id = requireNonNull(intent.extras).getInt("ID")
                appRoomDatabase = AppRoomDatabase.getDatabase(applicationContext)
                memoDetail = requireNonNull<AppRoomDatabase>(appRoomDatabase).memoDetailDao().loadMemo(id)
            }
            return true
        }

        //画面への書き込みと変数の初期化
        override fun onPostExecute(success: Boolean?) {
            super.onPostExecute(success)

            if (success!!) {

                editTitle = findViewById(R.id.memo_title)
                editContent = findViewById(R.id.memo_content)

                if (intent.hasExtra("ID")) {

                    //メモのタイトルと内容を設定
                    editTitle!!.setText(memoDetail!!.title)
                    editContent!!.setText(memoDetail!!.content)

                    //ツールバーにタイトルを設定
                    toolbar = findViewById(R.id.toolbar)
                    toolbar!!.title = memoDetail!!.title
                    setSupportActionBar(toolbar)
                }

            }
            //更新後にタスクを初期化
            memoDetailGetTask = null
        }

    }

}