package com.mk.musicplayer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MusicActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
TextView cDur,rDur,name;
ImageView play,next,previous;
SeekBar seekBar;
ImageView imageView;
MediaPlayer mediaPlayer;
ArrayList<String> sData=new ArrayList<>();
int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        rDur=findViewById(R.id.rdur);
        cDur=findViewById(R.id.cdur);
        name=findViewById(R.id.songName);
        imageView=findViewById(R.id.imageView);
        seekBar=findViewById(R.id.seekBar);
        play=findViewById(R.id.play);
        next=findViewById(R.id.next);
        previous=findViewById(R.id.pre);
        Bundle b=getIntent().getExtras();

        String data=b.getString("songData");
        sData=b.getStringArrayList("SongData");
        position=b.getInt("pos",0);
        if (mediaPlayer!=null){
            mediaPlayer.release();
        }
        mediaPlayer=MediaPlayer.create(this, Uri.parse(data));
        MediaMetadataRetriever mmr=new MediaMetadataRetriever();
        mmr.setDataSource(data);
        String title=b.getString("songName");
        name.setText(title);
        name.setSelected(true);
        byte [] d=mmr.getEmbeddedPicture();
        if (d!=null){
            Bitmap bmp= BitmapFactory.decodeByteArray(d,0,d.length);
            imageView.setImageBitmap(bmp);
        }
        else
        {
            imageView.setImageResource(R.drawable.music);
        }
        rDur.setText(convertToDuration(mediaPlayer.getDuration()));
        seekBar.setMax(mediaPlayer.getDuration());
        seekBar.setOnSeekBarChangeListener(this);
        play.setOnClickListener(this);
        new Thread(){
            @Override
            public void run() {
                while (mediaPlayer.getCurrentPosition()!= mediaPlayer.getDuration()){
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                }
            }
        }.start();
mediaPlayer.start();

// Next song Play section start
next.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        position=position+1;
        String data=sData.get(position);
        if (mediaPlayer!=null){
            mediaPlayer.release();
        }
        mediaPlayer=MediaPlayer.create(getApplicationContext(),Uri.parse(data));
    }
});

//Next Song Play section End
    }
    String convertToDuration(long dur){
        String d="" ;
        dur=dur/1000;
        d=d+dur/60+":"+dur%60;
        return d;
    }

    @Override
    public void onClick(View v) {
        if (!mediaPlayer.isPlaying()){
            mediaPlayer.start();
            play.setImageResource(R.drawable.pause);
        }
        else
        {
            mediaPlayer.pause();
            play.setImageResource(R.drawable.circle);
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
    if (fromUser){
        mediaPlayer.seekTo(progress);
    }
    rDur.setText(convertToDuration(mediaPlayer.getDuration()-mediaPlayer.getCurrentPosition()));
    cDur.setText(convertToDuration(mediaPlayer.getCurrentPosition()));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}