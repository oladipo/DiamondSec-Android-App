package com.synkron.diamondsec.adapters;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Locale;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.synkron.diamondsec.R;
import com.synkron.diamondsec.Stock;

public class StocksListAdapter extends ArrayAdapter<Stock>{
	private final Context context;
	private ArrayList<Stock> mStocks;
	
	public StocksListAdapter(Context context, ArrayList<Stock> myStocks) {
		super(context, R.layout.fragment_content, myStocks);
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
		
		rowView = inflater.inflate(R.layout.fragment_stocks_list, parent, false);
		rowView.setBackgroundResource(R.drawable.list_selector_normal);
		
		//3. get icon and title from row view
		TextView txtVwStockName = (TextView) rowView.findViewById(R.id.StockName);
		TextView txtVwPrice = (TextView) rowView.findViewById(R.id.Price);
		//TextView txtVwClose = (TextView) rowView.findViewById(R.id.Close);
		TextView txtVwChange = (TextView) rowView.findViewById(R.id.Change);
		
		//4. set text for text view and icon resource for imageview
		txtVwStockName.setText(mStocks.get(position).getName().toUpperCase());
		txtVwPrice.setText(" "+currencySymbol+" "+formatter.format(Double.parseDouble(mStocks.get(position).getPrice())));
		//txtVwClose.setText(" "+formatter.format(Double.parseDouble(mStocks.get(position).getLClose()+ " ")));
		txtVwChange.setText(" "+mStocks.get(position).getChange());
		
		
		return rowView;
		
	}

}
