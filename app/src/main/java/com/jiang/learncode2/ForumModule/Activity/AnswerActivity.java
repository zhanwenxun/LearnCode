package com.jiang.learncode2.ForumModule.Activity;

import android.content.Intent;
import android.icu.util.LocaleData;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.jiang.learncode2.DataBase.bean.Answer;
import com.jiang.learncode2.DataBase.bean.Question;
import com.jiang.learncode2.ForumModule.Adapter.answerAdapter;
import com.jiang.learncode2.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class AnswerActivity extends AppCompatActivity {

    private ListView listView;
    private TextView textView1;
    private TextView textView2;
    private String id;
    private String topic;
    private String description;
    private Intent intent;
    private answerAdapter answerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);
        Bmob.initialize(this, "58af32695db2c433af1fe18588fb71e2");

        textView1 = findViewById(R.id.jianjie);
        textView2 = findViewById(R.id.miaoshu);

        listView = findViewById(R.id.answerlistview);

        FloatingActionButton actionC =findViewById(R.id.answer);
        actionC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent11 = new Intent(AnswerActivity.this, newAnswerActivity.class);
                intent11.putExtra("topic",intent.getStringExtra("topic"));
                intent11.putExtra("description",intent.getStringExtra("description"));
                intent11.putExtra("id",intent.getStringExtra("id"));
                startActivity(intent11);
            }
        });


        intent = getIntent();
        id = intent.getStringExtra("id");
        topic = intent.getStringExtra("topic");
        description = intent.getStringExtra("description");

        Log.d("AA",id+"\n"+topic+"\n"+description);

        textView1.setText(topic);
        textView2.setText(description);

        final List<Map<String, Object>> list = getData();

        System.out.println(list.isEmpty());
        Log.d("map2", list.toString());

        answerAdapter = new answerAdapter(AnswerActivity.this,list);
        listView.setAdapter(answerAdapter);


    }

    private List<Map<String, Object>> getData() {

        final List<Map<String, Object>> list_sum = new ArrayList<Map<String, Object>>();

        list_sum.clear();

        BmobQuery<Answer> query = new BmobQuery<Answer>();

        //query.addWhereEqualTo("Q_topic", "1111");
        Log.d("id111",id);
        query.addWhereEqualTo("A_Q_ID",Integer.valueOf(id));

        Answer answer = new Answer();
        //按照时间降序
        query.order("_createdAt");
        //执行查询，第一个参数为上下文，第二个参数为查找的回调
        query.findObjects(new FindListener<Answer>() {

            @Override
            public void done(List<Answer> lists, BmobException e) {
                if (lists.isEmpty()){
                    Toast.makeText(AnswerActivity.this,"不存在任何回答",Toast.LENGTH_LONG).show();
                }else {
                    //Log.d("aa",lists.toString());
                    for (Answer list : lists) {

                        Map<String, Object> map = new HashMap<String, Object>();
                        Log.d("answer",list.getA_U_name());
                        map.put("id",String.valueOf(list.getA_Q_ID()));
                        Log.d("idaa",""+list.getA_Q_ID());
                        map.put("username",String.valueOf(list.getA_U_name()));
                        map.put("content",String.valueOf(list.getA_content()));
                        map.put("date",list.getCreatedAt());
                        Log.d("map",map.toString());
                        list_sum.add(map);
                        answerAdapter.notifyDataSetChanged();

                        Log.d("map1",list_sum.toString());
                    }
                }
            }
        });

        Log.d("map4",list_sum.toString());
        return list_sum;
    }
}
