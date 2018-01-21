package com.example.shikhar.weatherappfinal;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class WeatherDetails extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final int MY_PERMISSION_REQUEST_CODE = 7171;

    private GoogleApiClient googleApiClient;


    TextView cityTv,weather,status,description;
    Typeface heading,subheading,mainWeather;
    @Override
    protected void onStart() {
        super.onStart();
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                   // Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
                    makeClient();
                }
        }
    }

    private void makeClient() {
        googleApiClient = new GoogleApiClient.Builder(this, this,this).addApi(LocationServices.API).build();
        //Toast.makeText(this, "Client created", Toast.LENGTH_SHORT).show();
        googleApiClient.connect();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
         heading= Typeface.createFromAsset(getAssets(), "fonts/Raleway-MediumItalic.ttf");
         subheading=Typeface.createFromAsset(getAssets(), "fonts/Walkway_Oblique_UltraBold.ttf");
         mainWeather=Typeface.createFromAsset(getAssets(), "fonts/Quicksand-BoldItalic.otf");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_details);
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.rl);

        rl.animate().alpha(1).setDuration(1500);



        cityTv=(TextView)findViewById(R.id.city);

        //temp1=(TextView)findViewById(R.id.temp1);
        weather=(TextView)findViewById(R.id.weather);
        status=(TextView)findViewById(R.id.status);
        description=(TextView) findViewById(R.id.des);

        description.setTypeface(subheading);
        description.setText("  Please wait...  ");


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED
                ) {
            //Run-time request permission
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, MY_PERMISSION_REQUEST_CODE);
        }
        else{
            makeClient();
        }


    }

    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED
                ) {
            Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);




            if(lastLocation!=null) {
                double lat = lastLocation.getLatitude(), lon = lastLocation.getLongitude();
                //double lat=28.3743876;
                //double lon=79.4229749;
                Log.i("Location :", lat + "    " + lon);

                //String units = "imperial";
                //String url = "https://fcc-weather-api.glitch.me/api/current?lat=" + lat + "&lon=" + lon;
                String url="http://api.weatherbit.io/v2.0/current?key=fa5d88dc82f54084892ac6f1f429c6ee&lat=28.3743876&lon=79.4229749";
                //String url="https://newsapi.org/v2/top-headlines?sources=the-times-of-india&apiKey=d2b71909d2424d578aafb8175bc8192c";
                GetWeatherTask on = new GetWeatherTask(getApplicationContext());
                on.execute(url);
            }
            else{
                Toast.makeText(this, "Sorry not compatible", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
        else{
            Toast.makeText(this, "Give permissions first", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }



    class GetWeatherTask extends AsyncTask<String, Void, String>{
        Context ctx;

        public GetWeatherTask(Context ctx) {
            this.ctx = ctx;

        }
        @Override
        protected String doInBackground(String... strings) {
            try{

                URL url= new URL(strings[0]);
                HttpURLConnection con=(HttpURLConnection) url.openConnection();
                con.connect();
                InputStream stream=con.getInputStream();
                InputStreamReader ins=new InputStreamReader(stream);
                BufferedReader reader=new BufferedReader(ins);
                StringBuffer buffer=new StringBuffer();

                String line="";


                while((line=reader.readLine())!=null){
                    buffer.append(line+"\n");
                }

                return buffer.toString();
            }catch(Exception e){
                Log.d("Background:","Hello Error");
                return e.getMessage().toString();
            }
        }



        @Override
        protected void onPostExecute(String temp) {
            String w,st="",des="",city;
            try {
                description.setAlpha(0);
                JSONObject topLevel = new JSONObject(temp);
                JSONObject main = topLevel.getJSONObject("main");
                JSONArray tempWeather=topLevel.getJSONArray("weather");
                for(int i=0;i<tempWeather.length();i++){
                    JSONObject c=tempWeather.getJSONObject(i);
                    st=c.getString("main");
                    des=c.getString("description");

                }

                w = String.valueOf(main.getDouble("temp"));
                city=topLevel.getString("name");

                String result="";


                cityTv.setTypeface(heading);
                cityTv.setText(city);

                weather.setText(w+"°C");
                weather.setTypeface(heading);
                status.setText(st.toUpperCase());
                status.setTypeface(heading);
                description.setTypeface(subheading);
                description.setText("  Feels like "+des+"  ");
                Log.i("Processed","yes");
                cityTv.animate().alpha(1).setDuration(2000);
                weather.animate().alpha(1).setDuration(2000);
                status.animate().alpha(1).setDuration(2000);
                description.animate().alpha(1).setDuration(2000);



















            }catch(Exception e){

                Log.i("error",e.getMessage());

            }

            //Toast.makeText(ctx, "Weather:"+weather, Toast.LENGTH_SHORT).show();
        }
    }

}

