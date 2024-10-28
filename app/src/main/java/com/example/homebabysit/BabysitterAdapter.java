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
    private List<Babysitter> originalBabysitters; // Stores the unfiltered list
    private final OnBabysitterClickListener onBabysitterClickListener;

    public BabysitterAdapter(List<Babysitter> babysitters, OnBabysitterClickListener listener) {
        this.babysitters = new ArrayList<>(babysitters); // Initialize with a copy
        this.originalBabysitters = new ArrayList<>(babysitters); // Keep a copy of the full list
        this.onBabysitterClickListener = listener;
    }

    @NonNull
    @Override
    public BabysitterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new BabysitterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BabysitterViewHolder holder, int position) {
        Babysitter babysitter = babysitters.get(position);
        holder.textView.setText(babysitter.getName()); // Display babysitter's name
        holder.itemView.setOnClickListener(v -> onBabysitterClickListener.onClick(babysitter));
    }

    @Override
    public int getItemCount() {
        return babysitters.size();
    }

    // Method to filter the babysitters by experience level
    public void filterByExperience(String experienceLevel) {
        if (experienceLevel.equals("Any")) {
            babysitters = new ArrayList<>(originalBabysitters); // Show all babysitters
        } else {
            List<Babysitter> filteredList = new ArrayList<>();
            for (Babysitter babysitter : originalBabysitters) {
                if (babysitter.getExperience().equalsIgnoreCase(experienceLevel)) {
                    filteredList.add(babysitter);
                }
            }
            babysitters = filteredList;
        }
        notifyDataSetChanged();
    }

    // Method to update the list when needed
    public void updateList(List<Babysitter> newList) {
        originalBabysitters = new ArrayList<>(newList); // Update the original list
        babysitters = new ArrayList<>(newList); // Update the displayed list
        notifyDataSetChanged();
    }

    public interface OnBabysitterClickListener {
        void onClick(Babysitter babysitter);
    }

    static class BabysitterViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        BabysitterViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(android.R.id.text1);
        }
    }
}
