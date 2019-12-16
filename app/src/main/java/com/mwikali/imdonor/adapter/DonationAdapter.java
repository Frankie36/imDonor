package com.mwikali.imdonor.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mwikali.imdonor.R;
import com.mwikali.imdonor.models.Donation;

import java.util.List;

public class DonationAdapter extends RecyclerView.Adapter<DonationAdapter.ViewHolder> {

    private Context context;
    private List<Donation> donationList;

    public DonationAdapter(Context context, List<Donation> donationList) {
        this.context = context;
        this.donationList = donationList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_history, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Donation donation = donationList.get(position);
        holder.tvDonationBank.setText(donation.bankName);
        holder.tvDonationDate.setText(donation.date);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //context.startActivity(new Intent(context, NewsDetailActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return donationList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvDonationDate, tvDonationBank;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDonationDate = itemView.findViewById(R.id.tvDonationDate);
            tvDonationBank = itemView.findViewById(R.id.tvDonationBank);
        }
    }
}
