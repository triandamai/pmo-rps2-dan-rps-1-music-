package com.example.onank.pemutarmusiksederhana;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private ListView listView;
    private String songNames[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        listView = findViewById(R.id.listview);
    //panggil fungsi membaca lagu di memory
        musicListview();


    }

    public void musicListview(){
        final ArrayList<File> songs = readSongs(Environment.getExternalStorageDirectory());

        songNames = new String[songs.size()];

        for (int i = 0; i < songs.size(); ++i){
            songNames[i] = songs.get(i).getName().replace(".mp3", "");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.layout_lagu,
                R.id.textView,songNames);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int orientation = getResources().getConfiguration().orientation;
//kirim lagu ke activity selanjutnya
                    startActivity(new Intent(MainActivity.this,audio_player.class)
                            .putExtra("position",position).putExtra("list",songs).putExtra("judul",songNames[position]));



            }
        });
    }



    private ArrayList<File> readSongs(File root){
        ArrayList<File> arrayList = new ArrayList<File>();
        File files[] = root.listFiles();

        for (File file : files){
            if (file.isDirectory()) {
                arrayList.addAll(readSongs(file));
            }else {
                if (file.getName().endsWith(".mp3")){
                    arrayList.add(file);
                }
            }
            }
            return arrayList;
        }

    @Override
    public void onClick(View v) {

        }
    }


