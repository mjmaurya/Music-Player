package com.mk.musicplayer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
Cursor c;
ListView lv;
SearchView searchView;
SongCursor songCursor;
AdView adView;
ArrayList<String> sData=new ArrayList<>();
ArrayList<String> sName=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv=findViewById(R.id.list);
        searchView=findViewById(R.id.search);
        MobileAds.initialize(this,
                getString(R.string.admob_app_id));
        adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        // Display Banner ad
        adView.loadAd(adRequest);
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            getData();
        }
        lv.setOnItemClickListener(this);
    }

    private void getData() {
        c=getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,null,null,null,null);
        songCursor=new SongCursor(this,c,10);
        lv.setAdapter(songCursor);
        Cursor c1=getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,null,null,null,null);
        while (c1.moveToNext()){
            int i=c1.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME);
            int j=c.getColumnIndex(MediaStore.Audio.Media.DATA);
            sData.add(c1.getString(j));
            sName.add(c1.getString(i));
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent=new Intent(MainActivity.this,MusicActivity.class);
        intent.putExtra("songName",sName.get(position));
        intent.putExtra("SongData",sData);
        intent.putExtra("pos",position);
        intent.putExtra("songData",sData.get(position));
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    getData();
                }
                break;

            default:
                break;
        }
    }
}