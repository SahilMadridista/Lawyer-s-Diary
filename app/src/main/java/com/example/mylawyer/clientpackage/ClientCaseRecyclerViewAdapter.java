package com.example.mylawyer.clientpackage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mylawyer.R;
import com.example.mylawyer.interfaces.ClientCases;
import com.example.mylawyer.model.Case;

import java.util.ArrayList;

public class ClientCaseRecyclerViewAdapter extends RecyclerView.Adapter<ClientCaseRecyclerViewHolder> {

    private ArrayList<Case> clientCaseArrayList;
    private Context context;
    private ClientCases clientCases;

    public ClientCaseRecyclerViewAdapter(Context context,ArrayList<Case> clientCaseArrayList, ClientCases clientCases) {
        this.clientCaseArrayList = clientCaseArrayList;
        this.context = context;
        this.clientCases = clientCases;
    }

    @NonNull
    @Override
    public ClientCaseRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.clientcaseslayout,parent,false);
        return new ClientCaseRecyclerViewHolder(view,clientCases);
    }

    @Override
    public void onBindViewHolder(@NonNull ClientCaseRecyclerViewHolder holder, int position) {
        holder.bind(clientCaseArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return clientCaseArrayList.size();
    }
}
