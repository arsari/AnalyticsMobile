package com.arsari.analyticsmobile;


import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Mobile Analytics Implementation Playground
 *
 * A self playground of analytic implementation on an Android Mobile App using Firebase SDK, GTM,
 * and GA4 mobile data stream that allows to explore the implementation of a dataLayer array-objects
 * or Bundle firing and analyzing user interactions in GA4.
 *
 * @author:	Arturo Santiago-Rivera (asantiago@arsari.com)
 * @license: MIT License
 */
public class MainActivity extends AppCompatActivity {
	private static final String TAG = "AnalyticsMobileLog:";
	private FirebaseAnalytics mFirebaseAnalytics; // Declare FirebaseAnalytics
	private final Random rand = new Random();
	private final String uuID= "U-" + UUID.randomUUID(); // Universally Unique Identifier
	private String ui = "guest"; // User ID
	private Boolean logged = false; // Login status
	private String searchTerm; /// Search dialog user keyword input
	private String btnName;  // Search dialog button names
	private String resourceId; // App resource ID
	private String url; // Link URL
	private TextView displayJSON; // TextView that display events dataLayer object
	private int eventsCounter = 1; // Track number of user interactions
	SpannableStringBuilder builder = new SpannableStringBuilder();

	private final Handler videoHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			Button vbtn = findViewById(R.id.videoButton);
			vbtn.setText(R.string.video_btn);
			vbtn.setBackgroundColor(getColor(R.color.purple_500));
		}
	};
	boolean vplay = false; // Playing video
	boolean vstop = true; // Stopped video
	int vprogress = 0; // Video progress
	int vduration = 300; // Video length
	int milestone = 0; // Video progress milestone

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);

		mFirebaseAnalytics = FirebaseAnalytics.getInstance(MainActivity.this); // Obtain the FirebaseAnalytics instance.

		ActionBar actionBar = getSupportActionBar();
		assert actionBar != null;
		actionBar.setSubtitle("  Mobile Analytics Implementation");
		actionBar.setIcon(R.mipmap.ic_launcher_png);
		actionBar.setDisplayShowHomeEnabled(true);

		ImageView badgeImageView = findViewById(R.id.githubBadgeImageView);
		Uri badgeURI = Uri.parse("https://img.shields.io/github/last-commit/arsari/AnalyticsMobile?color=blue&style=for-the-badge&logo=github");
		ImageView tagImageView = findViewById(R.id.githubTagImageView);
		Uri tagURI = Uri.parse("https://img.shields.io/github/v/tag/arsari/AnalyticsMobile?color=orange&logo=github&sort=semver&style=for-the-badge");

		RequestBuilder<PictureDrawable> requestBuilder = Glide.with(MainActivity.this)
			.as(PictureDrawable.class)
			.transition(withCrossFade())
			.placeholder(R.drawable.placeholder)
			.error(R.drawable.image_error)
			.listener(new SvgSoftwareLayerSetter());

		requestBuilder.load(badgeURI).fitCenter().into(badgeImageView);
		requestBuilder.load(tagURI).fitCenter().into(tagImageView);

		displayJSON = findViewById(R.id.displayJSONTextView);
		displayJSON.setMovementMethod(ScrollingMovementMethod.getInstance());

		TextView intro = findViewById(R.id.introTextView);
		Button loginButton = findViewById(R.id.loginButton);
		Button videoButton = findViewById(R.id.videoButton);
		Button logoutButton = findViewById(R.id.logoutButton);
		Button phoneButton = findViewById(R.id.phoneButton);
		Button purchaseButton = findViewById(R.id.purchaseButton);
		Button emailButton = findViewById(R.id.emailButton);
		Button searchButton = findViewById(R.id.searchButton);
		TextView authorLink = findViewById(R.id.authorTextView);

		badgeImageView.setOnClickListener(view -> {
			String text = badgeImageView.getContentDescription().toString();
			url = "https://github.com/arsari/AnalyticsMobile";
			resourceId = getResources().getResourceEntryName(badgeImageView.getId());
			btnName = getString(R.string.ext_link);
			userInteraction(btnName, resourceId, text);
		});

		loginButton.setOnClickListener(view ->{
			String click = loginButton.getText().toString();
			resourceId = getResources().getResourceEntryName(loginButton.getId());
			userInteraction(click, resourceId, null);
		});

		phoneButton.setOnClickListener(view -> {
			String click = phoneButton.getText().toString();
			resourceId = getResources().getResourceEntryName(phoneButton.getId());
			userInteraction(click, resourceId, null);
		});

		emailButton.setOnClickListener(view ->{
			String click = emailButton.getText().toString();
			resourceId = getResources().getResourceEntryName(emailButton.getId());
			userInteraction(click, resourceId, null);
		});

		purchaseButton.setOnClickListener(view ->{
			String click = purchaseButton.getText().toString();
			resourceId = getResources().getResourceEntryName(purchaseButton.getId());
			userInteraction(click, resourceId, null);
		});

		videoButton.setOnClickListener(view ->{
			String click = videoButton.getText().toString();
			resourceId = getResources().getResourceEntryName(videoButton.getId());
			userInteraction(click, resourceId, null);
		});

		searchButton.setOnClickListener(view -> {
			String click = searchButton.getText().toString();
			resourceId = getResources().getResourceEntryName(searchButton.getId());
			userInteraction(click, resourceId, null);

			// get search_dialog.xml view
			LayoutInflater li = LayoutInflater.from(MainActivity.this);
			View dialogView = li.inflate(R.layout.search_dialog, null);

			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);

			// set search_dialog.xml to alertdialog builder
			alertDialogBuilder.setView(dialogView);

			final EditText searchInput = dialogView.findViewById(R.id.searchTextInputEditText);
			LinearLayout dialogButtons = dialogView.findViewById(R.id.dialogLinearLayout);

			// set dialog message
			alertDialogBuilder
				.setCancelable(false)
				.setPositiveButton("OK",
					(dialog, id) -> {
						String keyword = searchInput.getText().toString();
						if (keyword.isEmpty()) {
							btnName = "Search Error";
							resourceId = getResources().getResourceEntryName(searchInput.getId());
						} else if (keyword.contains("mailto:") || keyword.contains("@") || keyword.contains("tel:") || keyword.contains("sms:")) {
							btnName = "Search Error: PII";
							resourceId = getResources().getResourceEntryName(searchInput.getId());
						} else {
							searchTerm = searchInput.getText().toString();
							btnName = "Search OK";
							resourceId = getResources().getResourceEntryName(dialogButtons.getId());
						}
						InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(searchInput.getWindowToken(), 0);

						userInteraction(btnName, resourceId, null);
					})
				.setNegativeButton("Cancel",
					(dialog, id) -> {
						btnName = "Search Cancel";
						resourceId = getResources().getResourceEntryName(dialogButtons.getId());
						userInteraction(btnName, resourceId,null);
						dialog.cancel();
					});

			// create alert dialog
			AlertDialog alertDialog = alertDialogBuilder.create();

			// show it
			alertDialog.show();
		});

		logoutButton.setOnClickListener(view ->{
			String click = logoutButton.getText().toString();
			resourceId = getResources().getResourceEntryName(logoutButton.getId());
			userInteraction(click, resourceId, null);
		});

		authorLink.setOnClickListener(view -> {
			String text = authorLink.getText().toString();
			url = "https://www.linkedin.com/in/arturosantiago";
			resourceId = getResources().getResourceEntryName(authorLink.getId());
			btnName = getString(R.string.ext_link);
			userInteraction(btnName, resourceId, text);
		});

		intro.setOnClickListener(view -> {
			Log.w(TAG, "Clearing display JSON view");
			builder.clear();
			String click = getResources().getResourceEntryName(intro.getId());
			resourceId = getResources().getResourceEntryName(intro.getId());
			String text = intro.getContentDescription().toString();
			userInteraction(click, resourceId, text);
		});
	}

	/**
	 * It tracks the screen view of the app.
	 */
	@Override
	public void onResume() {
		super.onResume();
		// Screen view tracking
		btnName = "App Init";
		userInteraction(btnName, null,null);
	}

	/**
	 * It sets the user ID and a user property.
	 *
	 * @param logged boolean value that indicates whether the user is logged in or not.
	 * @return The boolean value of logged.
	 */
	public boolean userAuthenticate(boolean logged) {
		String status;
		if (logged) {
			status = "true";
			ui = uuID;
		} else {
			status = "false";
		}

		mFirebaseAnalytics.setUserId(ui);
		mFirebaseAnalytics.setUserProperty("logged_in", status);
		return logged;
	}

	/**
	 * The function takes three parameters, the first one is the name of the button that was clicked, the
	 * second one is the resource ID of the button, and the third one is the text of the button. The
	 * function then creates a bundle object and a decimal format object. It then creates a bunch of
	 * strings and doubles. The function then checks if the user is logged in or not. If the user is not
	 * logged in, the user ID is set to "guest". The function then checks the name of the button that was
	 * clicked and sets the event name, event type, contact method, currency code, event value, and other
	 * parameters accordingly. The function then checks if the button name contains the word "search". If
	 * it does, the function checks if the button name contains the word "error". If it does, the function
	 * sets the error message, event name, event type, and other parameters accordingly. The function then
	 * checks if the button name contains the word "OK".
	 *
	 * @param click The button text.
	 * @param resourceId The resource ID of the button that was clicked.
	 * @param text The text of the button that was clicked.
	 */
	public void userInteraction(@NonNull String click, String resourceId, String text) {

		Bundle params = new Bundle();
		DecimalFormat df = new DecimalFormat("0.00");
		Button vbtn = findViewById(R.id.videoButton);

		final String[] en = {""}; // Event name
		final String[] et = {""}; // Event type
		String cm = ""; // Contact method
		String cc = "USD"; // Currency code
		String etConversion = "conversion"; // Event types
		String etInteraction = "user interaction"; // Event types
		String etTool = "content tool"; // Event types
		String message = ""; // Error message
		String st; // Search term
		String screenName; // Screen view name
		String desc = null; // Resource desc when not button (content tool)
		String vp = "Any Video Player"; // Video player
		String vt = "Walk in The Clouds";  // Video title
		String vu =  "/videos/phantom"; // Video url
		int vd = vduration; // Video duration
		final int[] vct = new int[1]; // Video current time
		final String[] vs = {null}; // Video status
		double ev = 0; // Event value

		if (Boolean.FALSE.equals(logged)) ui = "guest";

		switch (click) {
			case "Phone":
				cm = "phone";
				ev = 25;
				break;
			case "Email":
				cm = "email";
				ev = 50;
				break;
			case "Purchase":
				if (Boolean.TRUE.equals(logged)) {
					en[0] = "purchase";
					et[0] = etConversion;
					int qty = rand.nextInt(100) + 1;
					double price = Double.parseDouble(df.format(rand.nextInt(20) + rand.nextFloat()));
					ev = Double.parseDouble(df.format(qty * price)) * 2;
					double tax = Double.parseDouble(df.format(ev * 0.07));
					double shipping = Double.parseDouble(df.format(ev * 0.12));
					String transID = "T-" + rand.nextInt(10000) + 1;
					// Item parameters
					Bundle item1 = new Bundle();
					item1.putString(FirebaseAnalytics.Param.ITEM_ID, ("SKU_" + rand.nextInt(1000) + 1));
					item1.putString(FirebaseAnalytics.Param.ITEM_NAME, "jeggings");
					item1.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "pants");
					item1.putString(FirebaseAnalytics.Param.ITEM_VARIANT, "black");
					item1.putString(FirebaseAnalytics.Param.ITEM_BRAND, "My Self");
					item1.putLong(FirebaseAnalytics.Param.QUANTITY, qty);
					item1.putDouble(FirebaseAnalytics.Param.PRICE, price);
					item1.putLong(FirebaseAnalytics.Param.INDEX, 1);
					Bundle item2 = new Bundle();
					item2.putString(FirebaseAnalytics.Param.ITEM_ID, ("SKU_" + rand.nextInt(1000) + 1));
					item2.putString(FirebaseAnalytics.Param.ITEM_NAME, "boots");
					item2.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "shoes");
					item2.putString(FirebaseAnalytics.Param.ITEM_VARIANT, "brown");
					item2.putString(FirebaseAnalytics.Param.ITEM_BRAND, "My Self");
					item2.putLong(FirebaseAnalytics.Param.QUANTITY, qty);
					item2.putDouble(FirebaseAnalytics.Param.PRICE, price);
					item2.putLong(FirebaseAnalytics.Param.INDEX, 2);
					// Purchase event parameters
					params.putString(FirebaseAnalytics.Param.TRANSACTION_ID, transID);
					params.putString(FirebaseAnalytics.Param.AFFILIATION, "My Great Store");
					params.putString(FirebaseAnalytics.Param.CURRENCY, cc);
					params.putDouble(FirebaseAnalytics.Param.VALUE, ev);
					params.putDouble(FirebaseAnalytics.Param.TAX, tax);
					params.putDouble(FirebaseAnalytics.Param.SHIPPING, shipping);
					params.putString(FirebaseAnalytics.Param.COUPON, "SUMMER_FUN");
					params.putParcelableArray(FirebaseAnalytics.Param.ITEMS, new Parcelable[]{item1, item2});
				} else {
					en[0] = "purchase_error";
					et[0] = etTool;
					message = "ERROR: Please Sign In!";
					Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
				}
				break;
			case "Sign In":
				if (Boolean.TRUE.equals(logged)) {
					en[0] = "login_error";
					et[0] = etTool;
					message = "ERROR: You already Sign In!";
					Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
				} else {
					en[0] = "login";
					et[0] = etConversion;
					logged = userAuthenticate(true);
					params.putString(FirebaseAnalytics.Param.METHOD, "google");
				}
				break;
			case "Sign Out":
				if (Boolean.FALSE.equals(logged)) {
					en[0] = "logout_error";
					et[0] = etTool;
					message = "ERROR: You already Sign Out!";
					Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
				} else {
					en[0] = "logout";
					et[0] = etInteraction;
					logged = userAuthenticate(false);
				}
				break;
			case "Search":
				en[0] = "search_dialog_opened";
				et[0] = etInteraction;
				break;
			case "introTextView":
				desc = text;
				en[0] = "data_display_clear";
				et[0] = etInteraction;
				break;
			case "Outbound Link":
				if (Boolean.TRUE.equals(logged)) {
					en[0] = "outbound_link";
					et[0] = etInteraction;
					boolean ol = true;

					params.putString("link_url", url);
					params.putString("link_text", text);
					params.putString("link_id", resourceId);
					params.putBoolean("outbound", ol);

					goToURL(url);
				} else {
					en[0] = "outbound_link_error";
					et[0] = etTool;
					message = "ERROR: Please Sign In!";
					Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
				}
				break;
			case "App Init":
				screenName = "Home Screen";
				en[0] = "screen_view";
				et[0] = etTool;

				params.putString(FirebaseAnalytics.Param.SCREEN_NAME, screenName);
				params.putString(FirebaseAnalytics.Param.SCREEN_CLASS, "MainActivity");
				params.putString("app_name", "AnalyticsMobile");
				params.putString("app_desc", "Mobile Analytics Implementation Playground");
				params.putString("app_author", "Arturo Santiago-Rivera");
				params.putString("author_email", "asantiago@arsari.com");
				params.putString("content_group", "Implementation");
				params.putString("content_type", "Playground");
				params.putString("language_code", "en-US");
				break;
			case "Video":
				vbtn.setText(R.string.video_play);
				vbtn.setBackgroundColor(getColor(R.color.red));
				en[0] = "video_start";
				vplay = true;
				vstop = false;
				break;
			case "Video Playing":
				vbtn.setText(R.string.video_btn);
				vbtn.setBackgroundColor(getColor(R.color.purple_500));
				en[0] = "video_stop";
				vstop = true;
				vplay = false;
				break;
			default:
		}

		if (click.equals("Phone") || click.equals("Email")) {
			en[0] = "generate_lead";
			et[0] = etConversion;
			params.putString("contact_method", cm);
			params.putString(FirebaseAnalytics.Param.CURRENCY, cc);
			params.putDouble(FirebaseAnalytics.Param.VALUE, ev);
		}

		Pattern searchPattern = Pattern.compile(getString(R.string.search_btn), Pattern.CASE_INSENSITIVE);
		Matcher searchMatcher = searchPattern.matcher(click);
		boolean searchFound = searchMatcher.find();
		if (searchFound) {
			if (click.contains("Error")) {
				if (click.contains("PII")) {
					message = "ERROR: No PII allowed as keyword!";
				} else {
					message = "ERROR: Input can't be empty!";
				}
				en[0] = "search_error";
				et[0] = etTool;
				Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
			}

			if (click.contains("OK")) {
				en[0] = "search";
				et[0] = etInteraction;
				st = searchTerm;

				params.putString(FirebaseAnalytics.Param.SEARCH_TERM, st);
			}

			if (click.contains("Cancel")) {
				en[0] = "search_dialog_closed";
				et[0] = etInteraction;
			}
		}

		if (en[0].contains("error")) {
			params.putString("error_message", message);
			params.putBoolean("toast_impression", true);
		}

		Pattern videoPattern = Pattern.compile(getString(R.string.video_btn), Pattern.CASE_INSENSITIVE);
		Matcher videoMatcher = videoPattern.matcher(click);
		boolean videoFound = videoMatcher.find();
		if (videoFound) {
			if (Boolean.TRUE.equals(vstop)) {
				vs[0] = "Stop";
			} else {
				vs[0] = "Play";
			}
			et[0] = etInteraction;
			vct[0] = vprogress;
			vd = vduration;

			params.putLong("video_duration", vd);
			params.putLong("video_current_time", vct[0]);
			params.putString("video_percent", milestone + "%");
			params.putString("video_status", vs[0]);
			params.putString("video_provider", vp);
			params.putString("video_title", vt);
			params.putString("video_url", vu);
		}

		if (Boolean.TRUE.equals(vplay)) {
			Timer timer = new Timer();
			Bundle progressParams = new Bundle();
			int finalVd = vd;
			timer.scheduleAtFixedRate(new TimerTask() {
				@Override
				public void run() {
					if (milestone < 100 && Boolean.FALSE.equals(vstop)) {
						Pattern progressPattern = Pattern.compile("10|25|50|75|90");
						Matcher progressMatcher = progressPattern.matcher(String.valueOf(milestone));
						boolean progressFound = progressMatcher.find();
						if (progressFound) {
							et[0] = etTool;
							vs[0] = "Progress " + milestone + "%";
							vct[0] = vprogress;

							progressParams.putLong("video_duration", finalVd);
							progressParams.putLong("video_current_time", vct[0]);
							progressParams.putString("video_percent", milestone + "%");
							progressParams.putString("video_status", vs[0]);
							progressParams.putString("video_provider", vp);
							progressParams.putString("video_title", vt);
							progressParams.putString("video_url", vu);
							progressParams.putString("event_type", et[0]);
							progressParams.putString("event_timestamp", String.valueOf(new Date().getTime())); // milliseconds
							progressParams.putString("custom_timestamp", timeStamp()); // ISO 8061
							progressParams.putString("custom_user_id", ui);
							mFirebaseAnalytics.logEvent(en[0], progressParams);
							displayEvent(progressParams, "Video Playing", "video_progress");
						}
						milestone += 5;
						vprogress += 15;
					}
					if (milestone == 100) {
						et[0] = etTool;
						vs[0] = "Complete";
						vct[0] = vprogress;
						progressParams.putLong("video_duration", finalVd);
						progressParams.putLong("video_current_time", vct[0]);
						progressParams.putString("video_percent", milestone + "%");
						progressParams.putString("video_status", vs[0]);
						progressParams.putString("video_provider", vp);
						progressParams.putString("video_title", vt);
						progressParams.putString("video_url", vu);
						progressParams.putString("event_type", et[0]);
						progressParams.putString("event_timestamp", String.valueOf(new Date().getTime())); // milliseconds
						progressParams.putString("custom_timestamp", timeStamp()); // ISO 8061
						progressParams.putString("custom_user_id", ui);
						mFirebaseAnalytics.logEvent(en[0], progressParams);
						displayEvent(progressParams, "Video End", "video_complete");
						videoHandler.obtainMessage(1).sendToTarget();
						milestone = 0;
						vprogress = 0;
						vstop = true;
						vplay = false;
						timer.cancel();
					}
				}
			}, 0, 3000);
		}

		// Global parameters
		params.putString("event_type", et[0]);
		params.putString("button_text", desc == null ? click : desc);
		params.putString("resource_id", resourceId);
		params.putString("event_timestamp", String.valueOf(new Date().getTime())); // milliseconds
		params.putString("custom_timestamp", timeStamp()); // ISO 8061
		params.putString("custom_user_id", ui);

		switch (en[0]) {
			case "generate_lead":
				mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.GENERATE_LEAD, params);
				break;
			case "purchase":
				mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.PURCHASE, params);
				break;
			case "login":
				mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN, params);
				break;
			case "search":
				mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SEARCH, params);
				break;
			case "screen_view":
				mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, params);
				break;
			default:
				mFirebaseAnalytics.logEvent(en[0], params);
				break;
		}

		displayEvent(params, click, en[0]);
	}

	/**
	 * It takes the parameters of the event, the click and the event name, and displays them in the
	 * TextView
	 *
	 * @param params The parameters of the event.
	 * @param click The name of the click event.
	 * @param en The event name.
	 */
	public void displayEvent(Bundle params, String click, String en) {

		JSONObject json = convertBundleToJson(params);

		try {
			String eventLog = getString(R.string.display_separator, eventsCounter) + "Click -> " + click + "\nEvent log -> " + en.toUpperCase() + "\n===> parameters (json) <===\n" + json.toString(2) + "\n\n";
			builder.append(eventLog);
			int length = builder.length();
			builder.setSpan(new ForegroundColorSpan(Color.GRAY), 0, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			builder.setSpan(new StyleSpan(Typeface.NORMAL), 0, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			builder.setSpan(new ForegroundColorSpan(Color.BLUE), length - eventLog.length(), length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			builder.setSpan(new StyleSpan(Typeface.BOLD), length - eventLog.length(), length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			displayJSON.setText(builder);

			displayJSON.post(() -> {
				int scrollAmount = displayJSON.getLayout().getLineTop(displayJSON.getLineCount()) - displayJSON.getHeight();
				displayJSON.scrollTo(0, Math.max(scrollAmount, 0));
			});
		} catch(JSONException e) {
			Log.w(TAG, e);
			displayJSON.setText((CharSequence) e);
		}
		eventsCounter++;
	}

	/**
	 * It takes a bundle and converts it to a JSONObject
	 *
	 * @param bundle The bundle that you want to convert to JSON.
	 * @return A JSONObject
	 */
	public JSONObject convertBundleToJson(Bundle bundle) {

		JSONObject json = new JSONObject();
		Set<String> keys = bundle.keySet();

		for (String key : keys) {
			try {
				if (bundle.get(key) != null && Objects.equals(bundle.get(key).getClass().getName(), "[Landroid.os.Parcelable;")) {
					Log.w(TAG, "Converting Parcelable Bundle to JSON");
					JSONArray arr = new JSONArray();
					Parcelable[] parcelableArray = (Parcelable[]) bundle.get(key);
					for (Parcelable parcelable : parcelableArray) {
						Bundle nestedParcelable = (Bundle) parcelable;
						arr.put(convertBundleToJson(nestedParcelable));
					}
					json.put(key, arr);
				} else if (bundle.get(key) != null && Objects.equals(bundle.get(key).getClass().getName(), "android.os.Bundle")) {
					Log.w(TAG, "Converting Bundle to JSON");
					Bundle nestedBundle = (Bundle) bundle.get(key);
					json.put(key, convertBundleToJson(nestedBundle));
				} else {
					json.put(key, JSONObject.wrap(bundle.get(key)));
				}
			} catch(JSONException e) {
				Log.w(TAG, e);
				displayJSON.setText((CharSequence) e);
			}
		}

		return json;
	}

	/**
	 * This function returns a string that represents the current time in the format of `yyyy-MM-dd'
	 * T'HH:mm:ss.S' UTC'XXX`
	 *
	 * @return A string with the current time in the format of yyyy-MM-dd' T'HH:mm:ss.S' UTC'XXX
	 */
	public String timeStamp() {
		SimpleDateFormat pattern = new SimpleDateFormat("yyyy-MM-dd' T'HH:mm:ss.S' UTC'XXX", Locale.getDefault());
		Timestamp time = new Timestamp(System.currentTimeMillis());
		return pattern.format(time);
	}

	/**
	 * This function takes a string as an argument, and then opens the default browser to the URL
	 * specified in the string.
	 *
	 * @param linkUrl The URL to redirect to.
	 */
	public void goToURL(String linkUrl) {
		Log.w(TAG, String.format("Redirecting to %s", linkUrl));
		Uri uriURL = Uri.parse(linkUrl);
		Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriURL);
		startActivity(launchBrowser);
	}
}
