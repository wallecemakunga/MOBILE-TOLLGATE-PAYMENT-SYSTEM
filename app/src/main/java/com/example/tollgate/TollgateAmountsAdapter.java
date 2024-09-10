package com.example.tollgate;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TollgateAmountsAdapter extends RecyclerView.Adapter<TollgateAmountsAdapter.TollgateViewHolder> {

    private List<TollgateAmount> tollgateAmounts;

    public TollgateAmountsAdapter(List<TollgateAmount> tollgateAmounts) {
        this.tollgateAmounts = tollgateAmounts;
    }

    @NonNull
    @Override
    public TollgateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_tollgate_amount, parent, false);
        return new TollgateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TollgateViewHolder holder, int position) {
        TollgateAmount tollgateAmount = tollgateAmounts.get(position);
        holder.tvTollgateName.setText(tollgateAmount.getName());
        holder.tvTollgateAmount.setText(String.format("TZS %.2f", tollgateAmount.getAmount()));
    }

    @Override
    public int getItemCount() {
        return tollgateAmounts.size();
    }

    public void updateTollgateAmounts(List<TollgateAmount> newTollgateAmounts) {
        this.tollgateAmounts = newTollgateAmounts;
        notifyDataSetChanged();
    }

    public static class TollgateViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTollgateName;
        private TextView tvTollgateAmount;

        public TollgateViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTollgateName = itemView.findViewById(R.id.tv_tollgate_name);
            tvTollgateAmount = itemView.findViewById(R.id.tv_tollgate_amount);
        }
    }
}
