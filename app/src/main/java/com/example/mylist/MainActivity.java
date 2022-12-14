package com.example.mylist;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.content.Intent;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    SQLiteDatabase db;
    EditText edit_key,edit_text;

    Intent result_act;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edit_key = (EditText)findViewById(R.id.EditKey);
        edit_text = (EditText)findViewById(R.id.EditText01);

        // データベースを作成
        String dbStr = "data/data/" + getPackageName() + "/nhs00764.db";
        db = SQLiteDatabase.openOrCreateDatabase(dbStr, null);

        // テーブル作成用のクエリ文
        // String query_table1 = "DROP TABLE IF EXISTS memopad";
        // String query_table2 = "CREATE TABLE memopad(id INTEGER PRIMARY KEY AUTOINCREMENT,key1 TEXT,memo TEXT, write_date TEXT)";
        String query_table = "CREATE TABLE IF NOT EXISTS memopad(id INTEGER PRIMARY KEY AUTOINCREMENT,key1 TEXT,memo TEXT, write_date TEXT)";

        // テーブルの作成
        //db.execSQL(query_table1);
        //db.execSQL(query_table2);
        db.execSQL(query_table);

        // レコードの追加
        // String write_dateStr = now_date();
        // String query_record_set[] = {
        //        "INSERT INTO memopad(key1,memo,write_date)VALUES('test1','test01','"+ write_dateStr +"')",
        //        "INSERT INTO memopad(key1,memo,write_date)VALUES('test2','test02','"+ write_dateStr +"')",
        //        "INSERT INTO memopad(key1,memo,write_date)VALUES('test3','test03','"+ write_dateStr +"')"
        //};
        //for (int i=0; i<query_record_set.length; i++){
        ///    db.execSQL(query_record_set[i]);
        //}

        // インテントからキーを取得
        Bundle extras = getIntent().getExtras();
        if (extras != null){
            String strId = extras.getString("select_number");
            Integer intId = new Integer(strId);

            String query_select = "SELECT * FROM memopad WHERE id ='" + intId + "'";

            // DB検索実行
            Cursor db_row = db.rawQuery(query_select, null);

            // レコードを取り出しながらフィールドデータを取得
            String set_memo = "";
            String set_key = "";
            while (db_row.moveToNext()){
                int index_key1 = db_row.getColumnIndex("key1");
                int index_memo = db_row.getColumnIndex("memo");
                String key1 = db_row.getString(index_key1);
                String memo = db_row.getString(index_memo);

                set_key += key1;
                set_memo += memo;
            }
            edit_key.setText(set_key);
            edit_text.setText(set_memo);
        }
    }

    public void functionKeyWrite(View v){
        String keyAdd = edit_key.getText().toString();
        String memoAdd = edit_text.getText().toString();

        // レコードの追加
        String write_dateStr = now_date();
        String query_record_set = "INSERT INTO memopad(key1,memo,write_date)VALUES('"+ keyAdd +"','"+ memoAdd +"','"+ write_dateStr +"')";
        db.execSQL(query_record_set);

        Toast myToast = Toast.makeText(this,keyAdd + "をキーにして書き込みが完了しました。",Toast.LENGTH_LONG);
        myToast.show();
    }
    public void functionKeyClear(View v){
        edit_text.setText("");
    }
    public void functionKeyList(View v){
        // レコード検索
        String query_select = "SELECT * FROM memopad";

        // DB検索実行
        Cursor db_row = db.rawQuery(query_select, null);

        // レコードを取り出しながらフィールドデータを取得
        String strId = "";
        String words = "";
        while (db_row.moveToNext()){
            int index_id = db_row.getColumnIndex("id");
            int index_key1 = db_row.getColumnIndex("key1");
            int index_write_date = db_row.getColumnIndex("write_date");
            String strIndex = db_row.getString(index_id);
            String key1 = db_row.getString(index_key1);
            String write_date = db_row.getString(index_write_date);

            strId += strIndex + ",";
            words += key1 + "   " + write_date + ",";
        }

        // ListViewアクティビティを起動
        result_act = new Intent(MainActivity.this, ListShow.class);
        result_act.putExtra("id",strId);
        result_act.putExtra("words", words);
        startActivity(result_act);

    }
    public void functionKeyDelete(View v){
        // インテントからキーを取得
        Bundle extras = getIntent().getExtras();
        if (extras != null){
            String strId = extras.getString("select_number");
            Integer intId = new Integer(strId);

            // レコードの削除
            String query_record_delete = "DELETE FROM memopad WHERE id='" + intId + "'";
            db.execSQL(query_record_delete);

            Toast myToast = Toast.makeText(this, "選択したリストを削除しました。",Toast.LENGTH_LONG);
            myToast.show();
        }
        else{
            Toast myToast = Toast.makeText(this,"指定されたキーのリストはありません。",Toast.LENGTH_LONG);
            myToast.show();
        }
    }

    public void functionKeyUpdate(View v) {
        String keyUpdate = edit_key.getText().toString();
        String memoUpdate = edit_text.getText().toString();
        // インテントからキーを取得
        Bundle extras = getIntent().getExtras();
        if (extras != null){
            String strId = extras.getString("select_number");
            Integer intId = new Integer(strId);

            // レコードの削除
            String query_record_update = "UPDATE memopad SET key1='" + keyUpdate + "',memo='" + memoUpdate + "' WHERE id='" + intId + "'";
            db.execSQL(query_record_update);

            Toast myToast = Toast.makeText(this, "選択したリストを更新しました。",Toast.LENGTH_LONG);
            myToast.show();
        }
        else{
            Toast myToast = Toast.makeText(this,"指定されたキーのリストはありません。",Toast.LENGTH_LONG);
            myToast.show();
        }
    }

    private String now_date(){
        // 現在日取得
        Date today = new Date();

        // Calendarクラスオブジェクトの生成
        Calendar cal = Calendar.getInstance();

        // Calendarオブジェクトに現在日付設定
        cal.setTime(today);

        // 年月日時分秒取得
        int yy = cal.get(Calendar.YEAR);
        int mm = cal.get(Calendar.MONTH)+1;
        int dd = cal.get(Calendar.DATE);
        int hour = cal.get(Calendar.HOUR);
        int minute = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);

        String now_dateStr = yy + "/" + mm + "/" + dd + "   " + hour + ":" + minute + ":" + second;
        return now_dateStr;

    }
}