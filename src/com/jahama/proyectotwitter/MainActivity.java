package com.jahama.proyectotwitter;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.view.Menu;


@SuppressLint("NewApi")
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
		// Navegacion con pestñas
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		// Creo la pestaña de tweets
		
		Tab pestanaTweets = actionBar.newTab();
		pestanaTweets.setText("Tweets")
					.setIcon(R.drawable.ic_launcher)
					.setContentDescription("Tweets") 
					.setTabListener(
					 new TabListener<MyFragment>
					(this, R.id.fragmentContainer, MyFragment.class));
		actionBar.addTab(pestanaTweets);


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu); 
		return true;
	}
	
	/**
	  *   clase para controlar la pestañas del la barra de accion
	  */
	  @SuppressLint("NewApi")
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
	    @SuppressLint("NewApi")
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
