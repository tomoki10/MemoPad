package com.example.megaloma.memopad;

import android.provider.BaseColumns;

final class DBDesign {

    //インスタンス化しても何も起きないよう作成
    private DBDesign(){}

    //テーブル定義の記入
    static class MemoTable implements BaseColumns {
        static final String _ID = "id";
        static final String MEMO_DATE = "write_date";
        static final String MEMO_TITLE = "title";
        static final String MEMO_CONTENT = "content";
        static final String MEMO_FAV = "fav";
    }
}
