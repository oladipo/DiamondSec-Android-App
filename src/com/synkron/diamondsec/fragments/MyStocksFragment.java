package com.synkron.diamondsec.fragments;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.synkron.diamondsec.R;
import com.synkron.diamondsec.Stock;
import com.synkron.diamondsec.adapters.CustomerStocksAdapter;
import com.synkron.diamondsec.callbacks.FragmentCallback;
import com.synkron.diamondsec.connectors.InfowareConnector;
import com.synkron.diamondsec.connectors.MyStocksConnector;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MyStocksFragment extends android.support.v4.app.Fragment implements FragmentCallback{
	private static String TAG = "MyStocksFragment";
	private View _theView;
	private ProgressBar _progressBar;
	private ArrayList<Stock> mStocks;
	private ListView mStocksListView;
	Context _context;
	
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View rootView = inflater.inflate(R.layout.fragment_mystocks_parent, container, false);
		_progressBar = (ProgressBar) rootView.findViewById(R.id.progressStocks);
		
		_context = getActivity();
		
		_theView = rootView;
		
		loadCustomerStocks();
		
		mStocksListView = (ListView) rootView.findViewById(R.id.customer_stocks_list);
		
		Log.i(TAG, "MyStocksFragment Inflated");
		
		return rootView;
	}

	private void loadCustomerStocks() {
		String strCustomerID = "";
		//_progressBar.setVisibility(View.VISIBLE);
		MyStocksConnector _connectorTask = new MyStocksConnector(_context);
		_connectorTask.setFragmentCallBack(this);
		
		if(_connectorTask.isNetworkAvailable()){
			
			try{
				//get customer id from shared preferences..
				SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.preference_file_key),
						Context.MODE_PRIVATE);
				
				strCustomerID = sharedPref.getString(getString(R.string.customer_id), "");
				
			}catch(Exception Ex){
				
				Log.e(TAG, "Exception Occured Retrieving Shared Preferences : "+Ex.getMessage());
				
			}
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
			Date date = new Date(System.currentTimeMillis());
			String strValueDate =  dateFormat.format(date);
			
			_connectorTask.execute(MyStocksConnector.API_CUSTOMER_PORTFOLIO_HOLDINGS_URL, strCustomerID, strValueDate);
			
		}else{
			Toast.makeText(getActivity(), "network connection lost", Toast.LENGTH_SHORT).show();
		}
		
	}

	@Override
	public void onTaskDone(String result) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void OnTaskDone(Object result) {
		try{
			Log.i(TAG, "MyStocksFragment OnTaskDone Called..");
			
			mStocks = (ArrayList<Stock>) result;
			mStocksListView.setAdapter(new CustomerStocksAdapter(_context, mStocks));
			_progressBar.setVisibility(View.INVISIBLE);
			
		}catch(Exception Ex){
			Log.e(TAG ,Ex.getMessage());
		}
	}
	
}
