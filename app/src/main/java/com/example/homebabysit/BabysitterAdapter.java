package com.example.homebabysit;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class BabysitterAdapter extends RecyclerView.Adapter<BabysitterAdapter.BabysitterViewHolder> {

    private List<Babysitter> babysitters;
    private final OnBabysitterClickListener onBabysitterClickListener;

    public BabysitterAdapter(List<Babysitter> babysitters, OnBabysitterClickListener listener) {
        this.babysitters = new ArrayList<>(babysitters);
        this.onBabysitterClickListener = listener;
    }

    @NonNull
    @Override
    public BabysitterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_babysitter, parent, false);
        return new BabysitterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BabysitterViewHolder holder, int position) {
        Babysitter babysitter = babysitters.get(position);
        holder.nameTextView.setText(babysitter.getName());
        holder.locationTextView.setText(babysitter.getLocation());
        holder.itemView.setOnClickListener(v -> onBabysitterClickListener.onClick(babysitter));
    }

    @Override
    public int getItemCount() {
        return babysitters.size();
    }

    public void updateList(List<Babysitter> newList) {
        babysitters = new ArrayList<>(newList);
        notifyDataSetChanged();
    }

    public interface OnBabysitterClickListener {
        void onClick(Babysitter babysitter);
    }

    static class BabysitterViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, locationTextView;

        BabysitterViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.text_babysitter_name);
            locationTextView = itemView.findViewById(R.id.text_babysitter_location);
        }
    }
}
