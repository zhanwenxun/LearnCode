package com.jiang.learncode2;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.jiang.learncode2.Fragment.ForumFragment;
import com.jiang.learncode2.Fragment.HomeFragment;
import com.jiang.learncode2.Fragment.SettingFragment;

import cn.bmob.v3.Bmob;
import de.greenrobot.event.EventBus;

public class MainActivity extends AppCompatActivity {


    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout;
    private HomeFragment homeFragment;
    private ForumFragment forumFragment;
    private SettingFragment settingFragment;
    private Bundle userbundle;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //EventBus.getDefault().register(this);



        bottomNavigationView = findViewById(R.id.main_nav);
        frameLayout = findViewById(R.id.main_frame);

        homeFragment = new HomeFragment();
        forumFragment = new ForumFragment();
        settingFragment = new SettingFragment();

        Bmob.initialize(this, "58af32695db2c433af1fe18588fb71e2");

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()){
                    //编码模块fragment
                    case R.id.nav_home:
                        //forumFragment.setArguments();
                        setFragment(homeFragment);

                        return true;

                    //社区模块fragment
                    case R.id.nav_forum:


                        setFragment(forumFragment);
                        return true;

                    //设置模块fragment
                    case R.id.nav_setting:

                        if (getSupportActionBar() != null){
                            getSupportActionBar().show();

                        }
                        setFragment(settingFragment);
                        return true;


                    default:
                        return false;
                }

            }
        });




    }

    //导航栏切换fragment
    private void setFragment(Fragment fragment) {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        fragmentTransaction.replace(R.id.main_frame,fragment);
        fragmentTransaction.commit();

    }






}
