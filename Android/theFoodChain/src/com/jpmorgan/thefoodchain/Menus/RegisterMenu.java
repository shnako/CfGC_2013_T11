package com.jpmorgan.thefoodchain.Menus;

import com.jpmorgan.thefoodchain.R;
import com.jpmorgan.thefoodchain.Dialogs.InformationDialog;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterMenu extends Activity {
	private TextView txtFirstName, txtPhone;
	@SuppressWarnings("unused")
	private CheckBox cbLicence;
	private Button btRegister;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);

		//Remove title bar.
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_register_menu);

		btRegister = (Button)findViewById(R.id.btRegister);
		btRegister.setOnClickListener(new ButtonClicked());
		txtFirstName = (EditText)findViewById(R.id.txtFirstname);
		txtPhone = (EditText)findViewById(R.id.txtPhone);
		cbLicence = (CheckBox)findViewById(R.id.cbDriving);
	}
	
	public class ButtonClicked implements View.OnClickListener{
		@Override
		public void onClick(View v) {
			if (v == btRegister){
				//Check username.
				if (txtFirstName.getText().toString().length() == 0) {
					InformationDialog.LaunchNewDialog(RegisterMenu.this, "Incomplete input", "Please input your first name!");
					return;
				}
				
				//Check password.
				if (txtPhone.getText().toString().length() == 0) {
					InformationDialog.LaunchNewDialog(RegisterMenu.this, "Incomplete input", "Please input your phone number!");
					return;
				}
				
				//TODO
				InformationDialog.LaunchNewDialog(RegisterMenu.this, "Registration complete", "Thank you! Your registration has been sent. We will contact you soon!");
				finish();
				return;
			}
		}
	}
}
