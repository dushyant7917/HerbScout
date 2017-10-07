package com.example.rishi.herbscout.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rishi.herbscout.Activity.PlantDetailActivity;
import com.example.rishi.herbscout.R;

import java.util.List;

/**
 * Created by Rishi on 06/10/17.
 */

public class StringAdapter extends RecyclerView.Adapter<StringAdapter.MyViewHolder> {

    static Context context;
    List<String> stringList;
    static int TYPE;

    public StringAdapter(Context context, List<String> stringList, int TYPE){
        this.context=context;
        this.stringList=stringList;
        this.TYPE=TYPE;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_string, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.setData(stringList.get(position));
    }

    @Override
    public int getItemCount() {
        return stringList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tvItem;
        CardView cardview;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvItem=itemView.findViewById(R.id.tvItem);
            cardview=itemView.findViewById(R.id.cardview);

        }

        public void setData(final String data) {
            tvItem.setText(data);
            Log.d("TYPEadapter",""+TYPE);
            if(TYPE==3){
                cardview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(context, PlantDetailActivity.class);
                        intent.putExtra("plantName",data);
                        context.startActivity(intent);
                    }
                });
            }
        }
    }
}
