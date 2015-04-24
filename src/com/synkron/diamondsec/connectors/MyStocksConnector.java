package com.synkron.diamondsec.connectors;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.synkron.diamondsec.Stock;
import com.synkron.diamondsec.callbacks.FragmentCallback;

public class MyStocksConnector extends InfowareConnector{
	private static final String TAG = "MyStocksConnector";
	private FragmentCallback _fragmentCallback;
	private ArrayList<Stock> mStocks;
	private Stock mStockItem;
	
	public void setFragmentCallBack(FragmentCallback fragmentCallback){
		_fragmentCallback = fragmentCallback;
	}
	
	public MyStocksConnector(Context context){
		super(context);
	}
	
	protected String doInBackground(String... args){
		Log.i(TAG, "MyStocks Connector Task Started");
		String _Result = "";
		String sb = "";
		try {
       	 if(super.isNetworkAvailable()){
	             //URL url = new URL(args[0]+args[1]+"|"+args[2]);
       		 	URL url = new URL(args[0]+"70001758"+"|"+args[2]);
	             HttpURLConnection connection = (HttpURLConnection)url.openConnection();
	             connection.setReadTimeout(10000);
	             connection.setConnectTimeout(15000);
	             connection.setRequestMethod("GET");
	             connection.setDoInput(true);
	             connection.connect();
	             
	             InputStream is = connection.getInputStream();
	             BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8") );
	             String data = null;
	             
	             while ((data = reader.readLine()) != null) {
	                 _Result += data;
	             }
	             
	             Log.d(TAG, _Result);
	             
	             try {
	 				JSONObject obj = new JSONObject(_Result);
	 				JSONArray jsonArray = obj.getJSONArray("Rows");
	 				mStocks = new ArrayList<Stock>();
	 				
	 				
	 				for(int i = 0; i < jsonArray.length(); i++){
	 					JSONArray inner  = jsonArray.getJSONArray(i);
	 					mStockItem = new Stock();
	 					
	 					mStockItem.setStockCode(inner.getJSONObject(0).getString("Value"));
	 					mStockItem.setName(inner.getJSONObject(1).getString("Value"));
	 					mStockItem.setTotalUnits(inner.getJSONObject(2).getString("Value"));
	 					mStockItem.setAverageUnitPrice(inner.getJSONObject(3).getString("Value"));
	 					mStockItem.setCost(inner.getJSONObject(4).getString("Value"));
	 					mStockItem.setFees(inner.getJSONObject(5).getString("Value"));
	 					mStockItem.setMarketValue(inner.getJSONObject(6).getString("Value"));
	 					mStockItem.setMarketQuote(inner.getJSONObject(7).getString("Value"));
	 					
	 					mStocks.add(mStockItem);
	 				}
	 			}catch (JSONException e) {
	 				// TODO Auto-generated catch block
	 				Log.e(TAG, e.getMessage());
	 			}
       	 }else{
       		 //throw new IOException("No Network Connection");
       	 }
            
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return sb;
	}
	
	protected void onPostExecute(String result) {
		
		Log.i(TAG, "MyStocksConnector Task Completed");
		
		try{
			Log.i(TAG, "FragmentCallBack Started");
			
			_fragmentCallback.OnTaskDone(mStocks);
			
		}catch(Exception Ex){
		
			Log.e(TAG, "Error Occured While Calling FragmentCallBack onTaskDone :"+Ex.getMessage());
		}
	}
}
