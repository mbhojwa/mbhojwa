package com.example.project10;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class service extends Service {
    public static MediaPlayer player;
    public static Boolean shuffle;
    int song_play;
    List<Integer> list = new ArrayList<Integer>();
    int start_Position;
    int list_length;

    /**
     * start playing the music
     * @param intent
     * @param flags
     * @param startId
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        shuffle = false;
        start_Position = (int) intent.getExtras().get("song_position");
        list_length = (int) intent.getExtras().get("song_length");
        list.clear();
        for (int i = start_Position; i < list_length; i++) {
            list.add((int) intent.getExtras().get("song" + i));
        }
        song_play = list.get(0);
        musicplayer();
        return START_STICKY;
    }

    /**
     * override onDestroy method
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * override onBind method
     * @param intent
     * @return
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * create a MediaPlayer and start playing the music
     */
    public void musicplayer() {
        if (player != null) {
            player.stop();
        }
        player = MediaPlayer.create(this, song_play);
        player.start();

        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if (shuffle) {
                    playmusicShuffle();
                }
                else {
                    playSongNext();
                }
            }
        });

    }

    /**
     * plat the next audio when completion
     */
    public void playSongNext() {
        if (start_Position == list_length - 1) {
            return;
        }
        start_Position += 1;
        song_play = list.get(start_Position);
        musicplayer();
    }

    /**
     * play the next random audio when completion
     */
    public void playmusicShuffle() {
        Random r = new Random();
        int randomNumber = r.nextInt(list_length);
        song_play = list.get(randomNumber);
        musicplayer();
    }
}

