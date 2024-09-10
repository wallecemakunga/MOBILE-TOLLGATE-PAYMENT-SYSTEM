package com.example.tollgate;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class VehicleAdapter extends RecyclerView.Adapter<VehicleAdapter.VehicleViewHolder> {

    private List<Vehicle> vehicleList;

    // Constructor
    public VehicleAdapter(List<Vehicle> vehicleList) {
        this.vehicleList = vehicleList;
    }

    @NonNull
    @Override
    public VehicleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_vehicle, parent, false);
        return new VehicleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VehicleViewHolder holder, int position) {
        Vehicle vehicle = vehicleList.get(position);
        holder.bind(vehicle);
    }

    @Override
    public int getItemCount() {
        return vehicleList.size();
    }

    static class VehicleViewHolder extends RecyclerView.ViewHolder {

        private TextView tvPhone, tvLicensePlate, tvMakeModel, tvType, tvColor, tvVin, tvYear;

        public VehicleViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPhone = itemView.findViewById(R.id.tv_phone);
            tvLicensePlate = itemView.findViewById(R.id.tv_license_plate);
            tvMakeModel = itemView.findViewById(R.id.tv_make_model);
            tvType = itemView.findViewById(R.id.tv_type);
            tvColor = itemView.findViewById(R.id.tv_color);
            tvVin = itemView.findViewById(R.id.tv_vin);
            tvYear = itemView.findViewById(R.id.tv_year);
        }

        public void bind(Vehicle vehicle) {
            tvPhone.setText(vehicle.getPhone());
            tvLicensePlate.setText(vehicle.getLicensePlate());
            tvMakeModel.setText(vehicle.getMakeModel());
            tvType.setText(vehicle.getType());
            tvColor.setText(vehicle.getColor());
            tvVin.setText(vehicle.getVin());
            tvYear.setText(vehicle.getYearOfManufacture());
        }
    }
}
