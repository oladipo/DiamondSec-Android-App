package com.synkron.diamondsec.fragments;

import com.synkron.diamondsec.R;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class MyAccountFragment extends android.support.v4.app.Fragment{ 
	
	public static final String ARG_MENU_NUMBER = "";
	public static final String TAG = "MyAccountFragment";
	
	public Context context;
	
	public MyAccountFragment(){
		
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		
		View rootView = inflater.inflate(R.layout.fragment_my_account, container, false);
		
		int i = getArguments().getInt(ARG_MENU_NUMBER);
		
		String menuTitle = getResources().getStringArray(R.array.app_menu)[i];
		
		getActivity().setTitle(menuTitle);
		
		FragmentTabHost tabHost = (FragmentTabHost)rootView.findViewById(android.R.id.tabhost);
		
		try{
			tabHost.setup(getActivity(), getChildFragmentManager(), android.R.id.tabcontent);
			
			Log.i(TAG, "Adding Summary and MyStocks Tabs");
			
			tabHost.addTab(tabHost.newTabSpec("Summary").setIndicator("Summary", null),
					SummaryFragment.class, null);
			tabHost.addTab(tabHost.newTabSpec("My Stocks").setIndicator("My Stocks",null),
					MyStocksFragment.class, null);
		
		}catch(Exception Ex){
			
			Log.e(TAG, Ex.getMessage());
		}
		return tabHost;
	}
}
