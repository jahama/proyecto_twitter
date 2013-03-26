package com.jahama.proyectotwitter;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.view.Menu;



public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		/*
		 * Action Bar
		 */
		ActionBar actionBar = getActionBar();
		actionBar.setTitle("CapaTwitter");
		actionBar.setSubtitle("Proyecto: Android");
		// Navegacion con pestanas
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		// Create a tab listener that is called when the user changes tabs.
		/*
		 * Listener que seran llamados cuando se el usuario cambie de pestañas
		 */
	    ActionBar.TabListener tabListener = new ActionBar.TabListener() {
	        public void onTabSelected(ActionBar.Tab tab,
	                FragmentTransaction ft) { }

	        public void onTabUnselected(ActionBar.Tab tab,
	                FragmentTransaction ft) { }

	        public void onTabReselected(ActionBar.Tab tab,
	                FragmentTransaction ft) { }
	    };
		
		/*
		 * Pestañas de la barra de accion
		 */
		Tab tabTweets = actionBar.newTab();
		tabTweets.setText("Tweets")
					.setContentDescription("Tweets") 
					.setTabListener(tabListener);
		actionBar.addTab(tabTweets);

		Tab tabEventos = actionBar.newTab();
		tabEventos.setText("Eventos")
					.setContentDescription("Eventos") 
					.setTabListener(tabListener);
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
	 
	
	public static class TabListener<T extends Fragment> 
	    implements ActionBar.TabListener {
	    
	    private MyFragment fragment;
	    private Activity activity;
	    private Class<T> fragmentClass;
	    private int fragmentContainer;

	    public TabListener(Activity activity, int fragmentContainer, 
	                       Class<T> fragmentClass) {

	      this.activity = activity;
	      this.fragmentContainer = fragmentContainer;
	      this.fragmentClass = fragmentClass;
	    }

	    // Called when a new tab has been selected
	   
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
	      if (fragment == null) {
	        String fragmentName = fragmentClass.getName();
	        fragment =
	          (MyFragment)Fragment.instantiate(activity, fragmentName);
	        ft.add(fragmentContainer, fragment, null);
	        fragment.setFragmentText(tab.getText());
	      } else {
	        ft.attach(fragment);
	      }
	    }

	    // Called on the currently selected tab when a different tag is
	    // selected. 
	    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	      if (fragment != null) {
	        ft.detach(fragment);
	      }
	    } 

	    // Called when the selected tab is selected.
	    public void onTabReselected(Tab tab, FragmentTransaction ft) {
	      // TODO React to a selected tab being selected again.
	    }
	  }

}
