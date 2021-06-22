package com.DicHAc.demo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{
    private static MyAdapter.ClickListener clickListener;

    private Context context;
    private List<User> list;

    public MyAdapter(Context context, List<User> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.operateur_item, parent, false);
        return new MyAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyAdapter.ViewHolder holder, int position) {
        User user = list.get(position);

        holder.textid.setText(user.getUserId());
        holder.textdate.setText(user.getName());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public TextView textid, textdate;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            textdate = itemView.findViewById(R.id.operateur_txt);
            textid = itemView.findViewById(R.id.qualification_txt);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(textid.getText().toString(),getAdapterPosition(), v);
        }

        @Override
        public boolean onLongClick(View v) {
            clickListener.onItemLongClick(textid.getText().toString(),getAdapterPosition(), v);
            return false;
        }
    }
    public void setOnItemClickListener(MyAdapter.ClickListener clickListener) {
        MyAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(String id,int position, View v);
        void onItemLongClick(String id,int position, View v);
    }
}