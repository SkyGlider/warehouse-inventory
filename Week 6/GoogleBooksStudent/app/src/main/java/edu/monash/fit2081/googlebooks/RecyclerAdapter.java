package edu.monash.fit2081.googlebooks;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    ArrayList<GoogleBook> data = new ArrayList<>();

    public void setData(ArrayList<GoogleBook> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false); //CardView inflated as RecyclerView list item
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.ViewHolder holder, int position) {
        holder.titleOut.setText(data.get(position).getBookTitle());
        holder.authorOut.setText(data.get(position).getAuthors());
        holder.yearOut.setText(data.get(position).getPublishedDate());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView titleOut, authorOut, yearOut;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleOut = itemView.findViewById(R.id.titleOut);
            authorOut = itemView.findViewById(R.id.authorOut);
            yearOut = itemView.findViewById(R.id.yearOut);
        }

    }
}
