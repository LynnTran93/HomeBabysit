package com.example.homebabysit;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class BabysitterAdapter extends RecyclerView.Adapter<BabysitterAdapter.BabysitterViewHolder> {

    private List<String> babysitters;
    private final OnBabysitterClickListener onBabysitterClickListener;
    private String selectedBabysitter;

    public BabysitterAdapter(List<String> babysitters, OnBabysitterClickListener listener) {
        this.babysitters = babysitters;
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
        String babysitter = babysitters.get(position);
        holder.textView.setText(babysitter);
        holder.itemView.setOnClickListener(v -> {
            selectedBabysitter = babysitter;
            onBabysitterClickListener.onClick(babysitter);
        });
    }

    @Override
    public int getItemCount() {
        return babysitters.size();
    }

    public void updateList(List<String> newList) {
        babysitters = newList;
        notifyDataSetChanged();
    }

    public interface OnBabysitterClickListener {
        void onClick(String babysitter);
    }

    static class BabysitterViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        BabysitterViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(android.R.id.text1);
        }
    }
}
