package com.synkron.diamondsec.fragments;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.synkron.diamondsec.R;
import com.synkron.diamondsec.contentproviders.StocksContentProvider;
import com.synkron.diamondsec.utils.DecimalFormatter;

//this fragment displays the details of a stock retrieved
//from stocksContentProvider, the unique identifier is retrieved from 
//a broadcast intent...


public class StockDetailFragment extends android.support.v4.app.Fragment implements 
																	LoaderManager.LoaderCallbacks<Cursor>{
	
	private static String TAG = "StockDetailFragment";
	private String contentURI;
	ContentResolver contentResolver;
	Context _context;	
	Cursor _cursor;
	View rootView;
	TextView StockName, TickerCode, Close, Change, Volume, StockPrice;
	ImageView ChangeImage;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		
		rootView = inflater.inflate(R.layout.fragment_stock_detail, container, false);
		_context = getActivity();
		Log.i(TAG, "StockDetailFragment Inflated");
		contentURI = getArguments().getString("URI");
		
		getLoaderManager().initLoader(0, null, this);
		
		StockName = (TextView) rootView.findViewById(R.id.StockName);
		TickerCode = (TextView) rootView.findViewById(R.id.TickerCode);
		Close = (TextView) rootView.findViewById(R.id.StockClose);
		Change = (TextView) rootView.findViewById(R.id.StockChange);
		ChangeImage = (ImageView)rootView.findViewById(R.id.ChangeImage);
		Volume = (TextView) rootView.findViewById(R.id.StockVolume);
		StockPrice = (TextView) rootView.findViewById(R.id.StockPrice);
		
		return rootView;
	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {		
		String[] projection = new String[]{
				StocksContentProvider.KEY_ID,
				StocksContentProvider.KEY_STOCK_CODE,
				StocksContentProvider.KEY_STOCK_NAME,
				StocksContentProvider.KEY_STOCK_SECTOR,
				StocksContentProvider.KEY_STOCK_PRICE,
				StocksContentProvider.KEY_STOCK_CHANGE,
				StocksContentProvider.KEY_STOCK_COST,
				StocksContentProvider.KEY_STOCK_FEES,
				StocksContentProvider.KEY_STOCK_CLOSE,
				StocksContentProvider.KEY_STOCK_MARKET_QUOTE,
				StocksContentProvider.KEY_STOCK_MARKET_VALUE,
				StocksContentProvider.KEY_STOCK_AVERAGE_UNIT_PRICE,
				StocksContentProvider.KEY_STOCK_TOTAL_UNITS,
				StocksContentProvider.KEY_STOCK_VOLUME
		};
		
		CursorLoader loader = new CursorLoader(_context, Uri.parse(contentURI), projection,null,null, null);
		
		return loader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		
		if(cursor != null && cursor.moveToFirst()){
			TickerCode.setText(cursor.getString(1) + " | "+ cursor.getString(3));
			StockName.setText(cursor.getString(2));
			Change.setText(DecimalFormatter.Format(cursor.getString(5)));
			Close.setText(DecimalFormatter.Format(cursor.getString(8)));
			toggleChangeImage(DecimalFormatter.Format(cursor.getString(5)));
			Volume.setText(DecimalFormatter.Format(cursor.getString(13)));
			StockPrice.setText(DecimalFormatter.Format(cursor.getString(4)));
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> cursor) {
		// TODO Auto-generated method stub
		
	}

	private void toggleChangeImage(String stockPriceChange){
		Double priceChange = Double.parseDouble(stockPriceChange);
		
		if(priceChange > 0.00){
			//+ve price change
			ChangeImage.setImageResource(R.drawable.action_green_arrow_up_1);
			ChangeImage.setVisibility(View.VISIBLE) ;
		}
		if(priceChange < 0.00){
			//-ve price change
			ChangeImage.setImageResource(R.drawable.action_red_arrow_down_1);
			ChangeImage.setVisibility(View.VISIBLE) ;
		}
		if(priceChange == 0.00){
			// no price change
			ChangeImage.setVisibility(View.GONE) ;
		}
	}
}
