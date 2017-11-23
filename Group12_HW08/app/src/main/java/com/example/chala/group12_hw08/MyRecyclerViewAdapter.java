package com.example.chala.group12_hw08;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by chala on 4/6/2017.
 */

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    private ArrayList<fore> mData = new ArrayList<fore>();
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private sendDataToCityWeather activity;

    public MyRecyclerViewAdapter(ArrayList<fore> mData,MyRecyclerViewAdapter.sendDataToCityWeather activity) {
        this.mData = mData;
        this.activity = activity;
    }


    @Override
    public MyRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.from(parent.getContext()).inflate(R.layout.recycle_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyRecyclerViewAdapter.ViewHolder holder, final int position) {
        holder.tv.setText(mData.get(position).getDate());
        int n=mData.get(position).getDay_icon();
        String nt=Integer.toString(n);
        if(n==0||n==1||n==2||n==3||n==4||n==5||n==6||n==7||n==8||n==9){
            nt="0"+nt;
        }
        Picasso.with(holder.imv.getContext())
                .load("http://developer.accuweather.com/sites/default/files/"+nt+"-s.png")
                .into(holder.imv);
        holder.mRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.sendDataToCityActivity(mData.get(position));
            }
        });

        holder.imv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.sendDataToCityActivity(mData.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv;
        public ImageView imv;
        //Dhenuka
        public View mRoot;

        public ViewHolder(View view) {
            super(view);
            tv=(TextView) view.findViewById(R.id.r_date);
            imv=(ImageView) view.findViewById(R.id.r_img);
            mRoot = view;
            //view.setOnClickListener(this);

        }
    }

    public interface sendDataToCityWeather
    {
        public void sendDataToCityActivity(fore fore);
    }

}
