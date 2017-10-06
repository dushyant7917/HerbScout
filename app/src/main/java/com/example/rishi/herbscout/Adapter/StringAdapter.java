package com.example.rishi.herbscout.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rishi.herbscout.R;

import java.util.List;

/**
 * Created by Rishi on 06/10/17.
 */

public class StringAdapter extends RecyclerView.Adapter<StringAdapter.MyViewHolder> {

    Context context;
    List<String> stringList;

    public StringAdapter(Context context, List<String> stringList){
        this.context=context;
        this.stringList=stringList;
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
        public MyViewHolder(View itemView) {
            super(itemView);
            tvItem=itemView.findViewById(R.id.tvItem);
        }


        public void setData(String data) {
            tvItem.setText(data);
        }
    }
}
