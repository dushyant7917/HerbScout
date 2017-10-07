package com.example.rishi.herbscout.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.example.rishi.herbscout.Activity.PlantDetailActivity;
import com.example.rishi.herbscout.Models.Plant;
import com.example.rishi.herbscout.Models.URLS;
import com.example.rishi.herbscout.R;
import com.example.rishi.herbscout.VolleySingleton.AppController;

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

        CardView cardView;
        TextView tvPlant;
        ImageView ivPlant;
        public MyViewHolder(View itemView) {
            super(itemView);
            tvPlant=itemView.findViewById(R.id.tvPlantName);
            ivPlant=itemView.findViewById(R.id.ivPlant);
            cardView=itemView.findViewById(R.id.cardview);
        }

        public void setData(final Plant data){
            String name=data.name.replace('-',' ');
            Log.d("NAME",name);
            tvPlant.setText(name.substring(0,1).toUpperCase()+name.substring(1).toLowerCase());
            String newname=data.name.toLowerCase().replace(' ','-');
            String url= URLS.BASE_URL+"PlantImages/"+newname+"/0.jpg";
            ImageLoader imageLoader= AppController.getInstance().getImageLoader();
            imageLoader.get(url,ImageLoader.getImageListener(ivPlant,R.mipmap.ic_launcher,R.drawable.arrow_34_48_blue));

            cardView.setOnClickListener(new View.OnClickListener() {
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
