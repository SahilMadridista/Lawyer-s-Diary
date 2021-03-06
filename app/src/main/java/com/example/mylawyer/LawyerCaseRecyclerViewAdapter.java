package com.example.mylawyer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mylawyer.interfaces.CasesModifier;
import com.example.mylawyer.model.Case;

import java.util.ArrayList;

public class LawyerCaseRecyclerViewAdapter extends RecyclerView.Adapter<LawyerCaseRecyclerViewHolder>{

    private Context context;
    private ArrayList<Case> casesArrayList;
    private CasesModifier casesModifier;

    public LawyerCaseRecyclerViewAdapter(Context context, ArrayList<Case> casesArrayList, CasesModifier casesModifier){
        this.context = context;
        this.casesArrayList = casesArrayList;
        this.casesModifier = casesModifier;
    }

    @NonNull
    @Override
    public LawyerCaseRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.single_client_layout,parent,false);
        return new LawyerCaseRecyclerViewHolder(view, casesModifier);
    }

    @Override
    public void onBindViewHolder(@NonNull final LawyerCaseRecyclerViewHolder holder, final int position) {
        holder.bind(casesArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return casesArrayList.size();
    }
}
