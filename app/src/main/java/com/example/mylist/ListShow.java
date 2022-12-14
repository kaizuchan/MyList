package com.example.mylist;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.content.Intent;

public class ListShow extends AppCompatActivity{

    private TextView List_title;
    private ListView List_VIew;
    String[] listKey,listId;

    //インテントからwordsを取得
    Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview);

        //IDから目的のウィジットを得る
        List_title = (TextView) findViewById(R.id.mylistTitle);
        List_VIew = (ListView) findViewById(R.id.myListView);

        extras = getIntent().getExtras();
        if (extras != null){
            String list_id = extras.getString("id");
            String list_key = extras.getString("words");
            // String list_date = extras.getString("datetime");
            listId = list_id.split(",");
            listKey = list_key.split(",");
        }

        // ヘッダ設定
        String head_title = "メモ一覧(" + listKey.length + "件)";
        List_title.setText(head_title);

        // リストアダプタを作成
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_expandable_list_item_1, listKey);
        // リストアダプターをリストビューにセット
        List_VIew.setAdapter(myAdapter);

        // リスト選択処理
        List_VIew.setOnItemClickListener( new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?>parent,View v,int position, long id){
                Log.d("onItemClick","position:" + String.valueOf(position));
                String select_number = listId[position];

                // ListShowアクティビティを起動
                Intent result_act = new Intent(ListShow.this, MainActivity.class);
                result_act.putExtra("select_number", select_number);
                startActivity(result_act);
            }
        });
    }

    // ボタンの処理
    public void buttonMethod(View myButton){
        Intent result_act = new Intent(ListShow.this, MainActivity.class);
        startActivity(result_act);
    }
}
