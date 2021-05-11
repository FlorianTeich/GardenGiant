package com.example.samplegame;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

/**
 * AlarmReceiver class for sending custom Notifications
 * @author flori_000
 */
public class AlarmReceiver extends BroadcastReceiver{
	
	/**
	 * @see android.content.BroadcastReceiver#onReceive(android.content.Context, android.content.Intent)
	 */
	@Override
	public void onReceive(Context context, Intent intent){
		Log.i("AlarmReceiver", "Message received");
		Toast.makeText(context, intent.getStringExtra("message"), Toast.LENGTH_LONG).show();		
		NotificationCompat.Builder mBuilder =
			    new NotificationCompat.Builder(context)
			    .setSmallIcon(R.drawable.icon_logo)
			    .setContentTitle(intent.getStringExtra("title"))
			    .setContentText(intent.getStringExtra("message"));
		int mNotificationId = GameBoard.REQUEST_CODE++;
		NotificationManager mNotifyMgr = 
		        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotifyMgr.notify(mNotificationId, mBuilder.build());

	}
}