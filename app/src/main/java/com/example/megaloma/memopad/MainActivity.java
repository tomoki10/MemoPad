package com.example.megaloma.memopad;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // DB を操作するためのインスタンス
    private SQLiteHelper sqLiteHelper;
    private SQLiteDatabase sqLiteDatabase;
    // 画面上に表示されるメモを保持するリスト
    ArrayAdapter<String> adapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //メモ追加用のボタン
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //詳細画面に移動
                Intent intent = new Intent(getApplication(), MemoDetailActivity.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        //DB呼び出し
        sqLiteHelper     = new SQLiteHelper(this);
        sqLiteDatabase = sqLiteHelper.getWritableDatabase();

        //データの格納(今後DAO化)
        final List<String> selectId = sqLiteHelper.selectMemo(sqLiteDatabase,"id");
        List<String> selectTitle = sqLiteHelper.selectMemo(sqLiteDatabase,"title");
        List<String> selectDate = sqLiteHelper.selectMemo(sqLiteDatabase,"write_date");

        //取得したDBのカラムをセット
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);

        for(int i=0;i<selectTitle.size();i++){
            adapter.add(selectDate.get(i) + "　" + selectTitle.get(i));
        }
        listView = findViewById(R.id.listView1);
        listView.setAdapter(adapter);

        //Itemごとにクリックリスナーを設定
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //開きたいメモの番号(レコードのID)を付与して詳細画面に移動
                Intent intent = new Intent(getApplication(), MemoDetailActivity.class);
                intent.putExtra("ID", Integer.valueOf(selectId.get(position)));
                startActivity(intent);
            }
        });
    }

    //画面遷移時にオブジェクトを初期化
    @Override
    protected void onPause() {
        super.onPause();
        sqLiteHelper = null;
        sqLiteDatabase = null;
        adapter = null;
        listView = null;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            //戻るボタンでアプリを終了
            moveTaskToBack(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
