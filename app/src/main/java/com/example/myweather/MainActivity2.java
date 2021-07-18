package com.example.myweather;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity2 extends AppCompatActivity {
    TextView disTemp,disCity,date,disHum,disWind,disPre,disDes,disDate;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        getSupportActionBar().setTitle("Detail View");
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        disTemp=(TextView) findViewById(R.id.tempValueSnd);
        disCity=(TextView) findViewById(R.id.cityName);
        date=(TextView) findViewById(R.id.date);

        disHum=(TextView) findViewById(R.id.disHum);
        disWind=(TextView) findViewById(R.id.disWind);
        disPre=(TextView) findViewById(R.id.disPre);
        disDes=(TextView) findViewById(R.id.disDec);
        disDate=(TextView) findViewById(R.id.dateName);

        imageView=(ImageView) findViewById(R.id.icon_ac2);


        Bundle bundle = getIntent().getExtras();
        Integer Tempicon = bundle.getInt("Listviewclickicon");
        String tempDis = getIntent().getStringExtra("disTemperature");
        String cityDis = getIntent().getStringExtra("disLocation");

        String disHum1 = getIntent().getStringExtra("dayHum");
        String disWind1 = getIntent().getStringExtra("dayWind");
        String disPre1 = getIntent().getStringExtra("dayPre");
        String disDes1 = getIntent().getStringExtra("dayDesc");
        String disDate1 = getIntent().getStringExtra("disDay");
        String disDate2 = getIntent().getStringExtra("disDate");

        imageView.setImageResource(Tempicon);
        disTemp.setText(tempDis);
        disCity.setText(cityDis);

        disDes.setText(disDes1);
        disHum.setText(disHum1);
        disWind.setText(disWind1);
        disPre.setText(disPre1);
        disDate.setText(disDate1);
        date.setText(disDate2);
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}