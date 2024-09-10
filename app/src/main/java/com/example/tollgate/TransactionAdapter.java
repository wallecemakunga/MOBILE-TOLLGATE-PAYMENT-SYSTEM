// TransactionsAdapter.java
package com.example.tollgate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

class TransactionsAdapter extends ArrayAdapter<Transaction> {

    public TransactionsAdapter(Context context, List<Transaction> transactions) {
        super(context, 0, transactions);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Transaction transaction = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_transaction, parent, false);
        }

        TextView tvDate = convertView.findViewById(R.id.tv_date);
        TextView tvAmount = convertView.findViewById(R.id.tv_amount);
        TextView tvDescription = convertView.findViewById(R.id.tv_description);

        tvDate.setText(transaction.getDate());
        tvAmount.setText(String.valueOf(transaction.getAmount()));
        tvDescription.setText(transaction.getDescription());

        return convertView;
    }
}
