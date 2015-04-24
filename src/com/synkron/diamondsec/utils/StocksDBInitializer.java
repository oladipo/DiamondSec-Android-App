package com.synkron.diamondsec.utils;

import java.util.ArrayList;
import com.synkron.diamondsec.Stock;
import com.synkron.diamondsec.contentproviders.StocksContentProvider;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class StocksDBInitializer extends AsyncTask<ArrayList<Stock>, String, String>{
	public static String TAG = "StocksDBInitializer AsyncTask";
	
	ContentResolver contentResolver;
	Context _context;
	
	public StocksDBInitializer(Context context){
		_context = context;
		
		Log.i(TAG,"initializing dbinitializer");
	}
	
	@Override
	protected String doInBackground(ArrayList<Stock>... params) {
		Log.i(TAG, "doInBackground Executing");
		
		contentResolver = _context.getContentResolver();
		
		int deleted = contentResolver.delete(StocksContentProvider.CONTENT_URI,null , null);
		Log.i(TAG, deleted+ "rows deleted..");
		
		//check that stocks array is not empty
		if(!params[0].isEmpty()){
			for(Stock item : params[0]){
				ContentValues values = new ContentValues();
				values.put(StocksContentProvider.KEY_STOCK_CODE, item.getStockCode());
				values.put(StocksContentProvider.KEY_STOCK_NAME, item.getName());
				values.put(StocksContentProvider.KEY_STOCK_SECTOR, item.getSector());
				values.put(StocksContentProvider.KEY_STOCK_PRICE, item.getPrice());
				values.put(StocksContentProvider.KEY_STOCK_CLOSE, item.getLClose());
				values.put(StocksContentProvider.KEY_STOCK_CHANGE, item.getChange());
				values.put(StocksContentProvider.KEY_STOCK_VOLUME, item.getVolume());
				
				//this operation should be happen as a transaction...
				contentResolver.insert(StocksContentProvider.CONTENT_URI, values);
			}
		}
		return null;
	}
}
