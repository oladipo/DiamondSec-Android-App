package com.synkron.diamondsec.services;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.synkron.diamondsec.R;
import com.synkron.diamondsec.Stock;
import com.synkron.diamondsec.broadcastreceivers.StockUpdateAlarmReceiver;
import com.synkron.diamondsec.connectors.InfowareConnector;
import com.synkron.diamondsec.utils.StocksDBInitializer;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

//services are run on the main GUI thread.
//to ensure that while runnning, it doesnt block the maint thread,
//we will call the encapsulated operation asynchronously and allow it to run
//on a different thread.
public class StocksUpdateService extends IntentService{
	
	private static String TAG = "StocksUpdateService";
	private AlarmManager alarmManager;
	private PendingIntent alarmIntent;
	private ArrayList<Stock> mStocks;
	private Stock mStockItem;
	private Context context;
	
	public StocksUpdateService(String name) {
		super(name);
	}
	
	public StocksUpdateService(){
		super(TAG);
	}
	
	public void onCreate(){
		Log.i(TAG,"onCreate event triggered");
		
		super.onCreate();
		alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
		
		String ALARM_ACTION = StockUpdateAlarmReceiver.ACTION_UPDATE_STOCKS_ALARM;
		Intent intentToFire = new Intent(ALARM_ACTION);
		alarmIntent = PendingIntent.getBroadcast(this, 0, intentToFire, 0);
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		Log.i(TAG,"onBind event triggered");
		return null;
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Log.i(TAG,"onHandleIntent event triggered for "+ TAG);
		context = getApplicationContext();
		
		//flags: (1) stocks updated (2) last update
		//if last update is null => app is running for the first time...=> startService() to update stocks..
		//last update is the last time stocks were updated/last time alarm was fired...
		int alarmType = AlarmManager.ELAPSED_REALTIME_WAKEUP;
		//test time interval to see if alarm is fire and service is started... 
		//this should be replaced with the alarm interval constant of a day.
		long triggerAtMillis = SystemClock.elapsedRealtime() + AlarmManager.INTERVAL_DAY;
		long intervalMillis = AlarmManager.INTERVAL_DAY;
		
		//long triggerAtMillis = AlarmManager.INTERVAL_DAY;
		Log.i(TAG,"Stock Update Alarm set to trigger at "+ triggerAtMillis);
		
		alarmManager.setInexactRepeating(alarmType, triggerAtMillis, intervalMillis, alarmIntent);
		
		refreshStocks();
	}

	private void refreshStocks() {
		Log.i(TAG, "Stocks Update Service is running...");
		String _Result = "";
		String sb = "";
		InfowareConnector _connector = new InfowareConnector(context);
		
       	 if(_connector.isNetworkAvailable()){
     		try {
	             URL url = new URL(InfowareConnector.API_FULL_PRICE_LIST_URL);
	             
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
	 					mStockItem.setSector(inner.getJSONObject(2).getString("Value"));
	 					mStockItem.setPrice(inner.getJSONObject(3).getString("Value"));
	 					mStockItem.setLClose(inner.getJSONObject(4).getString("Value"));
	 					mStockItem.setChange(inner.getJSONObject(5).getString("Value"));
	 					mStockItem.setVolume(inner.getJSONObject(6).getString("Value"));
	 					
	 					mStocks.add(mStockItem);
	 				}
	             }catch (JSONException e) {
	 				
	 				Log.e(TAG, e.getMessage());
	 			}
	             
	             updateContentProvider(mStocks); 
	             
            }catch (Exception e){
            	Toast.makeText(context, TAG +" Error :"+e.getMessage() , Toast.LENGTH_SHORT).show();
            	Log.e(TAG, e.getMessage());
            }
       	 }else{
       		Toast.makeText(context, TAG +" Error : Data Connection Failed", Toast.LENGTH_SHORT).show();
       		Log.e(TAG, "Data Connection Failed");
       	 }
	}
	
	private void updateContentProvider(ArrayList<Stock> Stocks){
		
		StocksDBInitializer dbInitializer = new StocksDBInitializer(getApplicationContext());
		dbInitializer.execute(Stocks);
	}
}
