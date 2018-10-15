package com.example.onank.pemutarmusiksederhana;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.Locale;

public class splash extends AppCompatActivity {
ProgressBar pb;
    private static final int REQUEST_WRITE_STORAGE = 112;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        pb = findViewById(R.id.progress);
        //membaca lokal bahasa
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        Configuration config = getBaseContext().getResources().getConfiguration();

        String lang = settings.getString("LANG", "");
        if (!"".equals(lang) && !config.locale.getLanguage().equals(lang)) {
            Locale locale = new Locale(lang);
            Locale.setDefault(locale);
            config.locale = locale;
            getBaseContext()
                    .getResources()
                    .updateConfiguration(config, getBaseContext()
                            .getResources()
                            .getDisplayMetrics()
                    );
        }
        ///
        permisi();


    }
    private void permisi(){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
            Toast.makeText(this,"Android Lolipop Kebawah",Toast.LENGTH_SHORT);
             pindah();
        }
        if (sudahpermisi()){
            Toast.makeText(this,"Sudah mendapat ijin",Toast.LENGTH_SHORT);
           pindah();
        }
        try {
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_WRITE_STORAGE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                    //File write logic here
                   pindah();
                }else {
                    System.exit(1);
                }
            }

        }catch (Exception e){
            throw e;
        }


    }
    private boolean sudahpermisi() {
        return (ContextCompat.checkSelfPermission(getBaseContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED);

    }
    private void pindah(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(splash.this,MainActivity.class));
                finish();
            }
        },500);
    }
}
