package com.synkron.diamondsec.adapters;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

import com.synkron.diamondsec.R;
import com.synkron.diamondsec.contentproviders.StocksContentProvider;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class StocksListCursorAdapter extends CursorAdapter{

	LayoutInflater inflater;
	
	public StocksListCursorAdapter(Context context, Cursor c, int flags) {
		super(context, c, flags);
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		
		DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.getDefault());
		formatter.setMaximumFractionDigits(2);
		formatter.setMinimumFractionDigits(2);
		
		 Currency currency = Currency.getInstance("NGN");
		 String currencySymbol = currency.getSymbol();
		//3. get icon and title from row view
		TextView txtVwStockName = (TextView) view.findViewById(R.id.StockName);
		TextView txtVwPrice = (TextView) view.findViewById(R.id.Price);
		TextView txtVwClose = (TextView) view.findViewById(R.id.Close);
		TextView txtVwChange = (TextView) view.findViewById(R.id.Change);
		
		txtVwStockName.setText(cursor.getString(cursor.getColumnIndex(StocksContentProvider.KEY_STOCK_NAME)).toUpperCase());
		txtVwPrice.setText(" "+currencySymbol+" "+cursor.getString(cursor.getColumnIndex(StocksContentProvider.KEY_STOCK_PRICE)).toUpperCase());
		txtVwClose.setText(" "+formatter.format(Double.parseDouble(cursor.getString(cursor.getColumnIndex(StocksContentProvider.KEY_STOCK_CLOSE)))));
		txtVwChange.setText(" "+cursor.getString(cursor.getColumnIndex(StocksContentProvider.KEY_STOCK_CHANGE)));
		
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
		
		return inflater.inflate(R.layout.fragment_stocks_list,viewGroup, false);
	}

}
