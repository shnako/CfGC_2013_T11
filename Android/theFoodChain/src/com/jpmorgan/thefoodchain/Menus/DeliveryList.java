package com.jpmorgan.thefoodchain.Menus;

import com.jpmorgan.thefoodchain.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class DeliveryList extends Activity{

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//Remove title bar.
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.listlayout);

		String[] postcodes = getIntent().getExtras().getStringArray(("Postcodes"));
		String[] statuses = getIntent().getExtras().getStringArray("Statuses");
		String[] results = new String[postcodes.length];
		for(int i = 0; i < postcodes.length; i++){
			results[i] = postcodes[i] + " | " + statuses[i];
		}
		
		
		ListView listView = (ListView) findViewById(R.id.list2);
		listView.setTextFilterEnabled(true);
		listView.setAdapter(new ArrayAdapter<String>(this, R.id.list_item_description, results));
 
		/*
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			    // When clicked, show a toast with the TextView text
			    Toast.makeText(getApplicationContext(),
				((TextView) view).getText(), Toast.LENGTH_SHORT).show();
			}
		});
		*/
	}
}
