package com.arsari.analyticsmobile;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics; // Import FirebaseAnalytics

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.Set;
import java.util.UUID;


public class MainActivity extends AppCompatActivity {

	private FirebaseAnalytics mFirebaseAnalytics; // Declare FirebaseAnalytics
	private final Random rand = new Random();

	private final String uuID= "U-" + UUID.randomUUID(); // Universally Unique Identifier
	private String ui; // User ID
	private Boolean log = false; // Login status

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);

		mFirebaseAnalytics = FirebaseAnalytics.getInstance(this); // Obtain the FirebaseAnalytics instance.

		Button phoneButton = findViewById(R.id.phoneButton);
		Button emailButton = findViewById(R.id.emailButton);
		Button loginButton = findViewById(R.id.loginButton);
		Button logoutButton = findViewById(R.id.logoutButton);
		Button purchaseButton = findViewById(R.id.purchaseButton);

		phoneButton.setOnClickListener(view ->
			userInteraction(phoneButton.getText().toString())
		);

		emailButton.setOnClickListener(view ->
			userInteraction(emailButton.getText().toString())
		);

		loginButton.setOnClickListener(view ->
			userInteraction(loginButton.getText().toString())
		);

		logoutButton.setOnClickListener(view ->
			userInteraction(logoutButton.getText().toString())
		);

		purchaseButton.setOnClickListener(view ->
			userInteraction(purchaseButton.getText().toString())
		);
	}

	public void userInteraction(@NonNull String btn) {

		Bundle params = new Bundle();
		DecimalFormat df = new DecimalFormat("0.00");

		String en = ""; // Event name
		String et = ""; // Event type
		String cm; // Contact method
		String cc = "USD"; // Currency code
		String eventTypeConversion = "conversion";
		String eventTypeUserInteraction = "user interaction";
		String message = "";
		int qty; // Item quantity
		double price; // Item price
		double ev; // Event value

		if (log.equals(false)) {
			ui = "guest";
		}

		if (btn.equals("Phone") || btn.equals("Email")) {
			en = "generated_lead";
			et = eventTypeConversion;

			if (btn.equals("Phone")) {
				cm = "phone";
				ev = 25;
			} else {
				cm = "email";
				ev = 50;
			}

			params.putString("contact_method", cm);
			params.putString(FirebaseAnalytics.Param.CURRENCY, cc);
			params.putDouble(FirebaseAnalytics.Param.VALUE, ev);
		}

		if (btn.equals("Purchase")) {
			en = "purchase";
			et = eventTypeConversion;
			qty = rand.nextInt(100) + 1;
			price = rand.nextInt(20) + rand.nextFloat();
			ev = Double.parseDouble(df.format(qty * price));

			params.putString(FirebaseAnalytics.Param.CURRENCY, cc);
			params.putDouble(FirebaseAnalytics.Param.VALUE, ev);
		}

		if (btn.equals("Sign In")) {
			if (log.equals(true)) {
				Toast.makeText(MainActivity.this, "You already Sign In!", Toast.LENGTH_SHORT).show();
				en = "login_error";
				et = "content tool";
				message = "You already Sign In!";
			} else {
				en = "login";
				et = eventTypeConversion;
				log = true;
				ui = uuID;

				params.putString(FirebaseAnalytics.Param.METHOD, "google");
			}
		}

		if (btn.equals("Sign Out")) {
			if (log.equals(false)) {
				Toast.makeText(MainActivity.this, "You already Sign Out!", Toast.LENGTH_SHORT).show();
				en = "logout_error";
				et = "content tool";
				message = "You already Sign Out!";
			} else {
				en = "logout";
				et = eventTypeUserInteraction;
				log = false;
			}
		}

		if (en.equals("login_error") || en.equals("logout_error")) {
			params.putString("error_message", message);
			params.putBoolean("toast_impression", true);
		}

		params.putString("btn_label", btn);
		params.putFloat("event_timestamp", new Date().getTime()); // milliseconds
		params.putString("custom_timestamp", timeStamp()); // ISO 8061
		params.putString("event_type", et);
		params.putBoolean("logged", log);
		params.putString("user_id", ui);

		switch (en) {
			case "generated_lead":
				mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.GENERATE_LEAD, params);
				break;
			case "purchase":
				mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.PURCHASE, params);
				break;
			case "login":
				mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN, params);
				break;
			default:
				mFirebaseAnalytics.logEvent(en, params);
				break;
		}

		displayEvent(params, btn, en);
	}

	public void displayEvent(Bundle params, String btn, String en) {
		TextView displayData;
		displayData = findViewById(R.id.dataLayerTextView);

		JSONObject json = new JSONObject();
		Set<String> keys = params.keySet();
		for (String key : keys) {
			try {
				json.put(key, JSONObject.wrap(params.get(key)));
			} catch(JSONException e) {
				displayData.setText((CharSequence) e);
			}
		}

		try {
			String eventLog = "Button click -> " + btn + "\nEvent log -> " + en.toUpperCase() + "\n=== dataLayer ===\n" + json.toString(4);
			displayData.setText(eventLog);
		} catch(JSONException e) {
			displayData.setText((CharSequence) e);
		}
	}

	public String timeStamp() {
		SimpleDateFormat pattern = new SimpleDateFormat("yyyy-MM-dd' T'HH:mm:ss.SSS' UTC'XXX", Locale.getDefault());
		Timestamp time = new Timestamp(System.currentTimeMillis());
		return pattern.format(time);
	}
}
