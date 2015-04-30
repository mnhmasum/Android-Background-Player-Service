package com.masum.backgroundplayservice;

import java.io.IOException;
import java.lang.ref.WeakReference;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class PlayerInService extends Service implements OnClickListener {

	private WeakReference<Button> btnPlay;
	private WeakReference<Button> btnStop;
	
	public static MediaPlayer mp;

	@Override
	public void onCreate() {
		mp = new MediaPlayer();
		mp.reset();
		mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
		super.onCreate();
	}

	// --------------onStartCommand----------------------//
	@SuppressWarnings("deprecation")
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		initUI();
		super.onStart(intent, startId);
		return START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	/**
	 * Init UI
	 */
	private void initUI() {
		btnPlay = new WeakReference<Button>(MainActivity.btnPlay);
		btnStop = new WeakReference<Button>(MainActivity.btnStop);
		btnPlay.get().setOnClickListener(this);
		btnStop.get().setOnClickListener(this);

	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnPlay:
			playSong();
			break;
		case R.id.btnStop:
			mp.stop();
			break;

		}
	}

	@Override
	public void onDestroy() {

	}

	// Play song
	public void playSong() {
		initNotification("Playing(Amar sonar bangla)...");

		try {
			mp.reset();
			Uri myUri = Uri.parse("android.resource://" + this.getPackageName() + "/" + R.raw.bangla);
			mp.setDataSource(this, myUri);
			mp.prepareAsync();
			mp.setOnPreparedListener(new OnPreparedListener() {
				public void onPrepared(MediaPlayer mp) {
					try {
						mp.start();
					} catch (Exception e) {
						Log.i("ONPREP_XCEPTION", "" + e.getMessage());
					}
				}
			});

		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// ////////////////////////////////////////////////////////////////////////////
	// ////////////////////////// Notification /////////////////////
	// ////////////////////////////////////////////////////////////////////////////

	// Set up the notification ID
	public static final int NOTIFICATION_ID = 1;
	@SuppressWarnings("unused")
	private NotificationManager mNotificationManager;

	// Create Notification
	@SuppressWarnings("deprecation")
	private void initNotification(String songTitle) {
		try {
			String ns = Context.NOTIFICATION_SERVICE;
			mNotificationManager = (NotificationManager) getSystemService(ns);
			int icon = R.drawable.ic_launcher;
			CharSequence tickerText = "Playing your song";
			long when = System.currentTimeMillis();

			Notification notification = new Notification(icon, tickerText, when);
			notification.flags = Notification.FLAG_ONGOING_EVENT;
			Context context = getApplicationContext();
			CharSequence songName = "" + songTitle;

			Intent notificationIntent = new Intent(this, MainActivity.class);
			PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
			notification.setLatestEventInfo(context, songName, null, contentIntent);
			// mNotificationManager.notify(NOTIFICATION_ID, notification);
			startForeground(NOTIFICATION_ID, notification);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
