package com.example.myweather;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.location.FusedLocationProviderClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    List<Double> ll = null;
    String location, country;
    private String city = "";
    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        FetchData fetchData = new FetchData();
        fetchData.execute();
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage(R.string.title);
        builder.setTitle(R.string.Exit);
        builder.setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user pressed "yes", then he is allowed to exit from application
                finish();
            }
        });
        builder.setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user select "No", just cancel this dialog and continue with app
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.about:
                //Toast.makeText(this, "About", Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(MainActivity.this, About.class);
                startActivity(intent1);
                return true;
            case R.id.setting:
                //Toast.makeText(this, "Setting", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, Setting.class);
                startActivity(intent);
                return true;
            case R.id.gps:
                Toast.makeText(this, "gps", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(getIntent());
                return true;
            case R.id.refresh:
                finish();
                startActivity(getIntent());
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @SuppressLint("StaticFieldLeak")
    public class FetchData extends AsyncTask<String,Void,String> {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String forecastJsonStr = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @SuppressLint("DefaultLocale")
        @Override
        protected void onPostExecute(String s) {
            try {

                JSONObject root = new JSONObject(forecastJsonStr);

                TextView locDis = findViewById(R.id.locName);
                final TextView liveTime = findViewById(R.id.liveTime);
                final TextView liveDate = findViewById(R.id.liveDate);

                ListView listView =findViewById(R.id.dayList);
                final List<String> disLocation = new ArrayList<>();

                final String[] dayName  =  new String[7];
                final String[] date  =  new String[7];
                final String[] tempValue  =new String[7];
                final Integer[] icon_list = new Integer[7];
                final String[] dayDec = new String[7];
                final String[] humidity = new String[7];
                final String[] windSpeed = new String[7];
                final String[] pressure = new String[7];

                final String[] minMaxTemp = new String[7];

                JSONArray array= root.getJSONArray("daily");

                location = location.substring(0, 1).toUpperCase() + location.substring(1);

                for(int i=0;i<7;i++)
                {
                    JSONObject object= array.getJSONObject(i);
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
                    SimpleDateFormat sdf_1 = new SimpleDateFormat("yyyy-MM-dd");
                    String unixTimeString = object.getString("dt");
                    long unixTimeInt = Integer.parseInt(unixTimeString);
                    Date dateFormat = new Date(unixTimeInt * 1000);
                    String weekday = sdf.format(dateFormat);

                    sdf_1.setTimeZone(java.util.TimeZone.getTimeZone("GMT-4"));
                    String formattedDate = sdf_1.format(dateFormat);

                    date[i] = formattedDate;

                    if (i == 0){
                        dayName[i] = weekday+"(TODAY)";
                    }else{
                        dayName[i] = weekday;
                    }
                    JSONObject tempObj= array.getJSONObject(i);
                    JSONObject tempObjDay= tempObj.getJSONObject("temp");
                    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                    String unit = sharedPref.getString("unitType", "1");
                    Double temp = tempObjDay.getDouble("day");
                    Double minTemp = tempObjDay.getDouble("min");
                    Double maxTemp = tempObjDay.getDouble("max");

                    if(unit.equals("1")){
                        Double tempV = temp - 273.15;
                        Double tempMin = minTemp - 273.15;
                        Double tempMax = maxTemp - 273.15;
                        tempValue[i] = String.format("%.2f"+"\u00B0"+"C", tempV);
                        minMaxTemp[i] = String.format("▼ %.2f"+"\u00B0"+"C\n%.2f"+"\u00B0"+"C  ▲", tempMin,tempMax);

                    }else{
                        Double tempV = (temp-273.15)*(9.0f/5.0f) +32.0f;
                        Double tempMin = (minTemp-273.15)*(9.0f/5.0f) +32.0f;
                        Double tempMax = (maxTemp-273.15)*(9.0f/5.0f) +32.0f;
                        tempValue[i] =String.format("%.2f"+"\u00B0"+"F", tempV);
                        minMaxTemp[i] = String.format("▼ %.2f"+"\u00B0"+"F\n%.2f"+"\u00B0"+"C  ▲", tempMin,tempMax);
                    }

                    JSONArray weaDes = object.getJSONArray("weather");
                    JSONObject desc = weaDes.getJSONObject(0);
                    String Desc1 = desc.getString("description");
                    String icon = desc.getString("icon");
                    String Desc2 = Desc1.substring(0, 1).toUpperCase() + Desc1.substring(1);
                    dayDec[i] = Desc2;

                    String pressure1 = object.getString("pressure");
                    String humidity1 = object.getString("humidity");
                    String windSpeed1 = object.getString("wind_speed");
                    if (i == 0){
                        humidity[i] = "Humidity\n" + humidity1 + " %";
                    }else{
                        humidity[i] = "Humidity : " + humidity1 + " %";
                    }

                    pressure[i] = "Pressure : " + pressure1 + " milibars";
                    windSpeed[i] = "Wind Speed :" + windSpeed1 + " m/s";

                    if(icon.equals("01d")){
                        icon_list[i] = R.drawable.clear;
                    }else if(icon.equals("01n")){
                        icon_list[i] = R.drawable.clear_n;
                    }else if(icon.equals("02d")){
                        icon_list[i] = R.drawable.few_clouds;
                    }else if(icon.equals("02n")){
                        icon_list[i] = R.drawable.few_clouds_n;
                    }else if(icon.equals("03d")){
                        icon_list[i] = R.drawable.scattered_clouds;
                    }else if(icon.equals("03n")){
                        icon_list[i] = R.drawable.scattered_clouds;
                    }else if(icon.equals("04d")){
                        icon_list[i] = R.drawable.broken_clouds;
                    }else if(icon.equals("04n")){
                        icon_list[i] = R.drawable.broken_clouds;
                    }else if(icon.equals("09d")){
                        icon_list[i] = R.drawable.shower_rain;
                    }else if(icon.equals("09n")){
                        icon_list[i] = R.drawable.shower_rain;
                    }else if(icon.equals("10d")){
                        icon_list[i] = R.drawable.rain;
                    }else if(icon.equals("10n")){
                        icon_list[i] = R.drawable.rain_n;
                    }else if(icon.equals("11d")){
                        icon_list[i] = R.drawable.thunderstorm;
                    }else if(icon.equals("11n")){
                        icon_list[i] = R.drawable.thunderstorm;
                    }else if(icon.equals("13d")){
                        icon_list[i] = R.drawable.snow;
                    }else if(icon.equals("13n")){
                        icon_list[i] = R.drawable.snow;
                    }else if(icon.equals("50d")){
                        icon_list[i] = R.drawable.mist;
                    }else if(icon.equals("50n")){
                        icon_list[i] = R.drawable.mist;
                    }else{
                        icon_list[i] = R.drawable.clear;
                    }

                    String con = location+","+country;
                    disLocation.add(con);

                }

                //////////////
                CountDownTimer newtimer = new CountDownTimer(1000000000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        Calendar c = Calendar.getInstance();
                        liveTime.setText("Time : "+c.get(Calendar.HOUR)+":"+c.get(Calendar.MINUTE)+":"+c.get(Calendar.SECOND));
                    }
                    public void onFinish() {

                    }
                };
                newtimer.start();
                if(android.os.Build.VERSION.SDK_INT>=android.os.Build.VERSION_CODES.O){
                    LocalDateTime time = LocalDateTime.now();
                    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    String dateTime = time.format(dateTimeFormatter);
                    liveDate.setText("Date : "+String.valueOf(dateTime));

                }
                /////////////////////////

                locDis.setText(location);
                //ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.my_list,R.id.dayName, dayName);
                CustomListAdapter adapter = new CustomListAdapter(MainActivity.this, dayName, tempValue, icon_list, dayDec,humidity,minMaxTemp);
                findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        String displayDay = dayName[+position];
                        String displayDate = date[+position];
                        String displayTemp = tempValue[+position];
                        String cityName = disLocation.get(+position);
                        String dayDesc = dayDec[+position];
                        String dayPre = pressure[+position];
                        String dayWind = windSpeed[+position];
                        String dayHum = humidity[+position];
                        Integer slecticon = icon_list[+position];

                        //Toast.makeText(getApplicationContext(), displayTemp, Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(MainActivity.this,MainActivity2.class);

                        intent.putExtra("disDay", displayDay);
                        intent.putExtra("disDate", displayDate);
                        intent.putExtra("disTemperature", displayTemp);
                        intent.putExtra("disLocation", cityName);
                        intent.putExtra("Listviewclickicon", slecticon);
                        intent.putExtra("dayDesc", dayDesc);
                        intent.putExtra("dayPre", dayPre);
                        intent.putExtra("dayWind", dayWind);
                        intent.putExtra("dayHum", dayHum);

                        startActivity(intent);
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                List<Double> gCode = new ArrayList<>();

                //Double lat = 6.9319;
                //Double lon = 79.8478;

                //gCode.add(lat);
                //gCode.add(lon);
                //boolean activityWasLauched = false;
                final SharedPreferences mSharedPreference= PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                String value=(mSharedPreference.getString("cityName", "colombo"));


                location = value;

                Geocoder gc = new Geocoder(getApplicationContext());
                List<Address> addresses= gc.getFromLocationName(location, 5);

                ll = new ArrayList<>(addresses.size()); // A list to save the coordinates if they are available
                for(Address a : addresses) {
                    if (a.hasLatitude() && a.hasLongitude()) {
                        ll.add(a.getLatitude());
                        ll.add(a.getLongitude());
                    }
                }
                country = addresses.get(0).getCountryName();
                final String BASE_URL ="https://api.openweathermap.org/data/2.5/onecall?lat="+ll.get(0)+"&lon="+ll.get(1)+"&exclude=minutely,hourly,alerts&appid=e1b571feaf5cfa3c8fbaf4f191149813";
                URL url = new URL(BASE_URL);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                if (inputStream == null) { return null; }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line1;

                while ((line1 = reader.readLine()) != null) { buffer.append(line1 + "\n"); }
                if (buffer.length() == 0) { return null; }
                forecastJsonStr = buffer.toString();

            } catch (IOException e) {
                Log.e("Hi", "Error ", e);
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("cityName", "colombo");
                editor.commit();
                return null;
            } finally{
                if (urlConnection != null) { urlConnection.disconnect(); }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("Hi", "Error closing stream", e);
                    }
                }
            }
            return null;
        }
    }
}