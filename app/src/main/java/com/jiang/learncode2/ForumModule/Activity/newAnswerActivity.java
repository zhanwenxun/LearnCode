package com.jiang.learncode2.ForumModule.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.jiang.learncode2.DataBase.bean.Answer;
import com.jiang.learncode2.R;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

public class newAnswerActivity extends AppCompatActivity {

    private String str1;
    private String str2;
    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private String topic;
    private String description;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_answer);

        Bmob.initialize(this, "58af32695db2c433af1fe18588fb71e2");

        Intent intent = getIntent();

        topic = intent.getStringExtra("topic");
        description = intent.getStringExtra("description");
        id = intent.getStringExtra("id");
        //editText3=findViewById(R.id.search);


        textView1 = findViewById(R.id.huida);
        textView2 = findViewById(R.id.jianjie);
        textView3 = findViewById(R.id.miaoshu);

        textView2.setText(topic);
        textView3.setText(description);
        // textView2 = findViewById(R.id.miaoshu);


        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                str1 = textView1.getText().toString();

                // Log.d("B",str1+str2);
                Answer answer=new Answer();
                answer.setA_content(str1);
                answer.setA_Q_ID(Integer.valueOf(id));


                answer.setA_U_name("test");
                // Question question=new Question();
                //question.setQ_topic(str1);
                //question.setQ_description(str2);
                Toast.makeText(newAnswerActivity.this,""+str1,Toast.LENGTH_LONG).show();
                answer.save(new SaveListener<String>() {
                    @Override
                    public void done(String objectId, BmobException e) {
                        Log.d("KKK","***");
                        if(e==null){
                            Toast.makeText(newAnswerActivity.this,"添加数据成功，返回objectId为："+objectId,Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(newAnswerActivity.this,"添加数据失败：" + e.getMessage(),Toast.LENGTH_LONG).show();

                        }
                    }
                });

            }
        });
    }
    //  private void showToast(String msg) {
    //  Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    //}
}

