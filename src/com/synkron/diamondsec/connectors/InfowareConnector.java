package com.synkron.diamondsec.connectors;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.widget.Toast;

public class InfowareConnector extends AsyncTask<String, String, String>{
	
	public static Context _context;
	//BEGIN API ENDPOINTS
	public static final String API_LOGIN_URL = "http://www.infowarelimited.com/svcs/svcs01/IWdataSvc.svc/json/1101/1325/1000010/";
	public static final String API_TRADEABLE_STOCKS_URL = "http://www.infowarelimited.com/svcs/svcs01/IWDataSvc.svc/json/1101/1325/1000011";
	public static final String API_CUSTOMER_ACCOUNT_SUMMARY_URL = "http://www.infowarelimited.com/svcs/svcs01/IWDataSvc.svc/json/4/1101/1325/1000007/";
	public static final String API_CUSTOMER_PORTFOLIO_HOLDINGS_URL = "http://www.infowarelimited.com/svcs/svcs01/IWDataSvc.svc/json/1101/1325/1000002/";
	public static final String API_FULL_PRICE_LIST_URL = "http://www.infowarelimited.com/svcs/svcs01/IWDataSvc.svc/json/1101/1325/1000005";
	public static final String API_MARKET_DATA_FOR_STOCK_URL = "http://www.infowarelimited.com/svcs/svcs01/IWDataSvc.svc/json/1101/1325/1000013/";
	public static final String API_GET_CUSTOMER_CSCS_NUMBERS_URL = "http://www.infowarelimited.com/svcs/svcs01/IWDataSvc.svc/json/1101/1325/1000012/";
	public static final String API_TRADE_REQUEST_URL = "http://www.infowarelimited.com/svcs/svcs01/IWDataSvc.svc/json/4/1101/1325/1000008/";
	public static final String API_PLACE_TRADE_ORDER_URL = "http://www.infowarelimited.com/svcs/svcs01/IWDataSvc.svc/json/4/1101/1325/1000009/";
	public static final String API_CUSTOMER_STATEMENT_URL = "http://www.infowarelimited.com/svcs/svcs01/IWDataSvc.svc/json/1101/1325/1000001/";
	public static final String API_CUSTOMER_OPEN_ORDERS_URL = "http://www.infowarelimited.com/svcs/svcs01/IWDataSvc.svc/json/1101/1325/1000014/";
	public static final String API_CUSTOMER_TRANSACTION_HISTORY_URL = "http://www.infowarelimited.com/svcs/svcs01/IWDataSvc.svc/json/1101/1325/1000015/";
	//END API ENDPOINTS
	
	//BEGIN TRADE ACTION DEFINITION
	public static final int API_TRADE_ACTION_BUY = 0;
	public static final int API_TRADE_ACTION_SELL = 1;
	//END TRADE ACTION DEFINITION
	
	
	//BEGIN ORDER TYPES
	public static final int API_ORDER_TYPE_MARKET = 49;
	public static final int API_ORDER_TYPE_LIMIT = 50;
	public static final int API_ORDER_TYPE_STOP = 51;
	public static final int API_ORDER_TYPE_STOP_LIMIT = 52;
	//END ORDER TYPES
	
	//BEGIN TIME IN FORCE
	public static final int API_TIME_IN_FORCE_DAY = 0;
	public static final int API_TIME_IN_FORCE_GOODTILLCANCELLED = 1;
	public static final int API_TIME_IN_FORCE_IMMEDIATEORCANCEL = 3;
	public static final int API_TIME_IN_FORCE_FILLORKILL = 4;
	public static final int API_TIME_IN_FORCE_GOODTILLDATE = 6;
	//END TIME IN FORCE
	
	public InfowareConnector(Context context){
		_context = context;
	}
	public  boolean isNetworkAvailable(){
		boolean connected = false;
		
		ConnectivityManager check = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
	      if (check != null) 
	      {
	         NetworkInfo[] info = check.getAllNetworkInfo();
	         if (info != null) 
	            for (int i = 0; i <info.length; i++) 
	            if (info[i].getState() == NetworkInfo.State.CONNECTED)
	            {
	               //Toast.makeText((Context) context, "Internet is connected",Toast.LENGTH_SHORT).show();
	            	connected =  true;
	            }
	         
	      }
	      else{
	         Toast.makeText((Context) _context, "not connected to internet", Toast.LENGTH_SHORT).show();
	         connected = false;
	     }
		return connected;
	}

	@Override
	protected String doInBackground(String... arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
    //
    protected void onProgressUpdate(String... progress) {
        // Set progress percentage
        //prgDialog.setProgress(Integer.parseInt(progress[0]));
    }

    @Override
    protected void onPostExecute(String file_url) {

    }
}
