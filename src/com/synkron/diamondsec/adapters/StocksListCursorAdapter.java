package com.synkron.diamondsec.adapters;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import com.synkron.diamondsec.R;
import com.synkron.diamondsec.contentproviders.StocksContentProvider;
import com.synkron.diamondsec.utils.DecimalFormatter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class StocksListCursorAdapter extends CursorAdapter{

	LayoutInflater inflater;
	
	public StocksListCursorAdapter(Context context, Cursor c, int flags) {
		super(context, c, flags);
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		
		//Currency currency = Currency.getInstance("NGN");
		//String currencySymbol = currency.getSymbol();
		
		Double priceChange = cursor.getDouble(cursor.getColumnIndex(StocksContentProvider.KEY_STOCK_CHANGE));
		Double price = cursor.getDouble(cursor.getColumnIndex(StocksContentProvider.KEY_STOCK_PRICE));
		
		TextView txtVwStockName = (TextView) view.findViewById(R.id.StockName);
		TextView txtVwStockTicker = (TextView) view.findViewById(R.id.StockTicker);
		TextView txtVwPrice = (TextView) view.findViewById(R.id.Price);
		TextView txtVwChange = (TextView) view.findViewById(R.id.Change);
		ImageView imgVwChange = (ImageView)view.findViewById(R.id.ChangeArrow);
		
		//TextView txtVwClose = (TextView) view.findViewById(R.id.Close);
		
		String formattedPriceChange = DecimalFormatter.Format(priceChange);
		String formattedPrice = DecimalFormatter.Format(price);
		
		if(priceChange > 0.00){
			//+ve price change
			formattedPriceChange = "+"+formattedPriceChange;
			txtVwChange.setTextColor(Color.argb(255, 4, 198, 4));
			
			imgVwChange.setImageResource(R.drawable.action_green_arrow_up);
			imgVwChange.setVisibility(View.VISIBLE) ;
		}
		if(priceChange < 0.00){
			//-ve price change
			//priceChange = "-"+priceChange;
			txtVwChange.setTextColor(Color.RED);
			imgVwChange.setImageResource(R.drawable.action_red_arrow_down);
			imgVwChange.setVisibility(View.VISIBLE) ;
		}
		if(priceChange == 0.00){
			// no price change
			txtVwChange.setTextColor(Color.BLACK);
			imgVwChange.setVisibility(View.GONE) ;
		}
		
		//3. get icon and title from row view
		txtVwStockName.setText(cursor.getString(cursor.getColumnIndex(StocksContentProvider.KEY_STOCK_NAME)).toUpperCase(Locale.getDefault()));
		
		txtVwStockTicker.setText(cursor.getString(cursor.getColumnIndex(StocksContentProvider.KEY_STOCK_CODE)).toUpperCase(Locale.getDefault())+" (NSE)");
		txtVwStockTicker.setTag(cursor.getString(cursor.getColumnIndex(StocksContentProvider.KEY_ID)));
		
		txtVwPrice.setText(formattedPrice);
		txtVwChange.setText(formattedPriceChange);
		//txtVwClose.setText(" "+formatter.format(Double.parseDouble(cursor.getString(cursor.getColumnIndex(StocksContentProvider.KEY_STOCK_CLOSE)))));
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
		
		return inflater.inflate(R.layout.fragment_stocks_list,viewGroup, false);
	}

}
