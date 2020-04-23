package com.example.warehouseinventoryapp;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder> {
    ArrayList<Item> data = new ArrayList<Item>();
    public void setData(ArrayList<Item> data) {
        this.data = data;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false); //CardView inflated as RecyclerView list item
        ViewHolder viewHolder = new ViewHolder(v);
        Log.d("week6App","onCreateViewHolder");
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.nameOut.setText(data.get(position).getName());
        holder.qtyOut.setText(String.valueOf(data.get(position).getQuantity()));
        holder.costOut.setText(String.valueOf(data.get(position).getCost()));
        holder.descOut.setText(data.get(position).getDesc());
        if (data.get(position).isFrozen()){
            holder.frozenOut.setText("Yes");
        }

        Log.d("week6App","onBindViewHolder");
    }
    @Override
    public int getItemCount() {

        return data.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameOut,qtyOut,costOut,descOut,frozenOut;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameOut = itemView.findViewById(R.id.nameOut);
            qtyOut = itemView.findViewById(R.id.qtyOut);
            costOut = itemView.findViewById(R.id.costOut);
            descOut = itemView.findViewById(R.id.descOut);
            frozenOut = itemView.findViewById(R.id.frozenOut);
        }
    }
}