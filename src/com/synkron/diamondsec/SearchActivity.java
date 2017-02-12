package com.synkron.diamondsec;

import com.synkron.diamondsec.contentproviders.StocksContentProvider;
import com.synkron.diamondsec.utils.AppConstants;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Loader;
import android.content.CursorLoader;
import android.widget.SimpleCursorAdapter;
import android.app.LoaderManager;

import android.database.Cursor;

public class SearchActivity extends ListActivity implements LoaderManager.LoaderCallbacks<Cursor>{

	private static final String QUERY_EXTRA_KEY = "QUERY_EXTRA_KEY";

	private static String TAG = "SearchActivity";
	
	//replace this adapter with a custom adapter for UI customization..
	private SimpleCursorAdapter _cursorAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//setContentView(R.layout.activity_search);
		
		//initialize the adapter by binding it to the listview..
		_cursorAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, 
				null, new String[]{StocksContentProvider.KEY_STOCK_CODE}, new int[]{android.R.id.text1}, 0);

		setListAdapter(_cursorAdapter);
		
		//initiate the cursor loader
		getLoaderManager().initLoader(0, null, this);
		
		// Get the intent, verify the action and get the query
		 handleIntent(getIntent());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	 @Override
	    protected void onNewIntent(Intent intent) {
	        super.onNewIntent(intent);
	         
	        handleIntent(intent);
	    }
	 
	    private void handleIntent(Intent intent) {
	    	
	    	Log.i(TAG, "handleIntent called");
	    	
	        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
	            String query = intent.getStringExtra(SearchManager.QUERY);
	            
	            Log.i(TAG, "search query : "+ query);
	            
	            performSearch(query);
	        }
	        
	        if(AppConstants.ACTION_VIEW_STOCK.equals(intent.getAction())){
	        	
	        	Log.i(TAG, "search suggestion selected : "+ intent.getData());
	        	//broadcast intent for stock details activity...
				
	        	Intent stockDetailIntent = new Intent();
	        	stockDetailIntent.setFlags(Intent.FLAG_EXCLUDE_STOPPED_PACKAGES);
	        	stockDetailIntent.setAction(AppConstants.ACTION_VIEW_STOCK);
	        	stockDetailIntent.setData(intent.getData());
				
				sendBroadcast(stockDetailIntent);
				
				Log.i(TAG, "Broadcast Intent Sent With Action :" + AppConstants.ACTION_VIEW_STOCK);
				
				finish();
	        }
	    }
	 
	    private void performSearch(String query) {
	        Bundle args = new Bundle();
	    	args.putString(QUERY_EXTRA_KEY, query);
	    	
	    	// Query your data set and show results
	    	Log.i(TAG,"Querying the StocksContentProvider with query string :"+query);
	    	
	    	//restart the cursor loader to execute new query
	    	getLoaderManager().restartLoader(0, args, this);
	    }

		@Override
		public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
			String query = "0";
			
			//Extract search query from bundle extra
			if(bundle != null)
				query = bundle.getString(QUERY_EXTRA_KEY);
				
				//construct new query in the form of a cursor loader
				String[] projection = {
						StocksContentProvider.KEY_ID,
						StocksContentProvider.KEY_STOCK_NAME,
						StocksContentProvider.KEY_STOCK_CODE,
						StocksContentProvider.KEY_STOCK_SECTOR,
						StocksContentProvider.KEY_STOCK_PRICE	
				};
				
				String where = StocksContentProvider.KEY_STOCK_NAME 
						+ " LIKE \"%" + query + "%\"";
				
				String[] whereArgs = null;
				
				String sortOrder = StocksContentProvider.KEY_ID +
						" COLLATE LOCALIZED ASC";
				
			return new CursorLoader(this, StocksContentProvider.CONTENT_URI, projection, where, 
					whereArgs, sortOrder);
		}

		@Override
		public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
			_cursorAdapter.swapCursor(cursor);
			
		}

		@Override
		public void onLoaderReset(Loader<Cursor> loader) {
			_cursorAdapter.swapCursor(null);
		}
}
