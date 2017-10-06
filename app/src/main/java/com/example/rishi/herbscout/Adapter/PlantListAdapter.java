package com.example.rishi.herbscout.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rishi.herbscout.Activity.PlantDetailActivity;
import com.example.rishi.herbscout.Models.Plant;
import com.example.rishi.herbscout.R;

import java.util.List;

/**
 * Created by Rishi on 06/10/17.
 */

public class PlantListAdapter extends RecyclerView.Adapter<PlantListAdapter.MyViewHolder> {

    List<Plant> plantList;
    static Context context;

    public PlantListAdapter(Context context,List<Plant> plantList){
        this.context=context;
        this.plantList=plantList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_plant, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.setData(plantList.get(position));
    }

    @Override
    public int getItemCount() {
        return plantList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tvPlant;
        public MyViewHolder(View itemView) {
            super(itemView);
            tvPlant=itemView.findViewById(R.id.tvPlantName);
        }

        public void setData(final Plant data){
            tvPlant.setText(data.name);

            tvPlant.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent =new Intent(context, PlantDetailActivity.class);
                    intent.putExtra("plantName",data.name);
                    context.startActivity(intent);
                }
            });
        }
    }
}
