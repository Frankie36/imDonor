package com.mwikali.imdonor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mwikali.imdonor.R;
import com.mwikali.imdonor.models.UserDonor;

import java.util.List;

public class DonorAdapter extends RecyclerView.Adapter<DonorAdapter.ViewHolder> {

    private Context context;
    private List<UserDonor> userDonorList;

    public DonorAdapter(Context context, List<UserDonor> userDonorList) {
        this.context = context;
        this.userDonorList = userDonorList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_donors, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserDonor userDonor = userDonorList.get(position);
        holder.tvBloodType.setText(userDonor.bloodGroup);
        holder.tvName.setText(String.format("%s %s", userDonor.firstName, userDonor.lastName));
        holder.tvLastDonationDate.setText(userDonor.lastDonationDate);
        //holder.tvHospital.setText(userDonor.hospital);
    }

    @Override
    public int getItemCount() {
        return userDonorList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvBloodType, tvName, tvLastDonationDate, tvHospital;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBloodType = itemView.findViewById(R.id.tvBloodType);
            tvName = itemView.findViewById(R.id.tvName);
            tvLastDonationDate = itemView.findViewById(R.id.tvLastDonationDate);
            //tvHospital = itemView.findViewById(R.id.tvHospital);
        }
    }
}
