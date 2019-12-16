package com.mwikali.imdonor.activity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.mwikali.imdonor.R;

public class NewsDetailActivity extends AppCompatActivity {

    private ImageView imgTutorialImage, imgNewsPublisher;
    private TextView tvPublisherName, tvTimeStamp, tvTutorialTitle, tvTutorialContent, tvReadMore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initViews();
    }

    private void initViews() {
        imgTutorialImage = findViewById(R.id.imgTutorialImage);
        tvPublisherName = findViewById(R.id.tvPublisherName);
        imgNewsPublisher = findViewById(R.id.imgNewsPublisher);
        tvTimeStamp = findViewById(R.id.tvTimeStamp);
        tvTutorialTitle = findViewById(R.id.tvTutorialTitle);
        tvTutorialContent = findViewById(R.id.tvTutorialContent);
        tvReadMore = findViewById(R.id.tvReadMore);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
