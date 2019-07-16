package com.jiang.learncode2.SettingModule;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.jiang.learncode2.LoginActivity;
import com.jiang.learncode2.R;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class ModifyPwdActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_pwd);


        setContentView(R.layout.activity_modify_pwd);
        Bmob.initialize(this, "58af32695db2c433af1fe18588fb71e2");

        BmobUser bmobUser = new BmobUser();


        //  TextView textView1 = (TextView) findViewById(R.id.oldpwd);
        //TextView textView2 = (TextView) findViewById(R.id.newpwd);
        //TextView textView3 = (TextView) findViewById(R.id.newpwd2);
        //String newPsw=textView2.getText().toString();
        // user.setRegisterName(s);
        //user.setRegisterPassword(newPsw);
        findViewById(R.id.queren).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView textView1=(TextView)findViewById(R.id.oldpwd);
                TextView textView2 = (TextView) findViewById(R.id.newpwd);
                TextView textView3 = (TextView) findViewById(R.id.newpwd2);
                Intent intent1 = getIntent();
                String s = intent1.getStringExtra("username");
                BmobUser user = new BmobUser();
                String oldPwd=textView1.getText().toString();
                String newPsw=textView2.getText().toString();
                user.setUsername(s);
                user.setPassword(newPsw);
                // String s1=user.getRegisterPassword();
                //  System.out.println("JAVA"+s1);

                BmobUser.updateCurrentUserPassword(oldPwd, newPsw, new UpdateListener() {
                    @Override
                    public void done(BmobException e) {

                    }
                });


                Toast.makeText(getApplicationContext(), "密码修改成功", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ModifyPwdActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });


        //modifyPsw(newPsw);//
        // Intent intent=new Intent(ModifyPswActivity.this,LoginActivity.class);
        // startActivity(intent);            //关闭设置页面
        // 在submit方法中，密码修改成功之后除了把当前页面关了，还要把设置界面也关了，所以用到instance
        // SettingActivity.instance.finish();            //关闭修改密码页面
        // ModifyPswActivity.this.finish();
        // }


        //  Toast.makeText(ModifyPwdActivity.this,""+string,Toast.LENGTH_LONG).show();
    }

}
