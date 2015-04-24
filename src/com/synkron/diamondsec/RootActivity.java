package com.synkron.diamondsec;

import java.util.ArrayList;
import java.util.zip.Inflater;

import com.synkron.diamondsec.adapters.SideMenuAdapter;
import com.synkron.diamondsec.fragments.ContentFragment;
import com.synkron.diamondsec.fragments.MyAccountFragment;
import com.synkron.diamondsec.fragments.StocksListFragment;

import android.support.v7.app.*;
import android.support.v7.widget.SearchView;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

@SuppressLint("NewApi") public class RootActivity extends ActionBarActivity {

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private CharSequence mTitle, mDrawerTitle;
	private String[] mAppMenu;
	private ArrayList<SideMenuModel> sideMenuArrayList;
	
	private Context _context;
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB) @SuppressLint("NewApi") @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_root);

		mTitle = mDrawerTitle = getTitle();
		mAppMenu = getResources().getStringArray(R.array.app_menu);
		
		sideMenuArrayList = new ArrayList<SideMenuModel>();
		sideMenuArrayList.add(new SideMenuModel(R.drawable.action_account,mAppMenu[0]));
		sideMenuArrayList.add(new SideMenuModel(R.drawable.action_order_history,mAppMenu[1]));
		sideMenuArrayList.add(new SideMenuModel(R.drawable.action_stocks_list,mAppMenu[2]));
		sideMenuArrayList.add(new SideMenuModel(R.drawable.action_market_info,mAppMenu[3]));
		sideMenuArrayList.add(new SideMenuModel(R.drawable.action_news,mAppMenu[4]));
		sideMenuArrayList.add(new SideMenuModel(R.drawable.action_logout,mAppMenu[5]));
		
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer,R.string.drawer_open,R.string.drawer_close){
			
			@SuppressLint("NewApi") 
			public void onDrawerClosed(View view){
				super.onDrawerClosed(view);
				getActionBar().setTitle(mTitle);
				invalidateOptionsMenu();
			}
			
			@SuppressLint("NewApi") public void 
			onDrawerOpened(View drawerView){
				super.onDrawerOpened(drawerView);
				getActionBar().setTitle(mTitle);
				invalidateOptionsMenu();
			}
		};
		
		mDrawerList = (ListView) findViewById(R.id.left_drawer);
		//mDrawerList.setAdapter(new ArrayAdapter<String>(this,R.layout.menu_list_item ,
		//	R.id.text1, mAppMenu));
		mDrawerList.setAdapter(new SideMenuAdapter(this, sideMenuArrayList));
		
		mDrawerList.setOnItemClickListener(new DrawerItemListClickListener());
		
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,GravityCompat.START);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		//getActionBar().setHomeButtonEnabled(true);
		
		if (savedInstanceState == null) {
			selectItem(0);
		}
		
	    // Get the intent, verify the action and get the query
	    Intent intent = getIntent();
	    if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
	      String query = intent.getStringExtra(SearchManager.QUERY);
	      
	      //doMySearch(query);
	    }
		_context = this;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();      
        inflater.inflate(R.menu.root, menu);
        
		//activates the searchable activity....
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo( searchManager.getSearchableInfo(getComponentName()) );
        
        return true;
		//return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		//int id = item.getItemId();
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}

	public boolean onPrepareOptionsMenu(Menu menu){
		return super.onPrepareOptionsMenu(menu);
	}
	
	public void onPostCreate(Bundle savedInstanceState){
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}
	
	public void onConfigurationChanged(Configuration newConfig){
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}
	
	@SuppressLint("NewApi") 
	public class DrawerItemListClickListener implements ListView.OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			// TODO Auto-generated method stub
			selectItem(arg2);
		}
	}
		private void selectItem(int position){
			android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
			android.support.v4.app.Fragment fragment;
			
			//select fragment based on menu selection..
			switch(position){
			case 0:
				fragment =  new MyAccountFragment();
				break;
			case 2:
				fragment = new StocksListFragment();
				break;
			case 5:
				//log off
				fragment = null;
	    		Intent intent = new Intent(_context, MainActivity.class);
	    		_context.startActivity(intent);
				
				break;
			default:
				fragment = new ContentFragment();
					break;
			}
			
			Bundle args = new Bundle();
			args.putInt(ContentFragment.ARG_MENU_NUMBER, position);
			
			if(fragment != null){
				fragment.setArguments(args);
				fragmentManager.beginTransaction()
					.replace(R.id.content_frame, fragment)
					.commit();
				
				mDrawerList.setItemChecked(position, true);
				setTitle(mAppMenu[position]);
				mDrawerLayout.closeDrawer(mDrawerList);
			}
		}
		
		public void setTitle(CharSequence title){
			mTitle = title;
			getActionBar().setTitle(mTitle);
		}
}