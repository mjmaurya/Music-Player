package com.mk.musicplayer;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class SongCursor extends CursorAdapter {
    public SongCursor(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.listview,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView song=view.findViewById(R.id.songName);
        TextView singer=view.findViewById(R.id.artist);
        TextView album=view.findViewById(R.id.album);
        String songName=cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));
        String singerName=cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
        String albumName=cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
        song.setText(songName);
        singer.setText(singerName);
        album.setText(albumName);
    }
}
