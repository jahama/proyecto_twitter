package com.jahama.proyectotwitter;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ActualizarAlarmReceiver extends BroadcastReceiver {
	

	public static final String ACTION_ACTUALIZAR_TWEETS_ALARM = 
		      "com.jahama.proyectotwitter.ACTION_ACTUALIZAR_TWEETS_ALARM";

		  @Override
		  public void onReceive(Context context, Intent intent) {
		    Intent startIntent = new Intent(context, ActualizarTweetsService.class);
		    context.startService(startIntent);
		  }
}
