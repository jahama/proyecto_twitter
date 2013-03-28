package com.jahama.proyectotwitter;


import android.app.Fragment;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class MyFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {
	  
	  SimpleCursorAdapter adapter;
  
	  
	  @Override
	  public void onActivityCreated(Bundle savedInstanceState) {
	    super.onActivityCreated(savedInstanceState);

	    // Create a new Adapter and bind it to the List View
	    adapter = new SimpleCursorAdapter(getActivity(),
	      android.R.layout.simple_list_item_2, null,
	      new String[] {TweetsProvider.KEY_DATE, TweetsProvider.KEY_TEXT },
	      new int[] { android.R.id.text1, android.R.id.text2 }, 0);
	    setListAdapter(adapter);

	    getLoaderManager().initLoader(0, null, this);  

	    Thread t = new Thread(new Runnable() {
	      public void run() {
	        // Actualizar timeline
	    	  actualizarTweets() ;
	      }
	    });
	    t.start();
	  }
	  
	  
	  Handler handler = new Handler();
	  
	  public void actualizarTweets() {
	    handler.post(new Runnable() {
	      public void run() {
	        getLoaderManager().restartLoader(0, null, MyFragment.this); 
	      }
	    });
	    
	    getActivity().startService(new Intent(getActivity(),ActualizarTweetsService.class));
	  }  
	 

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		 String[] projection = new String[] {
			      TweetsProvider.KEY_ID,
			      TweetsProvider.KEY_TEXT,
			      TweetsProvider.KEY_DATE
			      
			    }; 

			    MainActivity tweetActivity = (MainActivity)getActivity();
			    /*
			    String where = TweetsProvider.KEY_MAGNITUDE + " > " + 
			                   earthquakeActivity.minimumMagnitude;
			   */
			    String where = null;
			    CursorLoader loader = new CursorLoader(getActivity(), 
			      TweetsProvider.CONTENT_URI, projection, where, null, null);
			    
			    return loader;
	}

	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
	    adapter.swapCursor(cursor);
	  }

	  public void onLoaderReset(Loader<Cursor> loader) {
	    adapter.swapCursor(null);
	  }

}