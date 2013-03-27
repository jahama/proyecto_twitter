package com.jahama.proyectotwitter;



import android.app.Fragment;
import android.app.ListFragment;
import android.app.LoaderManager;
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
	      android.R.layout.simple_list_item_1, null,
	      new String[] {"aa" },
	      new int[] { android.R.id.text1 }, 0);
	    setListAdapter(adapter);

	    getLoaderManager().initLoader(0, null, this);  

	    Thread t = new Thread(new Runnable() {
	      public void run() {
	        // Actualizar timeline
	      }
	    });
	    t.start();
	  }
	  
	 

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// TODO Auto-generated method stub
		
	}
}