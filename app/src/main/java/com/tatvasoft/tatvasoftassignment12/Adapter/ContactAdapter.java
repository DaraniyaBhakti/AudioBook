package com.tatvasoft.tatvasoftassignment12.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tatvasoft.tatvasoftassignment12.Model.ContactsModel;
import com.tatvasoft.tatvasoftassignment12.R;

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
        View view = inflater.inflate(R.layout.contact_row_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactAdapter.ViewHolder holder, int position) {
        holder.tvContactName.setText(arrayList.get(position).getContactName());
        holder.tvContactNumber.setText(arrayList.get(position).getContactNumber());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvContactName,tvContactNumber;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvContactName=itemView.findViewById(R.id.tvContactName);
            tvContactNumber = itemView.findViewById(R.id.tvContactNumber);
        }
    }
}
