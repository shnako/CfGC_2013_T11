package com.jpmorgan.thefoodchain.Dialogs;

import com.jpmorgan.thefoodchain.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class InformationDialog extends Activity {

	private Button btOK;
	private TextView tvMessage;

	/**
	 * Method used to launch the dialog in a static manner. Returns the result of the function.
	 * @param activity - The activity of the parent window (the one you are using this function from).
	 * @param title - The title of the parameter being changed.
	 * @param message - The message to be displayed.
	 */
	public static void LaunchNewDialog(Activity activity, String title, String message){
		Intent openDialog = new Intent(activity, InformationDialog.class);
		openDialog.putExtra("title", title);
		openDialog.putExtra("message", message);
		activity.startActivityForResult(openDialog, 1);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_information_dialog);

		//Initialize title.
		setTitle(getIntent().getExtras().getString("title"));

		//Initialize message.
		tvMessage = (TextView)findViewById(R.id.tvMessage);
		tvMessage.setText(getIntent().getExtras().getString("message"));

		//Initialize buttons.
		btOK = (Button)findViewById(R.id.btOK);
		btOK.setOnClickListener(new ButtonClicked());
	}

	/**
	 * Sends the result back to the parent activity and finishes the current activity.
	 */
	private class ButtonClicked implements View.OnClickListener{
		public void onClick(View v) {
			finish();
		}
	}
}
