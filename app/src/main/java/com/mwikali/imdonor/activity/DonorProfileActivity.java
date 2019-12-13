package com.mwikali.imdonor.activity;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.mwikali.imdonor.R;
import com.mwikali.imdonor.fragment.DonationHistoryFragment;
import com.mwikali.imdonor.fragment.MyRequestsFragment;
import com.mwikali.imdonor.models.UserDonor;
import com.mwikali.imdonor.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

import cn.iwgang.countdownview.CountdownView;

public class DonorProfileActivity extends AppCompatActivity {

    private TextView tvName, tvEmail, tvPhone, tvBloodGroup;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private UserDonor userDonor;
    private CountdownView ctdnDonation;
    private SwitchCompat swtchIsDonor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initViews();
        setupViewPager(viewPager);

        tabLayout.setupWithViewPager(viewPager);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void initViews() {
        viewPager = findViewById(R.id.vpProfile);
        tabLayout = findViewById(R.id.tabProfile);
        ctdnDonation = findViewById(R.id.ctdnDonation);
        tvName = findViewById(R.id.tvName);
        tvEmail = findViewById(R.id.tvEmail);
        tvPhone = findViewById(R.id.tvPhone);
        tvBloodGroup = findViewById(R.id.tvBloodGroup);
        swtchIsDonor = findViewById(R.id.swtchIsDonor);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(DonationHistoryFragment.newInstance("", ""), getString(R.string.history));
        adapter.addFragment(MyRequestsFragment.newInstance(true, ""), getString(R.string.my_requests));
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_logout:
                new AppUtils().logout(getApplicationContext());
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        userDonor = new AppUtils().getDonorUserAccount();
        tvName.setText(String.format("%s %s", userDonor.firstName, userDonor.lastName));
        if (userDonor.email.isEmpty()) {
            tvEmail.setVisibility(View.GONE);
        } else {
            tvEmail.setText(userDonor.email);
        }

        if (userDonor.phone.isEmpty()) {
            tvPhone.setVisibility(View.GONE);
        } else {
            tvPhone.setText(userDonor.phone);
        }

        tvBloodGroup.setText(userDonor.bloodGroup);
        swtchIsDonor.setChecked(userDonor.isDonor);
    }
}
