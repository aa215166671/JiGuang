package com.example.a21516.ceshi_jiguang.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.a21516.ceshi_jiguang.R;
import com.example.a21516.ceshi_jiguang.famework.greendao.model.SearchAdd;

import java.util.List;

public class AddSearchAdapter extends BaseAdapter {
    private Context context;
    private List<SearchAdd> list;

    public void setListener (OnItemClickListener listener){
        this.listener=listener;
    }
    private OnItemClickListener listener;

    public AddSearchAdapter (Context context,List<SearchAdd> list){
        this.context = context;
        this.list = list;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public interface OnItemClickListener{
        void  onItemClick(String id,int position);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final SearchAdd tv = (SearchAdd) getItem(position);
        final ViewHolder viewHolder;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_add_search,null);

            viewHolder = new ViewHolder();
            viewHolder.tv=(TextView) convertView.findViewById(R.id.item_add_tv);
            viewHolder.tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                        listener.onItemClick(tv.getContent().toString(),position);
                    }catch (Exception e){

                    }
                }
            });
            convertView.setTag(viewHolder);

        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv.setText(tv.getContent());
        return convertView;
    }

    class ViewHolder{
        TextView tv;
    }
}
