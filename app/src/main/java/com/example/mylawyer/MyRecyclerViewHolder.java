package com.example.mylawyer;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyRecyclerViewHolder extends RecyclerView.ViewHolder {

    public TextView name_text,post_text,phone_text,date_text,aadhar_text;
    public ImageButton delete_staff_button;


    public MyRecyclerViewHolder(@NonNull View itemView) {

        super(itemView);

        name_text = itemView.findViewById(R.id.staff_name_text);
        post_text = itemView.findViewById(R.id.staff_post_text);
        phone_text = itemView.findViewById(R.id.staff_phone_text);
        date_text = itemView.findViewById(R.id.case_start_date);
        aadhar_text = itemView.findViewById(R.id.staff_aadhar_text);
        delete_staff_button = itemView.findViewById(R.id.remove_staff_button);

    }

}


