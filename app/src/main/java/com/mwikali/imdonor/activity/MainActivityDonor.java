package com.mwikali.imdonor.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.view.MenuItem;
import android.view.View;

import com.mwikali.imdonor.R;
import com.mwikali.imdonor.fragment.MyRequestsFragment;
import com.mwikali.imdonor.fragment.NewsFragment;
import com.mwikali.imdonor.fragment.UrgentRequestsFragment;
import com.mwikali.imdonor.utils.ArticleUtils;

public class MainActivityDonor extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_donor);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        drawerLayout = findViewById(R.id.drawer);
        NavigationView navigationView = findViewById(R.id.nav_home_donor);
        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), NewBloodRequestActivity.class));
            }
        });

        setUpNavigationView();

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
                    case R.id.nav_my_request:
                        showFragment(MyRequestsFragment.newInstance("", ""));
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
                drawerLayout.closeDrawers();
                return false;
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

}
