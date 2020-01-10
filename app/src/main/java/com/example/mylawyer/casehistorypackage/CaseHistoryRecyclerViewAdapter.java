package com.example.mylawyer.casehistorypackage;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mylawyer.R;
import com.example.mylawyer.model.CaseInformation;
import java.util.ArrayList;

public class CaseHistoryRecyclerViewAdapter extends RecyclerView.Adapter<CaseHistoryRecyclerViewHolder> {

    private ArrayList<CaseInformation> caseInformations;

    public CaseHistoryRecyclerViewAdapter(ArrayList<CaseInformation> caseInformations) {
        this.caseInformations = caseInformations;
    }

    @NonNull
    @Override
    public CaseHistoryRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CaseHistoryRecyclerViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.casehistorylayout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CaseHistoryRecyclerViewHolder holder, int position) {
        holder.bind(caseInformations.get(position));
    }

    @Override
    public int getItemCount() {
        return caseInformations.size();
    }
}
