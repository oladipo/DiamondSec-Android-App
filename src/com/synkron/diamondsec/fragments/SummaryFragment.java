package com.synkron.diamondsec.fragments;

import com.synkron.diamondsec.R;
import com.synkron.diamondsec.callbacks.FragmentCallback;
import com.synkron.diamondsec.connectors.SummaryConnector;
import com.synkron.diamondsec.utils.SplitString;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class SummaryFragment extends android.support.v4.app.Fragment implements FragmentCallback{

	private static String TAG = "SummaryFragment";
	private String[] summaryInfo;
	private View _theView;
	private ProgressBar _progressBar;
	
	Context _context;
	
	public SummaryFragment(){
		
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		_context = getActivity();
		
		View rootView = inflater.inflate(R.layout.fragment_summary, container, false);
		_progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);

		_theView = rootView;
		
		Log.i(TAG, "SummaryFragment Inflated");
		
		loadAccountSummary();
		
		return rootView;
	}
	
	public void loadAccountSummary(){
		
		_progressBar.setVisibility(View.VISIBLE);
		String strCustomerID = "";
		
		SummaryConnector _connectorTask = new SummaryConnector(_context);
		_connectorTask.setFragmentCallback(this);
		
		if(_connectorTask.isNetworkAvailable()){
			
			try{
				//get customer id from shared preferences..
				SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.preference_file_key),
						Context.MODE_PRIVATE);
				
				strCustomerID = sharedPref.getString(getString(R.string.customer_id), "");
				
			}catch(Exception Ex){
				
				Log.e(TAG, "Exception Occured Retrieving Shared Preferences : "+Ex.getMessage());
				
			}
			_connectorTask.execute(SummaryConnector.API_CUSTOMER_ACCOUNT_SUMMARY_URL, strCustomerID);
			
		}else{
			Toast.makeText(getActivity(), "network connection lost", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onTaskDone(String result) {

		summaryInfo = SplitString.split(result, "|");
		Double _total, _fundsAvailable = 0.00;
		
		try{
			
			_fundsAvailable = Double.parseDouble(summaryInfo[7]) + Double.parseDouble(summaryInfo[8])+ 
					Double.parseDouble(summaryInfo[10]) + Double.parseDouble(summaryInfo[11]);
			
			_total = Double.parseDouble(summaryInfo[8]) + Double.parseDouble(summaryInfo[9])+
					Double.parseDouble(summaryInfo[11]) + Double.parseDouble(summaryInfo[12]);
					
			TextView txtVwCustomerId = (TextView)getView().findViewById(R.id.txtVwCustomerID);
			txtVwCustomerId.setText(summaryInfo[1]);
			
			TextView txtVwCustomerName = (TextView)getView().findViewById(R.id.txtVwCustomerName);
			txtVwCustomerName.setText(summaryInfo[2]);
			
			TextView txtVwCashBalance = (TextView)getView().findViewById(R.id.txtVwCashBalanceValue);
			txtVwCashBalance.setText(summaryInfo[8]);
			
			TextView txtVwRealEstate = (TextView)getView().findViewById(R.id.txtVwRealEstateValue);
			txtVwRealEstate.setText("0.00");
			
			TextView txtVwEquities = (TextView)getView().findViewById(R.id.txtVwquitiesValue);
			txtVwEquities.setText(summaryInfo[9]);
			
			TextView txtVwFundsAvailable = (TextView)getView().findViewById(R.id.txtVwFundsAvailableValue);
			txtVwFundsAvailable.setText(_fundsAvailable.toString());
			
			TextView txtVwMarginAllowed = (TextView)getView().findViewById(R.id.txtVwMarginAllowedValue);
			txtVwMarginAllowed.setText(summaryInfo[11]);
		
			TextView txtVwFixedIncome = (TextView)getView().findViewById(R.id.txtVwFixedIncomeValue);
			txtVwFixedIncome.setText(summaryInfo[12]);
			
			TextView txtTotal = (TextView)getView().findViewById(R.id.txtVwTotalValue);
			txtTotal.setText(_total.toString());
			
			//set visibility of the fragment to visible..
			View myView = _theView.findViewById(R.id.contentSection);
			
			myView.setVisibility(View.VISIBLE);
			_progressBar.setVisibility(View.INVISIBLE);
			
		}catch(Exception Ex){
			Log.e(TAG, "Exception Occured while setting values on views :"+ Ex.getMessage());
		}
	}

	@Override
	public void OnTaskDone(Object result) {
		// TODO Auto-generated method stub
		
	}
}
