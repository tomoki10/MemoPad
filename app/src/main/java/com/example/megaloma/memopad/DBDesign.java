package com.example.megaloma.memopad;

import android.provider.BaseColumns;

public final class DBDesign {

    //インスタンス化しても何も起きないよう作成
    private DBDesign(){}

    //テーブル定義の記入
    public static class MemoTable implements BaseColumns {
        public static final String _ID = "id";
        public static final String COLUMN_NAME_MEMO_DATE = "write_date";
        public static final String COLUMN_NAME_MEMO_TITLE = "memo_title";
        public static final String COLUMN_NAME_MEMO_CONTENT = "memo_content";
        public static final String COLUMN_NAME_MEMO_FAV = "fav";
    }
}
