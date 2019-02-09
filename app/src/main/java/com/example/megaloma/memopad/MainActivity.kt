package com.example.megaloma.memopad

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.provider.DocumentsContract
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import com.example.megaloma.memopad.db.AppRoomDatabase
import com.example.megaloma.memopad.db.MemoDetail
import com.example.megaloma.memopad.fileutil.FileInOutUtility
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    // 画面上に表示されるメモを保持するリスト
    internal var adapter: ArrayAdapter<String>? = null
    internal var listView: ListView? = null

    //Room呼び出し
    internal var appRoomDatabase: AppRoomDatabase? = null
    internal var memoDetails: List<MemoDetail>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        //メモ追加用のボタン
        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener { view ->
            //詳細画面に移動
            val intent = Intent(application, MemoDetailActivity::class.java)
            startActivity(intent)
        }

        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

    }

    override fun onResume() {
        super.onResume()

        //非同期呼び出し
        memoGetCoroutines()
    }

    //画面遷移時にオブジェクトを初期化
    override fun onPause() {
        super.onPause()
        adapter = null
        listView = null
        memoDetails = null
    }

    override fun onBackPressed() {
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
            //戻るボタンでアプリを終了
            moveTaskToBack(true)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId


        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)

    }

    @SuppressLint("ShowToast")
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val id = item.itemId

        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = DocumentsContract.Document.MIME_TYPE_DIR

        if (id == R.id.nav_upload) {

        } else if (id == R.id.nav_export) {
            //外部ストレージ書き込み許可の確認
            if (FileInOutUtility.isExternalStorageWritable()) {
                //DTOをCSVへ変換する
                Log.d("出力テスト", FileInOutUtility.convertDtoToCsv(memoDetails!!))
                //ファイルへの書き込み
                //FileInOutUtility.memoFileSave(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),);
            } else {
                Toast.makeText(applicationContext, "保存できるストレージがありません", Toast.LENGTH_LONG).show()
            }
        }

        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    private fun memoGetCoroutines(){

        GlobalScope.launch{
            //別画面から遷移している時のため初期化
            val list: List<MemoDetail> = Objects.requireNonNull<AppRoomDatabase>(appRoomDatabase).memoDetailDao().loadMemoAll()

            appRoomDatabase = AppRoomDatabase.getDatabase(applicationContext)
            memoDetails = list

            //取得したDBのカラムをセット
            adapter = ArrayAdapter(applicationContext, android.R.layout.simple_list_item_1)

            for (i in list.indices) {
                adapter!!.add(list[i].write_date + "　" + list[i].title)
            }
            listView = findViewById(R.id.listView1)
            listView!!.adapter = adapter

            //Itemごとにクリックリスナーを設定
            listView!!.setOnItemClickListener { parent, view, position, id ->
                //開きたいメモの番号(レコードのID)を付与して詳細画面に移動
                val intent = Intent(application, MemoDetailActivity::class.java)
                intent.putExtra("ID", Integer.valueOf(list[position].id))
                startActivity(intent)
            }
        }
    }
}
