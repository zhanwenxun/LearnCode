package com.jiang.learncode2.ForumModule.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jiang.learncode2.DataBase.bean.Question;
import com.jiang.learncode2.DataBase.bean.RegisterUser;
import com.jiang.learncode2.ForumModule.Adapter.ListAdapter;
import com.jiang.learncode2.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class ContentFragment extends Fragment {


    private int mType = 0;
    private String mTitle;

    public void setType(int mType) {
        this.mType = mType;
    }
    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }
    private ListView listView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_content , container, false);
        listView = (ListView)view.findViewById(R.id.listview);
        List<Map<String, Object>> list=getData();
        listView.setAdapter(new ListAdapter(getActivity(), list));
        return view;
    }

    public List<Map<String, Object>> getData() {
        final List<Map<String, Object>> list_sum = new ArrayList<Map<String, Object>>();

        BmobQuery<Question> query = new BmobQuery<Question>();
        Question question = new Question();
        //按照时间降序
        query.order("-createdAt");
        //执行查询，第一个参数为上下文，第二个参数为查找的回调
        query.findObjects(new FindListener<Question>() {
            @Override
            public void done(List<Question> lists, BmobException e) {
                for (Question list : lists) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("topic", list.getQ_topic());
                    map.put("description", list.getQ_description());
                    list_sum.add(map);
                }
            }
        });

        return list_sum;
    }



}
