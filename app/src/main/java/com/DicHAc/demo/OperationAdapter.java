package com.DicHAc.demo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OperationAdapter extends RecyclerView.Adapter<OperationAdapter.ViewHolder>{
    private static OperationAdapter.ClickListener clickListener;

    private Context context;
    private List<Operation> list;

    public OperationAdapter(Context context, List<Operation> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public OperationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.operation_item, parent, false);
        return new OperationAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(OperationAdapter.ViewHolder holder, int position) {
        Operation operation = list.get(position);

        holder.textid.setText(operation.getId());
        holder.textdate.setText(operation.getDate_operation());

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
            textdate = itemView.findViewById(R.id.date);
            textid = itemView.findViewById(R.id.id);
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
    public void setOnItemClickListener(OperationAdapter.ClickListener clickListener) {
        OperationAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(String id,int position, View v);
        void onItemLongClick(String id,int position, View v);
    }
}
