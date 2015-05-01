package com.synkron.diamondsec.fragments;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.os.Handler;

import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v4.app.LoaderManager;
import android.support.v4.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.database.Cursor;

import com.synkron.diamondsec.R;
import com.synkron.diamondsec.Stock;

import com.synkron.diamondsec.adapters.StocksListCursorAdapter;
import com.synkron.diamondsec.broadcastreceivers.StockUpdateAlarmReceiver;
import com.synkron.diamondsec.callbacks.FragmentCallback;

import com.synkron.diamondsec.contentproviders.StocksContentProvider;

public class StocksListFragment extends android.support.v4.app.Fragment implements FragmentCallback, 
																				LoaderManager.LoaderCallbacks<Cursor>, 
																				OnRefreshListener
{

	private static String TAG = "StocksListFragment";
	private ProgressBar _progressBar;
	private ArrayList<Stock> mStocks;
	private ListView mStocksListView;
	private SwipeRefreshLayout mRefreshLayout;
	
	//this is the adapter used to display the listview data..
	StocksListCursorAdapter _cursorAdapter;	
	
	Context _context;
	
	@SuppressWarnings("deprecation")
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		
		View rootView = inflater.inflate(R.layout.fragment_stocks_list_parent, container, false);
		_progressBar = (ProgressBar) rootView.findViewById(R.id.progressStocks);
		
		mRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);
		mRefreshLayout.setOnRefreshListener(this);
		mRefreshLayout.setColorScheme(android.R.color.holo_blue_bright, 
	            android.R.color.holo_green_light, 
	            android.R.color.holo_orange_light, 
	            android.R.color.holo_red_light);
		_context = getActivity();
		
		getAllTradedStocks();
		
		mStocksListView = (ListView) rootView.findViewById(R.id.stocks_list);
		mStocksListView.setTextFilterEnabled(true);
		
		//create an empty adapter we will use to display loaded data....
		_cursorAdapter = new StocksListCursorAdapter(_context, null, CursorAdapter.NO_SELECTION);
		
		getLoaderManager().initLoader(0, null, this);
		
		//set listview adapter to a cursor adapter...
		mStocksListView.setAdapter(_cursorAdapter);
		
		Log.i(TAG, "MyStocksFragment Inflated");
		
		return rootView;
	}
	
	private void getAllTradedStocks() {
		
	}

	@Override
	public void onTaskDone(String result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnTaskDone(Object result) {
		
	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		Log.i(TAG, "onCreateLoader running...");
		
		String[] projection = new String[]{
				StocksContentProvider.KEY_ID,
				StocksContentProvider.KEY_STOCK_CODE,
				StocksContentProvider.KEY_STOCK_NAME,
				StocksContentProvider.KEY_STOCK_SECTOR,
				StocksContentProvider.KEY_STOCK_PRICE,
				StocksContentProvider.KEY_STOCK_CHANGE,
				StocksContentProvider.KEY_STOCK_COST,
				StocksContentProvider.KEY_STOCK_FEES,
				StocksContentProvider.KEY_STOCK_CLOSE,
				StocksContentProvider.KEY_STOCK_MARKET_QUOTE,
				StocksContentProvider.KEY_STOCK_MARKET_VALUE,
				StocksContentProvider.KEY_STOCK_AVERAGE_UNIT_PRICE,
				StocksContentProvider.KEY_STOCK_TOTAL_UNITS,
				StocksContentProvider.KEY_STOCK_VOLUME
		};
		
		CursorLoader loader = new CursorLoader(_context, StocksContentProvider.CONTENT_URI, projection,null,null, null);
		
		Log.i(TAG, "onCreateLoader complete");
		
		return loader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		Log.i(TAG, "onLoadFinished called..");
		_cursorAdapter.swapCursor(cursor);
		_progressBar.setVisibility(ProgressBar.GONE);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		Log.i(TAG, "onLoaderReset called..");
		_cursorAdapter.swapCursor(null);
		
	}

	@Override
	public void onRefresh() {
		//fire stockUpdate Intent
	    Intent alarmIntent = new Intent(StockUpdateAlarmReceiver.ACTION_UPDATE_STOCKS_ALARM);
	    _context.sendBroadcast(alarmIntent);
	    
	    new Handler().postDelayed(new Runnable() {
	        @Override public void run() {
	    	    mRefreshLayout.setRefreshing(false);
	        }
	    }, 5000);
	}

}
