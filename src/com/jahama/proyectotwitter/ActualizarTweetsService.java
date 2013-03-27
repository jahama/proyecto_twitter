package com.jahama.proyectotwitter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;

public class ActualizarTweetsService extends Service {
	private static final String TAG = "ActualizarTweetsService";

	public ActualizarTweetsService() {
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO: Return the communication channel to the service.
		throw new UnsupportedOperationException("Not yet implemented");
	}
	
	public void actualizarTweets(String uri) {
		JSONObject json;
		uri = "http://search.twitter.com/search.json?q=balonmano";
		
		Log.d(TAG, " -- actualizarTweets -- ");
		try {
		 
		URL url = new URL(uri);

		  // Create a new HTTP URL connection
		  URLConnection connection = url.openConnection();
		  HttpURLConnection httpConnection = (HttpURLConnection)connection;

		  int responseCode = httpConnection.getResponseCode();
		  if (responseCode == HttpURLConnection.HTTP_OK) {
			  try {
				  //JSONObject json = new JSONObject(httpConnection.getInputStream().toString());
				  BufferedReader streamReader =
						  new BufferedReader(new InputStreamReader(httpConnection.getInputStream(), "UTF-8")); 
				  StringBuilder responseStrBuilder = new StringBuilder();

				  String inputStr;
				  while ((inputStr = streamReader.readLine()) != null)
				      responseStrBuilder.append(inputStr);
				  
				  json = new JSONObject(responseStrBuilder.toString());
				  //Log.d(TAG, " JSON descargado " + json.toString());
				  
				  JSONArray infoTweets = json.getJSONArray("results");
				  String[] textosTweets = new String[infoTweets.length()];
				  
				  for (int i = 0; i < infoTweets.length(); i++) {
                      JSONObject tweet = infoTweets.getJSONObject(i);
                      Log.i(TAG, " Tweet : " + i + " - " +  tweet.getString("created_at") + " - " +  tweet.getString("from_user"));
                      textosTweets[i] =  tweet.getString("text");
                  }
                 
			  } catch(JSONException e) {
				  Log.e("500PX", "Error al leer el fichero JSON: "+e.getMessage());
			  }
		  }
		}
		catch (MalformedURLException e) {
		  Log.d(TAG, "Malformed URL Exception.", e);
		}
		catch (IOException e) {
		  Log.i(TAG, "IO Exception."+  e.getMessage());
		}
	}
	
	@Override
	  public int onStartCommand(Intent intent, int flags, int startId) {
		  Log.d(TAG, " -- onStartCommand --");
	    // Retrieve the shared preferences
	    Context context = getApplicationContext();
	    SharedPreferences prefs = 
	      PreferenceManager.getDefaultSharedPreferences(context);

	    int updateFreq = 
	      Integer.parseInt(prefs.getString(PreferencesActivity.PREF_UPDATE_FREQ, "60"));
	    boolean autoUpdateChecked = 
	      prefs.getBoolean(PreferencesActivity.PREF_AUTO_UPDATE, false);

	    if (autoUpdateChecked) {
	      int alarmType = AlarmManager.ELAPSED_REALTIME_WAKEUP;
	      long timeToRefresh = SystemClock.elapsedRealtime() +
	                           updateFreq*60*1000;
	      alarmManager.setInexactRepeating(alarmType, timeToRefresh,
	                                       updateFreq*60*1000, alarmIntent); 
	    }
	    else
	      alarmManager.cancel(alarmIntent);

	    Thread t = new Thread(new Runnable() {
	      public void run() {
	    	  String url = null;
	    	  actualizarTweets(url); 
	      }
	    });
	    t.start();

	    return Service.START_NOT_STICKY;
	  };

	  private AlarmManager alarmManager;
	  private PendingIntent alarmIntent;

	  @Override
	  public void onCreate() {
	    super.onCreate();
	    Log.d(TAG, " -- onCreate --");
	    // Obtengo una referencia del AlarmManager
	    alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
	    // Crea el PendingIntent que sera difuncido y la accion
	    String ALARM_ACTION =
	      ActualizarAlarmReceiver.ACTION_ACTUALIZAR_TWEETS_ALARM;
	    Intent intentToFire = new Intent(ALARM_ACTION);
	    alarmIntent =
	      PendingIntent.getBroadcast(this, 0, intentToFire, 0);
	  }
}
