package com.tatvasoft.tatvasoftassignment12.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tatvasoft.tatvasoftassignment12.R;

import java.util.List;

public class AudioAdapter extends RecyclerView.Adapter<AudioAdapter.ViewHolder> {
    List<String> arrayList;

    public AudioAdapter(List<String> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.audio_item_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvAudioName.setText(arrayList.get(position));

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvAudioName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAudioName = itemView.findViewById(R.id.tvAudioName);
        }
    }
}
