package com.jiang.learncode2.ForumModule.Adapter;

import android.content.Context;

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


public class ListAdapter extends BaseAdapter {

    private Context mContext;
    private List<Map<String, Object>> mData;

    public ListAdapter() {

    }

    public ListAdapter(Context mContext,List<Map<String, Object>> mData) {
        this.mData = mData;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            holder = new ViewHolder();
            convertView=LayoutInflater.from(mContext).inflate(R.layout.question_list, null);
            holder.topic = convertView.findViewById(R.id.topic);
            holder.topic_content = convertView.findViewById(R.id.topic_content);
            holder.description = convertView.findViewById(R.id.description);
            holder.description_content = convertView.findViewById(R.id.description_content);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.topic_content.setText((String)mData.get(position).get("topic"));
        holder.description_content.setText((String)mData.get(position).get("description"));
        return convertView;

    }

    private class ViewHolder{
        TextView description;
        TextView topic;
        TextView description_content;
        TextView topic_content;
    }

}
