package com.example.shikhar.weatherappfinal;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.Image;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView tv1,tv2,tv3,tv4;
    ImageView icon1,icon2;
    int counter=0;

    public void start(){
        new CountDownTimer(2110,700){

            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                icon1.animate().alpha(0).setDuration(1000);
                icon2.animate().alpha(1).setDuration(1000);
                counter++;
                tv4.animate().translationXBy(2000f).setDuration(1000);
                new CountDownTimer(3000,1000) {
                    @Override
                    public void onTick(long l) {

                    }

                    @Override
                    public void onFinish() {
                        Intent intent = new Intent(MainActivity.this, WeatherDetails.class);
                        startActivity(intent);
                    }
                }.start();


            }
        }.start();

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv1=(TextView) findViewById(R.id.tv1);
        tv2=(TextView) findViewById(R.id.tv2);
        tv3=(TextView) findViewById(R.id.tv3);
        tv4=(TextView) findViewById(R.id.tv4);




        Typeface typeface2= Typeface.createFromAsset(getAssets(), "fonts/Walkway_Oblique_Bold.ttf");
        Typeface typeface1= Typeface.createFromAsset(getAssets(), "fonts/Walkway_Oblique_SemiBold.ttf");
        Typeface typeface3=Typeface.createFromAsset(getAssets(),"fonts/Raleway-MediumItalic.ttf");
        tv4.setTranslationX(-2000f);
        tv4.setTypeface(typeface3);
        tv1.setTypeface(typeface1);
        tv3.setTypeface(typeface1);
        tv2.setTypeface(typeface2);

        icon1=(ImageView) findViewById(R.id.icon1);
        icon2=(ImageView) findViewById(R.id.icon2);
        /*
        icon1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                icon1.animate().alpha(0).setDuration(1000);
                icon2.animate().alpha(1).setDuration(1000);
                if(counter==0){
                    tv4.setAlpha(1);
                    tv4.animate().translationXBy(2000f).setDuration(1000);
                    counter=1;

                    new CountDownTimer(4010,1000){

                        @Override
                        public void onTick(long l) {

                        }

                        @Override
                        public void onFinish() {
                            Intent intent=new Intent(MainActivity.this,WeatherDetails.class);
                            startActivity(intent);
                        }
                    }.start();

                }



            }
        });*/

        start();





    }

    @SuppressWarnings("deprecation")
    public void onStart(){
        super.onStart();

        if(counter==1) {
            tv4.setTranslationX(-2000f);
            counter=0;
            icon1.animate().alpha(1).setDuration(100);
            icon2.animate().alpha(0).setDuration(100);
            start();

        }

    }

    public void onResume(){
        super.onResume();

        if(counter==1) {
            tv4.setTranslationX(-2000f);
            counter=0;
            icon1.animate().alpha(1).setDuration(100);
            icon2.animate().alpha(0).setDuration(100);


        }


    }



}
