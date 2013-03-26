package com.jahama.proyectotwitter;


import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;



public class MainActivity extends Activity {

	protected static final String TAG = "MAIN ACTIVITY";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Action Bar. Configuración para pestañas de la barra de acción		
		ActionBar actionBar = getActionBar();
		actionBar.setTitle("CapaTwitter");
		actionBar.setSubtitle("Proyecto: Android");
		// Navegacion con pestanas
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
				
		/*
		 * Pestañas de la barra de accion
		 */
		Tab tabTweets = actionBar.newTab();
		tabTweets.setText("Tweets")
					.setContentDescription("Tweets") 
					.setTabListener(new MyTabListener<MyFragment>(this, "tweets", MyFragment.class));
		actionBar.addTab(tabTweets);

		Tab tabEventos = actionBar.newTab();
		tabEventos.setText("Eventos")
					.setContentDescription("Eventos") 
						.setTabListener(new MyTabListener<EventosFragment>(this, "eventos",	EventosFragment.class));
		actionBar.addTab(tabEventos);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu); 
		return true;
	}
	
	/**
	  *   Clase para controlar la pestanas del la barra de accion
	  *   Escuchador para el cambio de pestañas
	  */
	 
	
public static class MyTabListener<T extends Fragment> implements  TabListener {
	private Fragment mFragment;
	private final Activity mActivity;
	private final String mTag;
	private final Class<T> mClass;


	/**
	 * Constructor used each time a new tab is created.
	 * 
	 * @param activity
	 *            The host Activity, used to instantiate the fragment
	 * @param tag
	 *            The identifier tag for the fragment
	 * @param clz
	 *            The fragment's Class, used to instantiate the fragment
	 */

	public MyTabListener(Activity activity, String tag, Class<T> clz) {
		mActivity = activity;
		mTag = tag;
		mClass = clz;
	}

	/* The following are each of the ActionBar.TabListener callbacks */

	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		Log.i(TAG," -- onTabSelected -- ");
		// Check if the fragment is already initialized
		if (mFragment == null) {
			// If not, instantiate and add it to the activity
			mFragment = Fragment.instantiate(mActivity, mClass.getName());
			ft.add(android.R.id.content, mFragment, mTag);
		} else {
			// If it exists, simply attach it in order to show it
			//ft.setCustomAnimations(android.R.animator.fade_in,R.animator.animationtest);
			Log.d(TAG," -- onTabSelected -- " + mFragment.getTag());
			ft.attach(mFragment);
		}
	}

	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		Log.i(TAG," -- onTabUnselected -- ");
		if (mFragment != null) {
			//ft.setCustomAnimations(android.R.animator.fade_in, R.animator.test);
			ft.detach(mFragment);
		}
	}

	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		Log.i(TAG," -- onTabReselected -- ");
	}
}
}
