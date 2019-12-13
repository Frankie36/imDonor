package com.mwikali.imdonor.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mwikali.imdonor.Constants;
import com.mwikali.imdonor.R;
import com.mwikali.imdonor.adapter.MyRequestsAdapter;
import com.mwikali.imdonor.models.DonationRequest;

import java.util.ArrayList;
import java.util.List;

public class UrgentRequestsFragment extends Fragment {

    private final String TAG = UrgentRequestsFragment.class.getSimpleName();
    private FirebaseFirestore database;
    private List<DonationRequest> donationRequests = new ArrayList<>();

    public static UrgentRequestsFragment newInstance() {
        return new UrgentRequestsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.urgent_requests_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final RecyclerView rvUrgentRequests = view.findViewById(R.id.rvUrgentRequests);
        rvUrgentRequests.setLayoutManager(new LinearLayoutManager(getContext()));

        database.collection(Constants.KEY_COLLECTION_BLOOD_REQUESTS)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                donationRequests.add(document.toObject(DonationRequest.class));
                            }
                            rvUrgentRequests.setAdapter(new MyRequestsAdapter(getContext(), donationRequests));
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

}
