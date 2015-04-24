package com.synkron.diamondsec.fragments;

import com.synkron.diamondsec.R;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

@SuppressLint("NewApi") 
public class ContentFragment  extends android.support.v4.app.Fragment{
	
	public static final String ARG_MENU_NUMBER = "menu_number";
	
	public ContentFragment(){
		
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View rootView = inflater.inflate(R.layout.fragment_content, container, false);
		int i = getArguments().getInt(ARG_MENU_NUMBER);
		
		String menuTitle = getResources().getStringArray(R.array.app_menu)[i];
		
		getActivity().setTitle(menuTitle);
		
		return rootView;
	}
}
