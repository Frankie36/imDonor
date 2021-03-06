package com.mwikali.imdonor.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mwikali.imdonor.R;
import com.mwikali.imdonor.fragment.NewsFragment;
import com.mwikali.imdonor.fragment.UrgentRequestsFragment;
import com.mwikali.imdonor.models.UserDonor;
import com.mwikali.imdonor.utils.AppUtils;
import com.mwikali.imdonor.utils.ArticleUtils;

public class MainActivityDonor extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private TextView tvName, tvEmail;
    private ImageView imgProfile;
    private UserDonor userDonor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_donor);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer);
        NavigationView navigationView = findViewById(R.id.nav_home_donor);
        FloatingActionButton fab = findViewById(R.id.fab);

        // Navigation view header
        View navHeader = navigationView.getHeaderView(0);
        tvName = navHeader.findViewById(R.id.tvName);
        tvEmail = navHeader.findViewById(R.id.tvEmail);
        imgProfile = navHeader.findViewById(R.id.imgProfile);

        navHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), DonorProfileActivity.class));
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), NewBloodRequestActivity.class);
                Bundle bundle = new Bundle();
                bundle.putBoolean("isDonor", true);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        setUpNavigationView();

        showFragment(UrgentRequestsFragment.newInstance());

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_urgent_request:
                        showFragment(UrgentRequestsFragment.newInstance());
                        break;
                    case R.id.nav_news:
                        showFragment(NewsFragment.newInstance());
                        break;
                    case R.id.nav_donation_centres:
                        openLink("https://nbtskenya.or.ke/donation-centers/");
                        break;
                    case R.id.nav_faq:
                        openLink("https://nbtskenya.or.ke/faqs/");
                        break;
                    case R.id.nav_quiz:
                        openLink("https://nbtskenya.or.ke/blood-drives/can-i-give-blood/eligibility-quiz/");
                        break;
                    case R.id.nav_before_attending:
                        openLink("https://nbtskenya.or.ke/blood-drives/before-attending/");
                        break;
                    case R.id.nav_about_blood:
                        openLink("https://nbtskenya.or.ke/blood-drives/learn-about-blood/");
                        break;
                }

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }

                menuItem.setChecked(true);

                drawerLayout.closeDrawers();
                return true;
            }
        });
    }

    private void showFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.home_container, fragment)
                .commit();
    }

    private void setUpNavigationView() {
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    private void openLink(String link) {
        ArticleUtils.openCustomChromeTab(MainActivityDonor.this, Uri.parse(link));
    }


    @Override
    protected void onResume() {
        super.onResume();
        userDonor = new AppUtils().getDonorUserAccount();
        if (userDonor != null) {
            tvName.setText(String.format("%s %s", userDonor.firstName, userDonor.lastName));
            if (!userDonor.email.isEmpty()) {
                tvEmail.setText(userDonor.email);
            } else {
                tvEmail.setText(userDonor.phone);
            }
        }
    }
}
