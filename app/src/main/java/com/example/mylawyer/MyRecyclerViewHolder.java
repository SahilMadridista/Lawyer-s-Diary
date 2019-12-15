package com.example.mylawyer;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyRecyclerViewHolder extends RecyclerView.ViewHolder {

    public TextView name_text,post_text,phone_text;
    public Button delete_button;


    public MyRecyclerViewHolder(@NonNull View itemView) {

        super(itemView);

        name_text = itemView.findViewById(R.id.staff_name_text);
        post_text = itemView.findViewById(R.id.staff_post_text);
        phone_text = itemView.findViewById(R.id.staff_phone_text);
        delete_button = itemView.findViewById(R.id.remove_button);

    }

}


