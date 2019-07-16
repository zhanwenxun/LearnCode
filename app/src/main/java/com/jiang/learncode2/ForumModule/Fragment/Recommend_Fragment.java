package com.jiang.learncode2.ForumModule.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.jiang.learncode2.DataBase.bean.Question;
import com.jiang.learncode2.ForumModule.Activity.AnswerActivity;
import com.jiang.learncode2.ForumModule.Activity.QuestionActivity;
import com.jiang.learncode2.ForumModule.Adapter.ListAdapter;
import com.jiang.learncode2.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

import static cn.bmob.v3.Bmob.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class Recommend_Fragment extends Fragment {


    private ListView listView;
    private ListAdapter listAdapter;
    private View rootView;
    private List<Map<String, Object>> list;
    private SwipeRefreshLayout swipeRefreshLayout;

    public Recommend_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        rootView = inflater.inflate(R.layout.fragment_recommend_, container, false);
        listView = rootView.findViewById(R.id.listview);
        swipeRefreshLayout = rootView.findViewById(R.id.swipe);

        final List<Map<String, Object>> list = getData();

        listAdapter = new ListAdapter(rootView.getContext(), list);

        listView.setAdapter(listAdapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        listAdapter.notifyDataSetChanged();
                        listView.setAdapter(listAdapter);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                },100);

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //通过view获取其内部的组件，进而进行操作
                String text = (String) ((TextView) view.findViewById(R.id.description)).getText();
                //大多数情况下，position和id相同，并且都从0开始
                String showText = "点击第" + position + "项，文本内容为：" + list.get(position).toString()+" , ID为：" + id;
                Map<String,Object> eg = list.get(position);
                String showText1 = ""+eg.get("topic");

                Intent intent = new Intent(getActivity().getApplicationContext(), AnswerActivity.class);
                String egid = ""+eg.get("id");
                intent.putExtra("id",""+eg.get("id"));
                intent.putExtra("topic",""+eg.get("topic"));
                intent.putExtra("description",""+eg.get("description"));
                startActivity(intent);

                Log.d("Toast",showText1);
                Toast.makeText(getApplicationContext(), showText, Toast.LENGTH_LONG).show();

            }
        });

        return rootView;
    }

    private List<Map<String, Object>> getData() {
        final List<Map<String, Object>> list_sum = new ArrayList<Map<String, Object>>();

        BmobQuery<Question> query = new BmobQuery<Question>();

        //query.addWhereEqualTo("Q_topic", "1111");
        query.addWhereEqualTo("Q_type",Integer.valueOf(1));

        //Question question = new Question();
        //按照时间降序
        query.order("Q_ID");
        //执行查询，第一个参数为上下文，第二个参数为查找的回调
        query.findObjects(new FindListener<Question>() {

            @Override
            public void done(List<Question> lists, BmobException e) {
                if (lists.isEmpty()){
                    Toast.makeText(getContext(),"不存在任何问题",Toast.LENGTH_LONG).show();
                }else {
                    for (Question list : lists) {

                        Map<String, Object> map = new HashMap<String, Object>();
                        Log.d("question",list.getQ_topic());
                        map.put("id",list.getQ_ID());
                        map.put("topic", ""+list.getQ_topic());
                        map.put("description",list.getQ_description());
                        list_sum.add(map);
                    }
                }
            }
        });

        return list_sum;
    }



}
