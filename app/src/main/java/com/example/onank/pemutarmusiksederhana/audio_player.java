package com.example.onank.pemutarmusiksederhana;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

public class audio_player extends AppCompatActivity implements View.OnClickListener {

    private Button btnPlay,btnBack,btnFor;
    private SeekBar seekBar;
    static private MediaPlayer mediaPlayer;
    private Runnable runnable;
    private Handler handler;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_player);

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
        ///cek lagunya
        if (mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();

        }

///ngambil data lagu dari Main activity
        Bundle bundle = getIntent().getExtras();

        ArrayList<File> songs = (ArrayList) bundle.getParcelableArrayList("list");
        int position = bundle.getInt("position");
        String judul = bundle.getString("judul");
        Uri uri =Uri.parse(songs.get(position).toString());

        mediaPlayer = MediaPlayer.create(this,uri);


    btnPlay = findViewById(R.id.btnPlay);
    btnBack = findViewById(R.id.btnBack);
    btnFor  = findViewById(R.id.btnFor);
    handler = new Handler();
    seekBar = findViewById(R.id.seekbar);
    title = findViewById(R.id.judul_txt);

    title.setText(judul);

    btnFor.setOnClickListener(this);
    btnBack.setOnClickListener(this);
    btnPlay.setOnClickListener(this);

    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
            seekBar.setMax(mediaPlayer.getDuration());
            mediaPlayer.start();
            changeSeekbar();
        }
    });

    seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
      //percepat/perlambat ketika digeser
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            if(b){
                mediaPlayer.seekTo(i);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    });
    }

    private void changeSeekbar() {
        //buat geser lagu
        seekBar.setProgress(mediaPlayer.getCurrentPosition());

        if(mediaPlayer.isPlaying()){
            runnable = new Runnable() {
                @Override
                public void run() {
                    changeSeekbar();

                }
            };

            handler.postDelayed(runnable, 1000);
        }
    }

    @Override
    public void onClick(View v) {
        //ketika klik tombol play stop dll
        switch (v.getId()){
            case R.id.btnPlay:
                if(mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    btnPlay.setBackgroundResource(R.drawable.music_play);
                    btnPlay.setText("");
                }else{
                    mediaPlayer.start();
                    btnPlay.setBackgroundResource(R.drawable.music_stop);
                    btnPlay.setText("");
                    changeSeekbar();
                }
                break;
            case R.id.btnFor:
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition()+5000);
                break;
            case R.id.btnBack:
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition()-5000);
                break;
        }
    }
}