package com.example.megaloma.memopad;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
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
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.megaloma.memopad.db.AppRoomDatabase;
import com.example.megaloma.memopad.db.MemoDetail;

import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // 画面上に表示されるメモを保持するリスト
    ArrayAdapter<String> adapter;
    ListView listView;

    //Room呼び出し
    AppRoomDatabase appRoomDatabase;
    //SQL非同期実行用
    MemoGetTask memoGetTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //メモ追加用のボタン
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            //詳細画面に移動
            Intent intent = new Intent(getApplication(), MemoDetailActivity.class);
            startActivity(intent);
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

        //書き換え
        memoGetTask = new MemoGetTask();
        memoGetTask.execute((Void) null);

    }

    //画面遷移時にオブジェクトを初期化
    @Override
    protected void onPause() {
        super.onPause();
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

        if (id == R.id.nav_upload) {
            // Handle the camera action
        } else if (id == R.id.nav_export) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @SuppressLint("StaticFieldLeak")
    private class MemoGetTask extends AsyncTask<Void, Void, Boolean> {

        List<MemoDetail> list;

        @Override
        protected Boolean doInBackground(Void... voids) {
            //別画面から遷移している時のため初期化
            list = null;

            appRoomDatabase = AppRoomDatabase.getDatabase(getApplicationContext());
            list = Objects.requireNonNull(appRoomDatabase).memoDetailDao().loadMemoAll();
            return true;
        }

        //画面への書き込みと変数の初期化
        @Override
        protected void onPostExecute(Boolean success) {
            super.onPostExecute(success);

            if(success){
                //取得したDBのカラムをセット
                adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1);

                for(int i=0;i<list.size();i++){
                    adapter.add(list.get(i).getWrite_date() + "　" + list.get(i).getTitle());
                }
                listView = findViewById(R.id.listView1);
                listView.setAdapter(adapter);

                //Itemごとにクリックリスナーを設定
                listView.setOnItemClickListener((parent, view, position, id) -> {
                    //開きたいメモの番号(レコードのID)を付与して詳細画面に移動
                    Intent intent = new Intent(getApplication(), MemoDetailActivity.class);
                    intent.putExtra("ID", Integer.valueOf(list.get(position).getId()));
                    startActivity(intent);
                });
            }
            //更新後にタスクを初期化
            memoGetTask = null;
        }
    }
}
