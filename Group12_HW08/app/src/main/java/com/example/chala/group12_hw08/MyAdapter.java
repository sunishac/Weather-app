package com.example.chala.group12_hw08;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by chala on 4/7/2017.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

ArrayList<Weather> weat= new ArrayList<Weather>();
    private LayoutInflater mInflater;
    private MyRecyclerViewAdapter.ItemClickListener mClickListener;
    DatabaseReference databaseReference;



    public MyAdapter(ArrayList<Weather> weat) {
        this.weat = weat;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.from(parent.getContext()).inflate(R.layout.recycle_main, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final int n=position;
        holder.tv1.setText(weat.get(position).getCity()+","+weat.get(position).getCountry());
        holder.tv2.setText("Temparature: "+weat.get(position).getMetric());
        Log.d("demo","temdksnjv "+weat.get(position).getMetric());
        //pretty
        String dateString=weat.get(position).getLocalObservationDateTime().toString();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa");

        Date convertedDate = new Date();

        try {
            convertedDate = dateFormat.parse(dateString);
        }  catch (ParseException e) {
            e.printStackTrace();
        }
        PrettyTime p  = new PrettyTime();
        String datetime= p.format(convertedDate);
        holder.tv3.setText("Last updated: "+datetime);
        //holder.tv3.setText("Last updated: "+weat.get(position).get(0).getLocalObservationDateTime());

        Log.d("demo1","getfav value is "+weat.get(position).getFav());
        if(weat.get(position).getFav().equals("FALSE")){
            Picasso.with(holder.imv.getContext())
                    .load(R.drawable.white)
                    .into(holder.imv);
        }else{
            Picasso.with(holder.imv.getContext())
                    .load(R.drawable.yellow)
                    .into(holder.imv);
            //weat.get(position).get(0).setFav("fav");

        }

        holder.imv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Picasso.with(holder.imv.getContext())
                        .load(R.drawable.yellow)
                        .into(holder.imv);
                weat.get(n).setFav("TRUE");
                Log.d("demo","weat is "+weat);

                final DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("weather");
                ref2.child(weat.get(n).getKeyfb()).child("favorite").setValue("TRUE");

                ref2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //dataSnapshot.child(weat.get(n).getKeyfb()).child("favorite");
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        holder.mRoot.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("weather");
                ref2.child(weat.get(n).getKeyfb()).removeValue();
                weat.remove(n);
                notifyDataSetChanged();
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return weat.size();
    }

    public void setClickListener(MyRecyclerViewAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public View mRoot;
        public TextView tv1;
        public TextView tv2;
        public TextView tv3;
        public ImageView imv;


        public ViewHolder(View itemView) {
            super(itemView);
            mRoot = itemView;
            tv1=(TextView) itemView.findViewById(R.id.citcon);
            tv2=(TextView) itemView.findViewById(R.id.tmpt);
            tv3=(TextView) itemView.findViewById(R.id.tym);
            imv=(ImageView) itemView.findViewById(R.id.star);

        }
    }

}
