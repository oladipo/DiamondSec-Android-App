package com.synkron.diamondsec.adapters;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Locale;

import com.synkron.diamondsec.R;
import com.synkron.diamondsec.Stock;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomerStocksAdapter extends ArrayAdapter<Stock>{
	private final Context context;
	private ArrayList<Stock> mStocks;
	
	public CustomerStocksAdapter(Context context, ArrayList<Stock> myStocks) {
		super(context, R.layout.fragment_mystocks, myStocks);
		this.mStocks = myStocks;
		this.context = context;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.getDefault());
		formatter.setMaximumFractionDigits(2);
		formatter.setMinimumFractionDigits(2);
		
		 Currency currency = Currency.getInstance("NGN");
		 String currencySymbol = currency.getSymbol();
		 
		//formatter.format(12.3456); 
		
		//1. Create inflater
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		//2. Get RowView from inflater
		View rowView = null;
		
		rowView = inflater.inflate(R.layout.fragment_mystocks, parent, false);
		rowView.setBackgroundResource(R.drawable.list_selector_normal);
		
		//3. get icon and title from row view
		TextView txtVwStockName = (TextView) rowView.findViewById(R.id.StockName);
		TextView txtVwMarketQuote= (TextView) rowView.findViewById(R.id.MarketQuote);
		TextView txtVwTotalUnits = (TextView) rowView.findViewById(R.id.TotalUnits);
		
		//4. set text for text view and icon resource for imageview
		txtVwStockName.setText(mStocks.get(position).getName().toUpperCase());
		txtVwMarketQuote.setText(currencySymbol+" "+formatter.format(Double.parseDouble(mStocks.get(position).getMarketQuote())));
		txtVwTotalUnits.setText(formatter.format(Double.parseDouble(mStocks.get(position).getTotalUnits())));
		
		return rowView;
		
	}
	
}
