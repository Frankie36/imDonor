package com.mwikali.imdonor.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.mwikali.imdonor.R;
import com.mwikali.imdonor.fragment.NewsFragment;
import com.mwikali.imdonor.fragment.UrgentRequestsFragment;
import com.mwikali.imdonor.models.UserBank;
import com.mwikali.imdonor.utils.AppUtils;
import com.mwikali.imdonor.utils.ArticleUtils;

public class MainActivityBank extends AppCompatActivity {

    private TextView tvName, tvEmail;
    private ImageView imgProfile;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private UserBank userBank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_bank);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer);
        NavigationView navigationView = findViewById(R.id.nav_home_bank);
        FloatingActionButton fab = findViewById(R.id.fab);

        // Navigation view header
        View navHeader = navigationView.getHeaderView(0);
        tvName = navHeader.findViewById(R.id.tvName);
        tvEmail = navHeader.findViewById(R.id.tvEmail);
        imgProfile = navHeader.findViewById(R.id.imgProfile);

        navHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), BankProfileActivity.class));
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), NewBloodRequestActivity.class);
                Bundle bundle = new Bundle();
                bundle.putBoolean("isDonor", false);
                intent.putExtras(bundle);
                startActivity(intent);
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
                    case R.id.nav_donors:
                        showFragment(NewsFragment.newInstance());
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
        ArticleUtils.openCustomChromeTab(MainActivityBank.this, Uri.parse(link));
    }

    @Override
    protected void onResume() {
        super.onResume();
        userBank = new AppUtils().getBankUserAccount();
        if (userBank != null) {
            tvName.setText(userBank.name);
            tvEmail.setText(userBank.email);
        }
    }
}
