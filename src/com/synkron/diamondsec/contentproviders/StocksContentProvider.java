package com.synkron.diamondsec.contentproviders;

import java.util.HashMap;

import com.synkron.diamondsec.dbopenhelpers.StocksDBOpenHelper;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class StocksContentProvider extends ContentProvider{
	
	public static final Uri CONTENT_URI = Uri.parse("content://com.synkron.diamondsec.contentproviders.StocksContentProvider/stocks");
	private static final int ALLROWS = 1;
	private static final int SINGLE_ROW = 2;
	private static final int SEARCH = 3;
	
	//database columns constants
	public static final String KEY_ID = "_id";
	public static final String KEY_STOCK_CODE = "stockCode";
	public static final String KEY_STOCK_NAME = "name";
	public static final String KEY_STOCK_SECTOR = "sector";
	public static final String KEY_STOCK_PRICE = "price";
	public static final String KEY_STOCK_CHANGE = "change";
	public static final String KEY_STOCK_COST = "cost";
	public static final String KEY_STOCK_FEES = "fees";
	public static final String KEY_STOCK_CLOSE = "lClose";
	public static final String KEY_STOCK_MARKET_QUOTE = "marketQuote";
	public static final String KEY_STOCK_MARKET_VALUE = "marketValue";
	public static final String KEY_STOCK_AVERAGE_UNIT_PRICE = "averageUnitPrice";
	public static final String KEY_STOCK_TOTAL_UNITS = "totalUnits";
	public static final String KEY_STOCK_VOLUME = "volume";
	public static final String KEY_SEARCH_COLUMN = KEY_STOCK_NAME;
	
	private static final UriMatcher uriMatcher;
	SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
	StocksDBOpenHelper dbHelper;
	
	static{
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI("com.synkron.diamondsec.contentproviders.StocksContentProvider",
				"stocks", ALLROWS);
		uriMatcher.addURI("com.synkron.diamondsec.contentproviders.StocksContentProvider", 
				"stocks/#", SINGLE_ROW);
		uriMatcher.addURI("com.synkron.diamondsec.contentproviders.StocksContentProvider",
				SearchManager.SUGGEST_URI_PATH_QUERY, SEARCH);
		uriMatcher.addURI("com.synkron.diamondsec.contentproviders.StocksContentProvider",
				SearchManager.SUGGEST_URI_PATH_QUERY + "/*", SEARCH);
		uriMatcher.addURI("com.synkron.diamondsec.contentproviders.StocksContentProvider",
				SearchManager.SUGGEST_URI_PATH_SHORTCUT, SEARCH);
		uriMatcher.addURI("com.synkron.diamondsec.contentproviders.StocksContentProvider",
				SearchManager.SUGGEST_URI_PATH_SHORTCUT + "/*", SEARCH);
	}
	
	//Projection for search suggestions....
	private static final HashMap<String, String> SEARCH_SUGGEST_PROJECTION_MAP;
	static{
		SEARCH_SUGGEST_PROJECTION_MAP = new HashMap<String, String>();
		SEARCH_SUGGEST_PROJECTION_MAP.put("_id", KEY_ID + " AS "+ "_id");
		SEARCH_SUGGEST_PROJECTION_MAP.put(SearchManager.SUGGEST_COLUMN_TEXT_1, KEY_SEARCH_COLUMN 
				+ " AS "+ SearchManager.SUGGEST_COLUMN_TEXT_1);
		SEARCH_SUGGEST_PROJECTION_MAP.put(SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID, KEY_ID 
				+ " AS "+ SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID);
	};
	
	@Override
	public int delete(Uri uri, String whereClause, String[] whereArgs) {
		SQLiteDatabase database = dbHelper.getWritableDatabase();
		
		int count;
		switch(uriMatcher.match(uri)){
		case ALLROWS:
				count = database.delete(StocksDBOpenHelper.DATABASE_TABLE, whereClause, whereArgs);
			break;
		case SINGLE_ROW:
			String segment = uri.getPathSegments().get(1);
			count = database.delete(StocksDBOpenHelper.DATABASE_TABLE, KEY_ID + "="
					+ segment
					+ (!TextUtils.isEmpty(whereClause) ? " AND ("
					+ whereClause + ')' : ""), whereArgs);
			break;
			
			default:
				throw new IllegalArgumentException("Unsupported URI: "+ uri);
		}
		
		getContext().getContentResolver().notifyChange(uri, null);
		
		return count;
	}

	@Override
	public String getType(Uri uri) {
		switch(uriMatcher.match(uri)){
			case ALLROWS: 
				return "vnd.android.cursor.dir/vnd.diamondsec.stock";
			case SINGLE_ROW: 
				return "vnd.android.cursor.item/vnd.diamondsec.stock";
			case SEARCH:
				return SearchManager.SUGGEST_MIME_TYPE;
			default:
				throw new IllegalArgumentException("Unsupported URI: "+ uri);
		}
	}

	@Override
	public Uri insert(Uri _uri, ContentValues values) {
		SQLiteDatabase database = dbHelper.getWritableDatabase();
		
		long rowID = database.insert(StocksDBOpenHelper.DATABASE_TABLE, "name", values);
		
		if(rowID > 0){
			Uri uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
			getContext().getContentResolver().notifyChange(uri, null);
			
			return uri;
		}
		throw new SQLException("Failed to insert row into "+ _uri);
	}

	@Override
	public boolean onCreate() {
		Context context = getContext();
		dbHelper = new StocksDBOpenHelper(context, StocksDBOpenHelper.DATABASE_NAME, null, 
				StocksDBOpenHelper.DATABASE_VERSION);
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		
		SQLiteDatabase database = dbHelper.getWritableDatabase();
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		
		queryBuilder.setTables(StocksDBOpenHelper.DATABASE_TABLE);
	
		//if this is a row query, limit the result set to the passed in row.
		switch(uriMatcher.match(uri)){
			case SINGLE_ROW:
				queryBuilder.appendWhere(KEY_ID + "=" + uri.getPathSegments().get(1));
				break;
			case SEARCH:
				String query = uri.getPathSegments().get(1);
				queryBuilder.appendWhere(KEY_SEARCH_COLUMN + " LIKE \"%"+ query + "%\"");
				queryBuilder.setProjectionMap(SEARCH_SUGGEST_PROJECTION_MAP);
				break;
			default:
					break;
		}
		
		//if no sort order is specified, sort alphabetically...
		String orderBy;
		if(TextUtils.isEmpty(sortOrder)){
			orderBy = KEY_STOCK_NAME;
		}else{
			orderBy = sortOrder;
		}
		
		//Apply the query to the underlying database.
		Cursor cursor = queryBuilder.query(database, projection, selection, selectionArgs, 
				null, null, orderBy);
		
		//Register the contexts ContentResolver to be notified if the cursor result set changes..
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		
		return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		SQLiteDatabase database = dbHelper.getWritableDatabase();
		
		int count;
		
		switch(uriMatcher.match(uri)){
		case ALLROWS:
				count = database.update(StocksDBOpenHelper.DATABASE_TABLE, values, selection, selectionArgs);
			break;
		case SINGLE_ROW:
			String segment = uri.getPathSegments().get(1);
			count = database.update(StocksDBOpenHelper.DATABASE_TABLE, values, 
					KEY_ID 
					+ "=" + segment
					+ (!TextUtils.isEmpty(selection) ? " AND ("
					+ selection + ')' : ""), selectionArgs);
			
			break;
			
			default:
				throw new IllegalArgumentException("Unknown URI "+ uri);
		}
		
		getContext().getContentResolver().notifyChange(uri, null);
		
		return count;
	}

}
