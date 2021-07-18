package com.example.myweather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

public class About extends AppCompatActivity {
    ImageView imageView1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        getSupportActionBar().setTitle("About");
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        imageView1=(ImageView) findViewById(R.id.icon_ac3);
        imageView1.setImageResource(R.drawable.snow);
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}