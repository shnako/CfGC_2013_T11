package com.jpmorgan.thefoodchain.Helpers;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.StrictMode;

import com.jpmorgan.thefoodchain.Dialogs.InformationDialog;
import com.jpmorgan.thefoodchain.Objects.Account;
import com.jpmorgan.thefoodchain.Objects.Delivery;
import com.jpmorgan.thefoodchain.Objects.DeliverySchedule;

public class ServerAdapter{
	private static Account acc;

	public static boolean checkCredentials (final Account account) {
		try{
			//Open connection.
			String url = "http://serengeti.uk.to/api/user_check";
			URL obj = new URL(url);
			final HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			//Add request headers.
			con.setRequestMethod("POST");
			con.setRequestProperty("X-Username", account.getUsername());
			con.setRequestProperty("X-Password", account.getPassword());
			Callable<Boolean> callable = new Callable<Boolean>() {
				public Boolean call() {
					int responseCode;
					try {
						responseCode = con.getResponseCode();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return false;					}

					//Credentials valid.
					if (responseCode == 200){
						System.out.print(responseCode + "TEST");
						acc = account;
						return true;
					}		
					return false;
				}
			};
			ExecutorService executor = Executors.newSingleThreadExecutor();
			Future<Boolean> future = executor.submit(callable);
			boolean result = future.get();
			executor.shutdown();

			//Invalid credentials or whatever.
			System.out.print(result + "TESTRESULT");
			return result;
		}
		catch(Exception ex){
			return false;
		}
	}

	public static boolean isAssigned(){
		try{
			//Open connection.
			String url = "http://serengeti.uk.to/api/is_delivery_assigned";
			URL obj = new URL(url);
			final HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			//Add request headers.
			con.setRequestMethod("POST");
			con.setRequestProperty("X-Username", acc.getUsername());
			con.setRequestProperty("X-Password", acc.getPassword());
			Callable<Boolean> callable = new Callable<Boolean>() {
				public Boolean call() {
					int responseCode;
					try {
						responseCode = con.getResponseCode();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return false;					}

					//Credentials valid.
					if (responseCode == 200){
						return true;
					}		
					return false;
				}
			};
			ExecutorService executor = Executors.newSingleThreadExecutor();
			Future<Boolean> future = executor.submit(callable);
			boolean result = future.get();
			executor.shutdown();

			//Invalid credentials or whatever.
			return result;
		}
		catch(Exception ex){
			return false;
		}
	}
	/*
	public static void updateLocation(){
		//TODO
	}
	 */
	public static void setAsDelivered(int deliveryID){
		//TODO
	}

	public static DeliverySchedule getDeliveries(){
		try{
			//Create object.
			final DeliverySchedule result = new DeliverySchedule();

			//Open connection.
			String url = "http://serengeti.uk.to/api/get_deliveries";
			URL obj = new URL(url);
			final HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			//Add request headers.
			con.setRequestMethod("POST");
			con.setRequestProperty("X-Username", acc.getUsername());
			con.setRequestProperty("X-Password", acc.getPassword());
			Callable<Boolean> callable = new Callable<Boolean>() {
				public Boolean call() {
					int responseCode;
					try {
						con.connect();
						int response= con.getResponseCode();
						if (response != HttpURLConnection.HTTP_OK) {
							return false;
						}
						InputStream in = con.getInputStream();
						InputStreamReader stream = new InputStreamReader(in);
						BufferedReader reader = new BufferedReader(stream);
						StringBuffer sb = new StringBuffer();
						while (true) {
							String s = reader.readLine();
							if (s == null)
								break;
							sb.append(s);
						}
						String json = sb.toString();
						try {
							JSONArray deliveries = new JSONArray(json);
							
							for(int i = 0; i < deliveries.length(); i++){
								JSONObject jsonobj = deliveries.getJSONObject(i);
								Delivery deli = new Delivery(jsonobj.getString("address"), jsonobj.getDouble("lng"), jsonobj.getDouble("lat"));
								JSONArray meals = jsonobj.getJSONArray("meals");
								for (int j = 0; j < meals.length(); j++){
									deli.addMeals(meals.getString(j));
								}
								result.addDelivery(deli);
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
				
						
						responseCode = con.getResponseCode();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return false;					
					}	
					return false;
				}
			};
			ExecutorService executor = Executors.newSingleThreadExecutor();
			Future<Boolean> future = executor.submit(callable);
			boolean res = future.get();
			executor.shutdown();

			//Invalid credentials or whatever.
			System.out.print(result + "TESTRESULT");
			return result;
		}
		catch(Exception ex){
			return null;
		}
	}
}
