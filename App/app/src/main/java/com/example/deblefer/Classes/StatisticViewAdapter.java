package com.example.deblefer.Classes;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.deblefer.R;

public class StatisticViewAdapter extends RecyclerView.Adapter<StatisticViewAdapter.ViewHolder> {

    private IconData[] data;

    public StatisticViewAdapter(IconData[] data) {
        this.data = data;
    }

    public void updateData(IconData[] data) {
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textView.setText(data[position].getFigure());
        holder.textView2.setText(data[position].getStat1());
        holder.textView3.setText(data[position].getStat2());
        holder.imageView.setImageResource(data[position].getImgId());
        holder.imageView2.setImageResource(data[position].getImgId2());
        holder.imageView3.setImageResource(data[position].getImgId3());
        holder.imageView4.setImageResource(data[position].getImgId4());
        holder.imageView5.setImageResource(data[position].getImgId5());
    }
    @Override
    public int getItemCount() {
        return data.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public ImageView imageView2;
        public ImageView imageView3;
        public ImageView imageView4;
        public ImageView imageView5;
        public TextView textView;
        public TextView textView2;
        public TextView textView3;

        public ViewHolder(View itemView) {
            super(itemView);
            this.imageView = (ImageView) itemView.findViewById(R.id.imageView);
            this.imageView2 = (ImageView) itemView.findViewById(R.id.imageView2);
            this.imageView3 = (ImageView) itemView.findViewById(R.id.imageView3);
            this.imageView4 = (ImageView) itemView.findViewById(R.id.imageView4);
            this.imageView5 = (ImageView) itemView.findViewById(R.id.imageView5);
            this.textView = (TextView) itemView.findViewById(R.id.textView);
            this.textView2 = (TextView) itemView.findViewById(R.id.textView2);
            this.textView3 = (TextView) itemView.findViewById(R.id.textView3);
        }
    }

}