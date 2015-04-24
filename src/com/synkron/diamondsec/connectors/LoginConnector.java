package com.synkron.diamondsec.connectors;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.synkron.diamondsec.RootActivity;
import com.synkron.diamondsec.broadcastreceivers.StockUpdateAlarmReceiver;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

public class LoginConnector extends InfowareConnector{
	private static final String TAG = "LoginConnector";
	ProgressDialog _loadingDialog;
	private String CustomerID = "";
	
	public LoginConnector(Context context){
		super(context);
		//_context = context;
	}
	
	protected void onPreExecute(){
		  super.onPreExecute();
          // Showing progress dialog
		  
		  _loadingDialog = new ProgressDialog(_context);
		  _loadingDialog.setMax(100);
		  _loadingDialog.setProgress(1);
		  _loadingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		  _loadingDialog.setTitle("Connecting...");
		  _loadingDialog.show();
	}
	
	@Override
	protected String doInBackground(String... args) {
		CustomerID = args[1];
		
		Log.i(TAG, "Login Task Started");
		String _Result = "";
		String sb = "";
		
         try {
        	 if(super.isNetworkAvailable()){
	             URL url = new URL(args[0]+args[1]+"|"+args[2]);
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
	 				
	 				for(int i = 0; i < jsonArray.length(); i++){
	 					//add a field to the screen..
	 					JSONArray inner  = jsonArray.getJSONArray(i);
	 					for(int j = 0; j < inner.length() ; j++){
	 						JSONObject innerObj = inner.getJSONObject(j);
	 						
	 						System.out.println(innerObj.getString("Value"));
	 						
	 						sb = sb + " "+ innerObj.getString("Value");
	 					}
	 					//publishProgress(String.valueOf(i));
	 				}
	 				sb = sb.trim();
	 				sb = sb.toLowerCase(Locale.US);
	 				
	 				if(sb.equals("true")){
	 					//Save Customer Id to Persistent Store Instance..
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
	
    protected void onProgressUpdate(String... progress) {
        // Set progress percentage
        //_loadingDialog.setProgress(Integer.parseInt(progress[0]));
    }
    
    @Override
    protected void onPostExecute(String result) {
    	_loadingDialog.dismiss();
    	
    	Log.i(TAG, "Login Task Completed");
    	
    	if(!result.equals("")){
	    	if(result.equals("true")){
	    		
	    		try{
	    		//add customer ID to Shared Preferences...
	    		SharedPreferences sharedPref = _context.getSharedPreferences("User Profile", 
	    				Context.MODE_PRIVATE);
	    		
	    		SharedPreferences.Editor editor = sharedPref.edit();
	    		editor.putString("Customer ID", CustomerID);
	    		editor.commit();
	    		}catch(Exception Ex){
	    			
	    			Log.e(TAG, "Exception Occured Setting Shared Preferences: "+ Ex.getMessage());
	    		}
	    		Intent intent = new Intent(_context, RootActivity.class);
	    		_context.startActivity(intent);
	    		
	    	}
	    	else if(result.equals("false")){
	    		Toast.makeText((Context) _context, "login failed [customer id or password incorrect]", Toast.LENGTH_SHORT).show();
	    	}
    	}else{
    		Toast.makeText((Context) _context, "empty server response [do you have a valid data connection?]", Toast.LENGTH_SHORT).show();
    	}
    }
}
