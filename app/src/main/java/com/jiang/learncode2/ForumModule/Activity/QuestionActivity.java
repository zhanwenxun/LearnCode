package com.jiang.learncode2.ForumModule.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.jiang.learncode2.DataBase.bean.Question;
import com.jiang.learncode2.R;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class QuestionActivity extends AppCompatActivity {
    private String str1;
    private String str2;
    private TextView textView1;
    private TextView textView2;
    private TextView textView3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        Bmob.initialize(this, "58af32695db2c433af1fe18588fb71e2");
        //editText3=findViewById(R.id.search);
        Intent i = getIntent();
        final String s = i.getStringExtra("username");

        textView1 = findViewById(R.id.jianjie);
        textView2 = findViewById(R.id.miaoshu);
        textView3 = findViewById(R.id.raised);

        textView3.setText(String.valueOf(s));

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                str1 = textView1.getText().toString();
                str2 = textView2.getText().toString();

                textView2.setMovementMethod(ScrollingMovementMethod.getInstance());
                // Log.d("B",str1+str2);
                Question question=new Question();
                question.setQ_topic(str1);
                question.setQ_description(str2);
                question.setQ_raised_name(s);
                Toast.makeText(QuestionActivity.this,""+str1+"\n"+str2,Toast.LENGTH_LONG).show();
                question.save(new SaveListener<String>() {
                    @Override
                    public void done(String objectId, BmobException e) {
                        Log.d("KKK","***");
                        if(e==null){
                            Toast.makeText(QuestionActivity.this,"添加数据成功，返回objectId为："+objectId,Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(QuestionActivity.this,"添加数据失败：" + e.getMessage(),Toast.LENGTH_LONG).show();

                        }
                    }
                });

            }
        });
    }

}




