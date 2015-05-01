package com.synkron.diamondsec;

import com.synkron.diamondsec.connectors.LoginConnector;

import android.annotation.TargetApi;
import android.app.ActionBar;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH) 
public class MainActivity extends ActionBarActivity {
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       
        //getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        
        setContentView(R.layout.activity_main);
        
        
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
        
        ActionBar actionBar = getActionBar();
        actionBar.setSubtitle("Login");
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.hide();
        
    }

    public void broadcastNotification(){
    	
    	int icon = R.drawable.icon;
		String tickerText = "Diamond Securities App is Running";
		
		Intent intent = new Intent(this, MainActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
		
		Notification.Builder builder = new Notification.Builder(this);
		builder.setAutoCancel(true)
				.setTicker(tickerText)
				.setSmallIcon(icon)
				.setContentIntent(pendingIntent)
				.setContentText("We are testing notifications");

		NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
		
		int NOTIFICATION_REF = 1;
		
		notificationManager.notify(NOTIFICATION_REF, builder.getNotification());
    }
    
    public void login(String CustomerID, String Password){
    	//call connector and attempt login..
    	
    		LoginConnector _connectorTask = new LoginConnector(this);
    		_connectorTask.execute(LoginConnector.API_LOGIN_URL, CustomerID, Password);
    	
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
    	
		Button btnLogin;
    	TextView txtVwCreateAccount, txtVwForgot;
    	EditText txtCustomerID , txtPassword;
    	
    	final Context context = this.getActivity();
    	
        public PlaceholderFragment() {
        
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            
            
            btnLogin = (Button) rootView.findViewById(R.id.btnLogin);
            txtCustomerID = (EditText) rootView.findViewById(R.id.edTxtCustomerID);
            txtPassword = (EditText) rootView.findViewById(R.id.edTxtPassword);
            
            
            btnLogin.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					//AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
					//dialog.setTitle("Button Click Alert");
					//dialog.setMessage("You just clicked the login button");
					
					//dialog.show();
					MainActivity myActivity = (MainActivity) getActivity();
					myActivity.login(txtCustomerID.getText().toString(), txtPassword.getText().toString());
				}
            });
            
            txtVwCreateAccount = (TextView) rootView.findViewById(R.id.txtVwCreateAccount);
            txtVwCreateAccount.setOnClickListener(new OnClickListener(){
            	
            	public void onClick(View v){
            		Toast toast = Toast.makeText(getActivity(), "creating user account", Toast.LENGTH_SHORT);
            		toast.show();
            	}
            	
            });
            
            txtVwForgot = (TextView) rootView.findViewById(R.id.txtVwForgot);
            txtVwForgot.setOnClickListener(new OnClickListener(){
            	
            	public void onClick(View v){
            		MainActivity myActivity = (MainActivity)getActivity();
            		myActivity.broadcastNotification();
            	}
            	
            });
            return rootView;
        }
        
        
     }
}
