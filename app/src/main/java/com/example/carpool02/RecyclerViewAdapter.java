package com.example.carpool02;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private List<recyclerListItem> listItems;
    private Context context;

    public RecyclerViewAdapter(List<recyclerListItem> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rideslistitem,parent,false);

        return new ViewHolder(v);


    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        recyclerListItem recyclerItem = listItems.get(position);

        holder.textViewName.setText(recyclerItem.getName());
        holder.textViewtime.setText(recyclerItem.time);
        holder.textViewdate.setText(recyclerItem.getDate());
        Glide.with(context)
                .load(recyclerItem.getImageUrl())
                .into(holder.imageViewdp);

    }

    @Override
    public int getItemCount() {

        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView textViewName,textViewdate,textViewtime;
        public ImageView imageViewdp;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.nameOfrider);
            textViewdate = itemView.findViewById(R.id.dateOfRide);
            textViewtime = itemView.findViewById(R.id.timeOfRide);
            imageViewdp = itemView.findViewById(R.id.imageViewRiderDP);
        }
    }
}
