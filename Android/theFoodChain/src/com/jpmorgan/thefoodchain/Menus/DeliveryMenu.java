package com.jpmorgan.thefoodchain.Menus;

import com.jpmorgan.thefoodchain.R;
import com.jpmorgan.thefoodchain.Dialogs.ConfirmationDialog;
import com.jpmorgan.thefoodchain.Dialogs.InformationDialog;
import com.jpmorgan.thefoodchain.Helpers.ServerAdapter;
import com.jpmorgan.thefoodchain.Objects.DeliverySchedule;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class DeliveryMenu extends Activity {

	private Button btNavigate, btShowMeals, btMarkDelivered, btSkip, btShowList;
	private Button clickedButton;
	private DeliverySchedule deliverySchedule;
	private TextView tvAddress;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//Remove title bar.
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_delivery_menu);

		//Instantiate buttons and listeners.
		btNavigate = (Button)findViewById(R.id.btNavigate);
		btNavigate.setOnClickListener(new ButtonClicked());
		btMarkDelivered = (Button)findViewById(R.id.btMarkDelivered);
		btMarkDelivered.setOnClickListener(new ButtonClicked());
		btSkip = (Button)findViewById(R.id.btSkip);
		btSkip.setOnClickListener(new ButtonClicked());
		btShowMeals = (Button)findViewById(R.id.btShowMeals);
		btShowMeals.setOnClickListener(new ButtonClicked());
		tvAddress = (TextView)findViewById(R.id.tvAddress);

		//Get deliveries.
		deliverySchedule = ServerAdapter.getDeliveries();
		tvAddress.setText(tvAddress.getTag() + deliverySchedule.getAddress());
	}

	//Button click listener.
	public class ButtonClicked implements View.OnClickListener{
		@Override
		public void onClick(View v) {
			clickedButton = (Button) v;

			if (v == btNavigate){
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=" + deliverySchedule.getCurrentLatitude() + "," + deliverySchedule.getCurrentLongitude()));
				intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION );
				startActivity(intent);
				return;
			}

			if (v == btMarkDelivered) {
				ConfirmationDialog.LaunchNewDialog(DeliveryMenu.this, "Confirm operation", "Mark this as delivered?");
			}

			if (v == btSkip) {
				ConfirmationDialog.LaunchNewDialog(DeliveryMenu.this, "Confirm operation", "Are you sure you want to skip this?");
			}

			if (v == btShowMeals) {
				InformationDialog.LaunchNewDialog(DeliveryMenu.this, "Meals", deliverySchedule.getCurrentDeliveryMeals());
			}
			
			if (v == btShowList){
				//Extract the data to display.
				String[] postcodes = new String[deliverySchedule.getCount()];
				String[] statuses = new String[deliverySchedule.getCount()];
				for(int i = 0; i < deliverySchedule.getCount(); i++) {
					postcodes[i] = deliverySchedule.getStatus();
					statuses[i] = deliverySchedule.getStatus();
				}
				
				//Send to delivery list and open it.
				Intent openDialog = new Intent(DeliveryMenu.this, DeliveryList.class);
				openDialog.putExtra("Postcodes", postcodes);
				openDialog.putExtra("Statuses", statuses);
				startActivity(openDialog);
			}
		}
	}
	
	private void notifyCompleted(){
		InformationDialog.LaunchNewDialog(DeliveryMenu.this, "All done!", "You have completed all the deliveries!");
		finish();
	}

	//Delivery changed listener.
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		//Only update if result confirmed.
		if (resultCode == Activity.RESULT_OK){
			if (clickedButton == btMarkDelivered) {
				//Change the delivery and update components.
				if (deliverySchedule.nextDelivery()) {
					tvAddress.setText(tvAddress.getTag() + deliverySchedule.getAddress());
				} else {
					notifyCompleted();
				}
				return;
			}
			
			if (clickedButton == btSkip) {
				if (!deliverySchedule.skip()) {
					notifyCompleted();
				} else {
					tvAddress.setText(tvAddress.getTag() + deliverySchedule.getAddress());
				}
			}
		}
	}
}