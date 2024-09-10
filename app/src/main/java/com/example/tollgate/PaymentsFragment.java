// PaymentsFragment.java
package com.example.tollgate;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class PaymentsFragment extends Fragment {

    private ListView listViewTransactions;
    private Database database;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_payments, container, false);

        listViewTransactions = rootView.findViewById(R.id.ViewTransactions);
        database = new Database(getActivity());

        // Retrieve the logged-in user's phone number from SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userSession", getActivity().MODE_PRIVATE);
        String loggedInUserPhone = sharedPreferences.getString("phone", null);

        // Check if the phone number is available
        if (loggedInUserPhone != null) {
            // Fetch transactions for the logged-in user
            List<Transaction> transactionList = database.getTransactionsByPhoneNumber(loggedInUserPhone);

            // Create and set the adapter for the ListView
            TransactionsAdapter adapter = new TransactionsAdapter(getActivity(), transactionList);
            listViewTransactions.setAdapter(adapter);
        } else {
            Toast.makeText(getActivity(), "Phone number not found in session", Toast.LENGTH_SHORT).show();
        }

        return rootView;
    }
}
