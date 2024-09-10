package com.example.tollgate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {

    private TextView tvUserName;
    private TextView tvUserAccount;
    private TextView tvAirtimeBalance;
    private CardView cvAddVehicle;
    private CardView cvRechargeBalance;
    private CardView cvTollGates;
    private CardView cvProfile;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize UI components
        tvUserName = view.findViewById(R.id.tv_user_name);
        tvUserAccount = view.findViewById(R.id.tv_user_account);
        tvAirtimeBalance = view.findViewById(R.id.tv_airtime_balance);
        cvAddVehicle = view.findViewById(R.id.Home);
        cvRechargeBalance = view.findViewById(R.id.tvUserAccount);
        cvTollGates = view.findViewById(R.id.mountains);
        cvProfile = view.findViewById(R.id.parks);

        // Get user details from SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userSession", Context.MODE_PRIVATE);
        String userName = sharedPreferences.getString("username", "User");
        String phone = sharedPreferences.getString("phone", "User");

        Log.d("HomeFragment", "Username: " + userName + ", Phone: " + phone); // Log statement for debugging

        // Set user name and other details
        tvUserName.setText(userName);
        tvUserAccount.setText("Phone Number - " + phone);

        // Initial balance update
        updateBalance(phone);

        // Set click listeners for each card
        cvAddVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddVehicleActivity.class);
                startActivity(intent);
            }
        });

        cvRechargeBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RechargeActivity.class);
                startActivity(intent);
            }
        });

        cvTollGates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TollGatesActivity.class);
                startActivity(intent);
            }
        });

        cvProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        // Get user details from SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userSession", Context.MODE_PRIVATE);
        String phone = sharedPreferences.getString("phone", "User");

        // Update the balance when the fragment resumes
        updateBalance(phone);
    }

    private void updateBalance(String phone) {
        // Get the updated balance from the database
        Database db = new Database(getActivity());
        double balance = db.getBalance(phone);

        // Log the updated balance for debugging
        Log.d("HomeFragment", "Updated balance: " + balance);

        // Update the balance TextView
        tvAirtimeBalance.setText("Current Balance: " + balance + " TZS");
    }
}
