package com.jiang.learncode2.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.jiang.learncode2.ForumModule.Activity.QuestionActivity;
import com.jiang.learncode2.ForumModule.Activity.SearchActivity;
import com.jiang.learncode2.ForumModule.Adapter.FragmentAdapter;
import com.jiang.learncode2.MessageEvent;
import com.jiang.learncode2.R;
import com.jiang.learncode2.SettingModule.message;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


/**
 * A simple {@link Fragment} subclass.
 */
public class ForumFragment extends Fragment {


    private TabLayout tabLayout;
    private ViewPager viewPager;
    private View view;
    private Button button;
    private TextView textView;
    private String username;
    private message message;
    private Boolean flag = true;
    private Boolean loginFlag = false;

    public ForumFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_forum,container,false);

        tabLayout = view.findViewById(R.id.tab);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        viewPager = view.findViewById(R.id.viewpager);

        button = view.findViewById(R.id.Search_button);
        textView = view.findViewById(R.id.SearchText);



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SearchActivity.class);
                intent.putExtra("search",String.valueOf(textView.getText()));
                //Log.d("search",""+textView.getText().toString());
                startActivity(intent);


            }
        });

        viewPager.setAdapter(new FragmentAdapter(getChildFragmentManager(),tabLayout.getTabCount()));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        InitView();

        return view;
    }




    private void InitView() {
        // FloatingActionButton button1 = (FloatingActionButton) listView.findViewById(R.id.tiwen);
        FloatingActionButton actionC = view.findViewById(R.id.tiwen);
        actionC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (loginFlag == false){
                    Toast.makeText(getContext(),"请先登录",Toast.LENGTH_LONG).show();


                }else {
                    if (flag){
                        //注册EventBus
                        Toast.makeText(getContext(),"1",Toast.LENGTH_LONG).show();
                        EventBus.getDefault().register(ForumFragment.this);
                        flag = false;

                        Intent intent = new  Intent(getActivity(), QuestionActivity.class);

                        intent.putExtra("username",String.valueOf(username));

                        startActivity(intent);
                        flag = true;
                    }
                }

                }
        });
    }


    @Subscribe (threadMode = ThreadMode.MAIN,sticky = true)
    public void onEventMainThread(MessageEvent event) {

        String msg = "onEventMainThread收到了消息：" + event.getUsername();
        //Log.d("harvic", msg);
        //textView.setText(msg);
        username = event.getUsername();
        Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();


    }



    @Override
    public void onDestroy(){
        super.onDestroy();
        EventBus.getDefault().removeAllStickyEvents();
        EventBus.getDefault().unregister(this);//反注册EventBus
        loginFlag = true;
    }
}
