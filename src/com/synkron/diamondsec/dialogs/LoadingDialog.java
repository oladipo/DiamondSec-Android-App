package com.synkron.diamondsec.dialogs;

import com.synkron.diamondsec.R;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class LoadingDialog extends DialogFragment{
	
	
	
	 public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		 View v = inflater.inflate(R.layout.fragment_loading_dialog,container, false);
		 
		 return v;
	}
}
