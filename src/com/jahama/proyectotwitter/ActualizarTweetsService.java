package com.jahama.proyectotwitter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;





import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ParseException;
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
	
	private void addNewTweet(Tweet tweet) {
		Log.i(TAG, "-- A„ADIR NUEVO TWEET -- ");
		
	    ContentResolver cr = getContentResolver();

	    // Construct a where clause to make sure we don't already have this
	    // earthquake in the provider.
	    String w = TweetsProvider.KEY_DATE + " = " + tweet.getFecha().getTime();

	    // If the earthquake is new, insert it into the provider.
	    Cursor query = cr.query(TweetsProvider.CONTENT_URI, null, w, null, null);
	    
	    if (query.getCount()==0) {
	      ContentValues values = new ContentValues();

	      values.put(TweetsProvider.KEY_DATE, tweet.getFecha().getTime());
	      values.put(TweetsProvider.KEY_USER, tweet.getUsuario());   
	      values.put(TweetsProvider.KEY_USER_NAME, tweet.getNombreUsuario());
	      values.put(TweetsProvider.KEY_TEXT, tweet.getTexto());

	      cr.insert(TweetsProvider.CONTENT_URI, values);
	      Log.i(TAG, "--- INSERTED");
	    } else {
	    	Log.i(TAG, "--- EXISTS");
	    }
	    query.close();
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
                      
                      // Convertir la fecha que viene como un string a un formato de fecha
                      SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
                      Date fecha = null;
                      try{
                    	   fecha = formateador.parse("27/4/2013");
                      }catch (Exception e) {
						// TODO: handle exception
					}
                       String usuario = tweet.getString("from_user");
                	   String nombreUsuario = tweet.getString("from_user_name");
                	   String texto = tweet.getString("text");
                      
                	  
                      Tweet nuevoTweet = new Tweet(fecha, usuario,nombreUsuario,texto);

                      // Process a newly found earthquake
                      addNewTweet(nuevoTweet);
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
