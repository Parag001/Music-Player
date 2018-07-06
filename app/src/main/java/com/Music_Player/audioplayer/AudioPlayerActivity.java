package com.Music_Player.audioplayer;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.Music_Player.audioplayer.controls.Controls;
import com.Music_Player.audioplayer.service.SongService;
import com.Music_Player.audioplayer.util.PlayerConstants;
import com.Music_Player.audioplayer.util.UtilFunctions;

import java.util.Random;

public class AudioPlayerActivity extends AppCompatActivity {
	static ImageButton shuffle;
	Button btnBack;
	static Button btnPause;
	Button btnNext,stop;
	static Button btnPlay;
	static TextView textNowPlaying;
	static TextView textAlbumArtist;
	//static TextView textAlbumcomposer;
	static LinearLayout linearLayoutPlayer;
	ProgressBar progressBar;
	static Context context;
	public static boolean shuffle1=false;
	public static Random rand;
	public static SeekBar seekBar;




	TextView textBufferDuration, textDuration;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_ACTION_BAR);
		setContentView(R.layout.audio_player);

		context = this;
		rand=new Random();
		init();
		Explode enterTransition = null;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			enterTransition = new Explode();
		}
		enterTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			getWindow().setEnterTransition(enterTransition);
		}



	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.nowplaying_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();

		if (id == R.id.nav_share) {
			//String sharePath = "file:///storage/emulated/0/Songs/Zingaat.mp3";
			String composer = PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getPath();
			Uri uri = Uri.parse("file://"+composer);
			Intent share = new Intent(Intent.ACTION_SEND);
			share.setType("audio/*");
			share.putExtra(Intent.EXTRA_STREAM, uri);
			share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
			startActivity(Intent.createChooser(share, "Share Music File"));

		}



		return super.onOptionsItemSelected(item);
	}




	public void setShuffle1(){
		if(shuffle1) shuffle1=false;
		else shuffle1=true;
	}


	private void init() {



		getViews();
		setListeners();

		seekBar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.orange), Mode.SRC_IN);

		PlayerConstants.PROGRESSBAR_HANDLER = new Handler(){
			 @Override
		        public void handleMessage(Message msg){
				 Integer i[] = (Integer[])msg.obj;
				 textBufferDuration.setText(UtilFunctions.getDuration(i[0]));
				 textDuration.setText(UtilFunctions.getDuration(i[1]));
				 //seekBar.setProgress(i[2]);
				 seekBar.setMax(SongService.mp.getDuration());
				 seekBar.setProgress(SongService.mp.getCurrentPosition());
			 }
		};
		seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

				if(SongService.mp != null && fromUser){
					SongService.mp.seekTo(progress);
				}
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {

			}
		});
		//handler.postDelayed(runnable,1000);
	}


	private void setListeners() {
		btnBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

					Controls.previousControl(getApplicationContext());

			}
		});
		
		btnPause.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Controls.pauseControl(getApplicationContext());
			}
		});
		
		btnPlay.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Controls.playControl(getApplicationContext());
			}
		});
		
		btnNext.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				Controls.nextControl(getApplicationContext());


			}
		});


		shuffle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				//Code Here For Shuffle
				/*
				Log.d("TAG", "TAG Tapped INOUT(IN)");
				PlayerConstants.SONG_PAUSED = false;
				boolean isServiceRunning = UtilFunctions.isServiceRunning(SongService.class.getName(), getApplicationContext());
				if (isServiceRunning) {
					PlayerConstants.SONG_CHANGE_HANDLER.sendMessage(PlayerConstants.SONG_CHANGE_HANDLER.obtainMessage());
					Toast.makeText(getApplicationContext(), "Shuffle On", Toast.LENGTH_SHORT).show();

					updateUI();
					changeButton1();
					Log.d("TAG", "TAG Tapped INOUT(OUT)");
				}else{
					Toast.makeText(getApplicationContext(), "Shuffle Off", Toast.LENGTH_SHORT).show();
				}


				*/
				setShuffle1();

				if(shuffle1)
				{

					Toast.makeText(getApplicationContext(), "Shuffle On", Toast.LENGTH_SHORT).show();
					shuffle.setBackgroundResource(R.drawable.img_btn_shuffle_pressed);
				}
				else

				{

					Toast.makeText(getApplicationContext(), "Shuffle Off", Toast.LENGTH_SHORT).show();
					shuffle.setBackgroundResource(R.drawable.img_btn_shuffle);
				}




			}
		});


		stop.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//Controls.nextControl(getApplicationContext());
				Toast.makeText(getApplicationContext(), "Song Stopped", Toast.LENGTH_SHORT).show();
				Intent i = new Intent(context, SongService.class);
				context.stopService(i);
				Intent in = new Intent(context, MainActivity.class);
				in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(in);



			}
		});
	}
	
	public static void changeUI(){
		updateUI();
		changeButton();
	}
	
	private void getViews() {
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		shuffle = (ImageButton) findViewById(R.id.shuffle);
		stop = (Button) findViewById(R.id.btnStop);
		btnBack = (Button) findViewById(R.id.btnBack);
		btnPause = (Button) findViewById(R.id.btnPause);
		btnNext = (Button) findViewById(R.id.btnNext);
		btnPlay = (Button) findViewById(R.id.btnPlay);
		textNowPlaying = (TextView) findViewById(R.id.textNowPlaying);
		linearLayoutPlayer = (LinearLayout) findViewById(R.id.linearLayoutPlayer);
		textAlbumArtist = (TextView) findViewById(R.id.textAlbumArtist);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		textBufferDuration = (TextView) findViewById(R.id.textBufferDuration);
		textDuration = (TextView) findViewById(R.id.textDuration);
		//textAlbumcomposer = (TextView) findViewById(R.id.textAlbumcomposer);
		textNowPlaying.setSelected(true);
		textAlbumArtist.setSelected(true);

		//textAlbumcomposer.setSelected(true);
		seekBar = (SeekBar) findViewById(R.id.seekBar);



	}
	
	@Override
	protected void onResume() {
		super.onResume();
		boolean isServiceRunning = UtilFunctions.isServiceRunning(SongService.class.getName(), getApplicationContext());
		if (isServiceRunning) {
			updateUI();
		}
		changeButton();
	}
	
	public static void changeButton() {
		if(PlayerConstants.SONG_PAUSED){
			btnPause.setVisibility(View.GONE);
			btnPlay.setVisibility(View.VISIBLE);
		}else{
			btnPause.setVisibility(View.VISIBLE);
			btnPlay.setVisibility(View.GONE);
		}
	}

	
	private static void updateUI() {
		try{
			String songName = PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getTitle();
			String artist = PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getArtist();
			String album = PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getAlbum();
			String composer = PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getPath();
			textNowPlaying.setText(songName);
			textAlbumArtist.setText(artist + " - " + album);



		}catch(Exception e){
			e.printStackTrace();
		}
		try{
			long albumId = PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getAlbumId();
			Bitmap albumArt = UtilFunctions.getAlbumart(context, albumId);
			if(albumArt != null){
				linearLayoutPlayer.setBackgroundDrawable(new BitmapDrawable(albumArt));
			}else{
				linearLayoutPlayer.setBackgroundDrawable(new BitmapDrawable(UtilFunctions.getDefaultAlbumArt(context)));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
