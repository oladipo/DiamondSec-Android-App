package com.synkron.diamondsec.connectors;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.synkron.diamondsec.callbacks.FragmentCallback;

import android.content.Context;
import android.util.Log;


public class SummaryConnector extends InfowareConnector{
	private static final String TAG = "AccountSummaryConnector";
	private FragmentCallback _fragmentCallback;
		
	public SummaryConnector(Context context){
		super(context);
	}
	
	public void setFragmentCallback(FragmentCallback fragmentCallBack){
		_fragmentCallback = fragmentCallBack;
	}
	
	protected String doInBackground(String... args) {
		Log.i(TAG, "Account Summary Task Started");
		String _Result = "";
		String sb = "";
		try {
       	 if(super.isNetworkAvailable()){
	             URL url = new URL(args[0]+args[1]);
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
	 						
	 						sb = sb + "|"+ innerObj.getString("Value");
	 					}
	 				}
	 				sb = sb.trim();
	 				
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
	
	protected void onPostExecute(String result) {
		
		Log.i(TAG, "AccountSummaryTask Completed");
		
		try{
			Log.i(TAG, "FragmentCallBack Started");
			
			_fragmentCallback.onTaskDone(result);
			
		}catch(Exception Ex){
		
			Log.e(TAG, "Error Occured While Calling FragmentCallBack onTaskDone :"+Ex.getMessage());
		}
	}
}
