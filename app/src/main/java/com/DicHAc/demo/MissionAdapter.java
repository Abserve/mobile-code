package com.DicHAc.demo;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
public class MissionAdapter extends RecyclerView.Adapter<MissionAdapter.ViewHolder> {
    private static ClickListener clickListener;

        private Context context;
        private List<Mission> list;

    public MissionAdapter(Context context, List<Mission> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(context).inflate(R.layout.recyclerview_row, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Mission mission = list.get(position);

            holder.textid.setText(mission.getId());
            holder.textDesc.setText(mission.getDescription());
            holder.txtDateE.setText(mission.getDate_declanche());

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
            public TextView textid, textDesc, txtDateE;

            public ViewHolder(View itemView) {
                super(itemView);
                itemView.setOnClickListener(this);
                itemView.setOnLongClickListener(this);
                textDesc = itemView.findViewById(R.id.description);
                textid = itemView.findViewById(R.id.id);
                txtDateE = itemView.findViewById(R.id.dated);
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
    public void setOnItemClickListener(ClickListener clickListener) {
        MissionAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(String id,int position, View v);
        void onItemLongClick(String id,int position, View v);
    }
}