package com.Music_Player.audioplayer.controls;

import android.content.Context;

import com.Music_Player.audioplayer.AudioPlayerActivity;
import com.Music_Player.audioplayer.R;
import com.Music_Player.audioplayer.service.SongService;
import com.Music_Player.audioplayer.util.PlayerConstants;
import com.Music_Player.audioplayer.util.UtilFunctions;

import java.util.Random;

public class Controls {
    private Random rand;
    static String LOG_CLASS = "Controls";


    public static void playControl(Context context) {
        sendMessage(context.getResources().getString(R.string.play));
    }

    public static void pauseControl(Context context) {
        sendMessage(context.getResources().getString(R.string.pause));
    }

    public static void nextControl(Context context) {
        boolean isServiceRunning = UtilFunctions.isServiceRunning(SongService.class.getName(), context);
        if (!isServiceRunning)
            return;

        if (AudioPlayerActivity.shuffle1) {
            int newSong = PlayerConstants.SONG_NUMBER;
            while (newSong == PlayerConstants.SONG_NUMBER) {
                newSong = AudioPlayerActivity.rand.nextInt(PlayerConstants.SONGS_LIST.size());
            }
            PlayerConstants.SONG_NUMBER = newSong;

            PlayerConstants.SONG_CHANGE_HANDLER.sendMessage(PlayerConstants.SONG_CHANGE_HANDLER.obtainMessage());
            PlayerConstants.SONG_PAUSED = false;
        } else {

            if (PlayerConstants.SONGS_LIST.size() > 0) {
                if (PlayerConstants.SONG_NUMBER < (PlayerConstants.SONGS_LIST.size() - 1)) {
                    PlayerConstants.SONG_NUMBER++;
                    PlayerConstants.SONG_CHANGE_HANDLER.sendMessage(PlayerConstants.SONG_CHANGE_HANDLER.obtainMessage());
                } else {
                    PlayerConstants.SONG_NUMBER = 0;
                    PlayerConstants.SONG_CHANGE_HANDLER.sendMessage(PlayerConstants.SONG_CHANGE_HANDLER.obtainMessage());
                }
            }
            PlayerConstants.SONG_PAUSED = false;

        }


    }


    public static void previousControl(Context context) {
        boolean isServiceRunning = UtilFunctions.isServiceRunning(SongService.class.getName(), context);
        if (!isServiceRunning)
            return;

        if (AudioPlayerActivity.shuffle1) {
            int newSong = PlayerConstants.SONG_NUMBER;
            while (newSong == PlayerConstants.SONG_NUMBER) {
                newSong = AudioPlayerActivity.rand.nextInt(PlayerConstants.SONGS_LIST.size());
            }
            PlayerConstants.SONG_NUMBER = newSong;

            PlayerConstants.SONG_CHANGE_HANDLER.sendMessage(PlayerConstants.SONG_CHANGE_HANDLER.obtainMessage());
            PlayerConstants.SONG_PAUSED = false;
        } else {
            if (PlayerConstants.SONGS_LIST.size() > 0) {
                if (PlayerConstants.SONG_NUMBER > 0) {
                    PlayerConstants.SONG_NUMBER--;
                    PlayerConstants.SONG_CHANGE_HANDLER.sendMessage(PlayerConstants.SONG_CHANGE_HANDLER.obtainMessage());
                } else {
                    PlayerConstants.SONG_NUMBER = PlayerConstants.SONGS_LIST.size() - 1;
                    PlayerConstants.SONG_CHANGE_HANDLER.sendMessage(PlayerConstants.SONG_CHANGE_HANDLER.obtainMessage());
                }
            }
            PlayerConstants.SONG_PAUSED = false;

        }


    }

    private static void sendMessage(String message) {
        try {
            PlayerConstants.PLAY_PAUSE_HANDLER.sendMessage(PlayerConstants.PLAY_PAUSE_HANDLER.obtainMessage(0, message));
        } catch (Exception e) {
        }
    }
}
