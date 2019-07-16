package com.jiang.learncode2.ForumModule.Adapter;

import android.content.Context;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiang.learncode2.R;

import java.util.List;
import java.util.Map;

import cn.bmob.v3.datatype.BmobDate;

public class answerAdapter extends BaseAdapter {

    private List<Map<String, Object>> data;
    private Context context;
    public answerAdapter(Context context, List<Map<String, Object>> data){
        Log.d("zujian",""+data.toString());
        this.context=context;
        this.data=data;
        //this.layoutInflater=LayoutInflater.from(context);
    }
    /**
     * 组件集合，对应list.xml中的控件
     * @author Administrator
     */
    public final class Zujian{
        public TextView username;
        public TextView date;
        public TextView content;

    }
    @Override
    public int getCount() {
        return data.size();
    }
    /**
     * 获得某一位置的数据
     */
    @Override
    public Object getItem(int position) {
        return data.get(position);
    }
    /**
     * 获得唯一标识
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Zujian zujian=null;
        if(convertView==null){
            zujian=new Zujian();
            //获得组件，实例化组件

            Log.d("zujian",""+data.get(0).get("username"));
            convertView = LayoutInflater.from(context).inflate(R.layout.answerlistview,parent,false);
            zujian.username = convertView.findViewById(R.id.text_user);
            zujian.date = convertView.findViewById(R.id.text_date);
            zujian.content = convertView.findViewById(R.id.content);
            convertView.setTag(zujian);
        }else{
            zujian=(Zujian)convertView.getTag();
        }
        //绑定数据

        zujian.username.setText((String)data.get(position).get("username"));
        zujian.date.setText((String)data.get(position).get("date"));
        zujian.content.setText((String)data.get(position).get("content"));
        return convertView;
    }

}
