package com.synkron.diamondsec.broadcastreceivers;

import com.synkron.diamondsec.services.StocksUpdateService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class StockUpdateAlarmReceiver extends BroadcastReceiver{
	private static String TAG = "STOCK_UPDATE_ALARM_RECEIVER";
	public static final String ACTION_UPDATE_STOCKS_ALARM = 
			"com.synkron.diamondsec.ACTION_UPDATE_STOCKS_ALARM";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Intent startIntent = new Intent(context, StocksUpdateService.class);
		context.startService(startIntent);
	}

}
