package com.jpmorgan.thefoodchain.Menus;

import com.jpmorgan.thefoodchain.R;
import com.jpmorgan.thefoodchain.Helpers.ServerAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class MainMenu extends Activity {

	private Button btStartDelivery, btCall, btLogout;
	private TextView tvStatus;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//Remove title bar.
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_main_menu);

		//Instantiate buttons and listeners.
		btStartDelivery = (Button)findViewById(R.id.btStartDelivery);
		btStartDelivery.setOnClickListener(new ButtonClicked());
		btCall = (Button)findViewById(R.id.btCall);
		btCall.setOnClickListener(new ButtonClicked());
		btLogout = (Button)findViewById(R.id.btLogOut);
		btLogout.setOnClickListener(new ButtonClicked());
		tvStatus = (TextView)findViewById(R.id.tvStatus);

		//Hide the start delivery button if no delivery schedule assigned.
		if (!ServerAdapter.isAssigned()) {
			tvStatus.setText("Unassigned");//Move to string resource.
			btStartDelivery.setVisibility(View.GONE);
		}
	}

	//Button click listener.
	public class ButtonClicked implements View.OnClickListener{
		@Override
		public void onClick(View v) {
			//Open delivery menu.
			if (v == btStartDelivery){
				Intent intent = new Intent(MainMenu.this, DeliveryMenu.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION );
				startActivity(intent);
				return;
			}

			//Open the dialer with the loaded number.
			if (v == btCall){
				Uri phoneNumberUri = Uri.parse("tel: 020 7843 1800");
				Intent i = new Intent(Intent.ACTION_DIAL, phoneNumberUri);
				startActivity(i);
			}
			
			if (v == btLogout) {
				//Clear the credentials.
				SharedPreferences sharedPref = getSharedPreferences(getString(R.string.PrefFile), Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = sharedPref.edit();
				editor.putString(getString(R.string.Username), "");
				editor.putString(getString(R.string.Password), "");
				editor.commit();
				
				//Go to login screen.
				Intent intent = new Intent(MainMenu.this, LoginMenu.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION );
				startActivity(intent);
				finish();
				return;
			}
		}
	}
}
