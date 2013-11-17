package com.jpmorgan.thefoodchain.Menus;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.jpmorgan.thefoodchain.R;

import com.jpmorgan.thefoodchain.Dialogs.InformationDialog;
import com.jpmorgan.thefoodchain.Helpers.ServerAdapter;
import com.jpmorgan.thefoodchain.Objects.Account;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class LoginMenu extends Activity {

	private Button btLogin, btRegister;
	private EditText txtUser, txtPass; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//Check for credentials.
		Context context = LoginMenu.this;
		SharedPreferences sharedPrefs = context.getSharedPreferences(
				getString(R.string.PrefFile), Context.MODE_PRIVATE);
		final String username = sharedPrefs.getString("Username", "");
		final String password = sharedPrefs.getString("Password", "");

		//Remove title bar.
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_login_menu);

		//Instantiate components and listeners.
		btLogin = (Button)findViewById(R.id.btLogin);
		btLogin.setOnClickListener(new ButtonClicked());
		btRegister = (Button)findViewById(R.id.btRegister);
		btRegister.setOnClickListener(new ButtonClicked());
		txtUser = (EditText)findViewById(R.id.txtUsername);
		txtPass = (EditText)findViewById(R.id.txtPassword);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login_menu, menu);
		return true;
	}

	private void showMainMenu(){
		Intent intent = new Intent(LoginMenu.this, MainMenu.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION );
		startActivity(intent);
		this.finish();
	}

	//Button click listener.
	public class ButtonClicked implements View.OnClickListener{
		@Override
		public void onClick(View v) {
			if (v == btLogin){
				//Check username.
				if (txtUser.getText().toString().length() == 0) {
					InformationDialog.LaunchNewDialog(LoginMenu.this, "Incomplete input", "Please input your username!");
					return;
				}

				//Check password.
				if (txtPass.getText().toString().length() == 0) {
					InformationDialog.LaunchNewDialog(LoginMenu.this, "Incomplete input", "Please input your password!");
					return;
				}

				if (ServerAdapter.checkCredentials(new Account(txtUser.getText().toString(), txtPass.getText().toString()))){
					//Save credentials.
					SharedPreferences sharedPref = getSharedPreferences(getString(R.string.PrefFile), Context.MODE_PRIVATE);
					SharedPreferences.Editor editor = sharedPref.edit();
					editor.putString(getString(R.string.Username), txtUser.getText().toString());
					editor.putString(getString(R.string.Password), txtPass.getText().toString());
					editor.commit();

					showMainMenu();
					return;
				}

				//If reached, login failed.
				InformationDialog.LaunchNewDialog(LoginMenu.this, "", "Invalid credentials!");
			}

			//Show registration activity.
			if (v == btRegister) {
				Intent intent = new Intent(LoginMenu.this, RegisterMenu.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION );
				startActivity(intent);
				return;
			}
		}
	}
}