package com.mwikali.imdonor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mwikali.imdonor.R;
import com.mwikali.imdonor.models.DonationRequest;

import java.util.List;

public class MyRequestsAdapter extends RecyclerView.Adapter<MyRequestsAdapter.ViewHolder> {

    private Context context;
    private List<DonationRequest> donationRequestList;

    public MyRequestsAdapter(Context context, List<DonationRequest> donationRequestList) {
        this.context = context;
        this.donationRequestList = donationRequestList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_requests, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DonationRequest donationRequest = donationRequestList.get(position);
        holder.tvBloodType.setText(donationRequest.bloodGroup);
        holder.tvName.setText(donationRequest.firstName);
        holder.tvTreatment.setText(donationRequest.treatment);
        holder.tvHospital.setText(donationRequest.hospital);
    }

    @Override
    public int getItemCount() {
        return donationRequestList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvBloodType, tvName, tvTreatment, tvHospital;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBloodType = itemView.findViewById(R.id.tvBloodType);
            tvName = itemView.findViewById(R.id.tvName);
            tvTreatment = itemView.findViewById(R.id.tvTreatment);
            tvHospital = itemView.findViewById(R.id.tvHospital);
        }
    }
}
