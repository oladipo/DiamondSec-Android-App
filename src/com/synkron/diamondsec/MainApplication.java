package com.synkron.diamondsec;

import com.synkron.diamondsec.broadcastreceivers.StockUpdateAlarmReceiver;

import android.app.Application;
import android.content.Intent;

public class MainApplication extends Application{
	
	@Override
	public void onCreate(){
	    super.onCreate();
		//fire broadcast intent to start alarm for stocksupdate service..
		Intent alarmIntent = new Intent(StockUpdateAlarmReceiver.ACTION_UPDATE_STOCKS_ALARM);
		this.sendBroadcast(alarmIntent);
	}
}
