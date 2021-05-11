package com.example.samplegame;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.example.gameframework.gameframework.implementations.AndroidGame;
import com.example.gameframework.gameframework.implementations.Screen;
import com.example.samplegame.screens.LoadingScreen;

public class SampleGame extends AndroidGame {
	
	@Override
	public Screen getInitScreen() {
		return new LoadingScreen(this); 
	}
	
	public void notificate(){
		NotificationCompat.Builder mBuilder =
		        new NotificationCompat.Builder(this)
		        .setSmallIcon(R.drawable.icon_logo)
		        .setContentTitle("My notification")
		        .setContentText("Hello World!");
		// Creates an explicit intent for an Activity in your app
		Intent resultIntent = new Intent(this, SampleGame.class);
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		// Adds the back stack for the Intent (but not the Intent itself)
		stackBuilder.addParentStack(SampleGame.class);
		// Adds the Intent that starts the Activity to the top of the stack
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent =
		        stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(resultPendingIntent);
		NotificationManager mNotificationManager =
		    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		// mId allows you to update the notification later on.
		mNotificationManager.notify(1, mBuilder.build());
	}
	
	public class rec extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			notificate();
		}
		
	}
	
}
