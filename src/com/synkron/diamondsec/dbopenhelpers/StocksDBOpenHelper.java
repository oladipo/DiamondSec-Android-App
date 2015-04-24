package com.synkron.diamondsec.dbopenhelpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.synkron.diamondsec.contentproviders.StocksContentProvider;

public class StocksDBOpenHelper extends SQLiteOpenHelper{

	private static final String TAG = "StocksDBOpenHelper";
	
	public static final String DATABASE_NAME = "stocksDB.db";
	public static final String DATABASE_TABLE = "StocksTable";
	public static final int DATABASE_VERSION = 1;
	
	private static final String DATABASE_CREATE = "create table "+ DATABASE_TABLE + " ("
			+ StocksContentProvider.KEY_ID + " integer primary key autoincrement,"
			+ StocksContentProvider.KEY_STOCK_CODE + " TEXT,"
			+ StocksContentProvider.KEY_STOCK_NAME + " TEXT,"
			+ StocksContentProvider.KEY_STOCK_MARKET_QUOTE + " TEXT,"
			+ StocksContentProvider.KEY_STOCK_MARKET_VALUE + " TEXT,"
			+ StocksContentProvider.KEY_STOCK_PRICE + " TEXT,"
			+ StocksContentProvider.KEY_STOCK_AVERAGE_UNIT_PRICE + " TEXT,"
			+ StocksContentProvider.KEY_STOCK_CHANGE + " TEXT,"
			+ StocksContentProvider.KEY_STOCK_CLOSE + " TEXT,"
			+ StocksContentProvider.KEY_STOCK_COST + " TEXT,"
			+ StocksContentProvider.KEY_STOCK_FEES + " TEXT,"
			+ StocksContentProvider.KEY_STOCK_SECTOR + " TEXT,"
			+ StocksContentProvider.KEY_STOCK_TOTAL_UNITS + " TEXT,"
			+ StocksContentProvider.KEY_STOCK_VOLUME + " TEXT)";
	
	private SQLiteDatabase stocksDB;
	
	
	public StocksDBOpenHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.i(TAG, "Executing query on database :"+ DATABASE_CREATE);
		
		db.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(TAG, "Upgrading database from version" + oldVersion + " to "+ newVersion + " which will destroy all old data");
		
		db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
		
		onCreate(db);
	}

}
