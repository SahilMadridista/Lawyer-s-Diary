package com.example.mylawyer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewHolder> {

    StaffInformation staffInformation;
    ArrayList<Staffmembers> staffmembersArrayList = new ArrayList<>();

    public MyRecyclerViewAdapter(StaffInformation staffInformation, ArrayList<Staffmembers> staffmembersArrayList) {
        this.staffInformation = staffInformation;
        this.staffmembersArrayList = staffmembersArrayList;
    }

    @NonNull
    @Override
    public MyRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(staffInformation.getBaseContext());
        View view = layoutInflater.inflate(R.layout.listview_layout,parent,false);

        return new MyRecyclerViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyRecyclerViewHolder holder, int position) {

        holder.name_text.setText(staffmembersArrayList.get(position).getName());
        holder.post_text.setText(staffmembersArrayList.get(position).getPost());
        holder.phone_text.setText(staffmembersArrayList.get(position).getPhone());

    }

    @Override
    public int getItemCount() {
        return staffmembersArrayList.size();

    }

}
