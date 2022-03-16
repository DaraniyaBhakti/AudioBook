package com.tatvasoft.tatvasoftassignment12.Adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tatvasoft.tatvasoftassignment12.databinding.AudioItemRowBinding;

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
        return new AudioAdapter.ViewHolder(AudioItemRowBinding.inflate(inflater,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.audioItemRowBinding.tvAudioName.setText(arrayList.get(position));

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void clearData(){
        arrayList.clear();
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final AudioItemRowBinding audioItemRowBinding;
        public ViewHolder(@NonNull AudioItemRowBinding audioItemRowBinding) {
            super(audioItemRowBinding.getRoot());
            this.audioItemRowBinding = audioItemRowBinding;
        }
    }
}
