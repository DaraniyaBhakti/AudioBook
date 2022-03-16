package com.tatvasoft.tatvasoftassignment12.Adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tatvasoft.tatvasoftassignment12.Model.ContactsModel;
import com.tatvasoft.tatvasoftassignment12.databinding.ContactRowItemBinding;

import java.util.ArrayList;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {

    ArrayList<ContactsModel> arrayList;

    public ContactAdapter(ArrayList<ContactsModel> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ContactAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(ContactRowItemBinding.inflate(inflater,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ContactAdapter.ViewHolder holder, int position) {
        holder.contactRowItemBinding.tvContactName.setText(arrayList.get(position).getContactName());
        holder.contactRowItemBinding.tvContactNumber.setText(arrayList.get(position).getContactNumber());
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
        private final ContactRowItemBinding contactRowItemBinding;

        public ViewHolder(@NonNull ContactRowItemBinding contactRowItemBinding) {
            super(contactRowItemBinding.getRoot());
            this.contactRowItemBinding = contactRowItemBinding;
        }
    }
}
