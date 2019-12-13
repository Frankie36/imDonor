package com.mwikali.imdonor.fragment;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mwikali.imdonor.Constants;
import com.mwikali.imdonor.R;
import com.mwikali.imdonor.adapter.MyRequestsAdapter;
import com.mwikali.imdonor.models.DonationRequest;
import com.mwikali.imdonor.models.UserBank;
import com.mwikali.imdonor.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyRequestsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyRequestsFragment extends Fragment {

    private final String TAG = MyRequestsFragment.class.getSimpleName();
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_IS_DONOR = "isDonor";
    private static final String ARG_PARAM2 = "param2";

    private Boolean isDonor;
    private String mParam2;
    private RecyclerView rvDonationRequests;
    private FirebaseFirestore database;
    private List<DonationRequest> donationRequests = new ArrayList<>();

    public MyRequestsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param isDonor Check if is showing donor requests.
     * @param param2  Parameter 2.
     * @return A new instance of fragment MyRequestsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyRequestsFragment newInstance(Boolean isDonor, String param2) {
        MyRequestsFragment fragment = new MyRequestsFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_IS_DONOR, isDonor);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isDonor = getArguments().getBoolean(ARG_IS_DONOR);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        database = FirebaseFirestore.getInstance();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_requests, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvDonationRequests = view.findViewById(R.id.rvDonationRequests);
        rvDonationRequests.setLayoutManager(new LinearLayoutManager(getContext()));
        String userId;
        if (isDonor) {
            userId = new AppUtils().getDonorUserAccount().id;
        } else {
            userId = new AppUtils().getBankUserAccount().id;
        }

        database.collection(Constants.KEY_COLLECTION_BLOOD_REQUESTS)
                .whereEqualTo("userId", userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                donationRequests.add(document.toObject(DonationRequest.class));
                            }
                            rvDonationRequests.setAdapter(new MyRequestsAdapter(getContext(), donationRequests));
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}
