package eu.safefleet;

import java.io.IOException;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends Activity {
	private static final String TAG = "LoginActivity";
	public static final String PREFS_NAME = "SafeDroidPrefs";
	private static final String PREF_USERNAME = "username";
	private static final String PREF_PASSWORD = "password";
	private static final String PREF_REMEMBER = "remember";
	private EditText userEdit;
	private EditText passwordEdit;
	private Button loginButton;
	private CheckBox rememberCheck;
	private TextView resultText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		userEdit = (EditText) findViewById(R.id.userentry);
		passwordEdit = (EditText) findViewById(R.id.passwordentry);
		loginButton = (Button) findViewById(R.id.loginbutton);
		rememberCheck = (CheckBox) findViewById(R.id.rememberBox);
		resultText = (TextView) findViewById(R.id.resultlabel);

		SharedPreferences pref = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
		String username = pref.getString(PREF_USERNAME, null);
		String password = pref.getString(PREF_PASSWORD, null);
		boolean checked= pref.getBoolean(PREF_REMEMBER, false);

		if (username != null) {
			userEdit.setText(username);
		}

		if (password != null) {
			passwordEdit.setText(password);
		}
		rememberCheck.setChecked(checked);

		loginButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
					boolean success;
					try {
						success = WebService.getInstance().login(
								userEdit.getText().toString(),
								passwordEdit.getText().toString());
						resultText.setText(success?R.string.loginsuccessful:R.string.loginfailed);
						if (success){
							finish();
						}
					} catch (ClientProtocolException e) {
						resultText.setText(R.string.servererror);
					} catch (IOException e) {
						resultText.setText(R.string.nointernet);
					}
				saveCredentials();
			}
		});
	}

	public void test() throws Exception {
		resultText.append("\nStarting...\n");
		DefaultHttpClient httpclient = new DefaultHttpClient();

		HttpPost httpost = new HttpPost(
				"http://portal.safefleet.eu/safefleet/webservice/authenticate/?username="
						+ userEdit.getText() + "&password="
						+ passwordEdit.getText());

		HttpResponse response = httpclient.execute(httpost);
		HttpEntity entity = response.getEntity();

		resultText.append("\nLogin form get: " + response.getStatusLine());
		if (entity != null) {
			entity.consumeContent();
		}
		resultText.append("\nCookies:");
		List<Cookie> cookies = httpclient.getCookieStore().getCookies();
		if (cookies.isEmpty()) {
			resultText.append("\nNone");
		} else {
			for (int i = 0; i < cookies.size(); i++) {
				resultText.append("\n- " + cookies.get(i).toString());
			}
		}

		HttpGet httpget = new HttpGet(
				"http://portal.safefleet.eu/safefleet/webservice/get_companies/");

		Cookie c = httpclient.getCookieStore().getCookies().get(0);
		httpget.setHeader("Cookie", c.getName() + "=" + c.getValue());
		resultText.append("\nrequest: " + httpget.getRequestLine());
		resultText.append("\nheaders: ");
		for (Header h : httpget.getAllHeaders()) {
			resultText.append("\n-" + h.toString());
		}

		response = httpclient.execute(httpget);
		entity = response.getEntity();

		resultText.append("\nresponse: " + response.getStatusLine());
		if (entity != null) {
			entity.consumeContent();
		}

		// When HttpClient instance is no longer needed,
		// shut down the connection manager to ensure
		// immediate deallocation of all system resources
		httpclient.getConnectionManager().shutdown();
	}

	private void saveCredentials() {
		if (rememberCheck.isChecked()) {
			Log.d(TAG, "Saving credential");
			getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
					.edit()
					.putString(PREF_USERNAME, userEdit.getText().toString())
					.putString(PREF_PASSWORD, passwordEdit.getText().toString())
					.putBoolean(PREF_REMEMBER, true)
					.commit();
		} else {
			Log.d(TAG, "Clearing credential");
			getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit()
					.putString(PREF_USERNAME, "").putString(PREF_PASSWORD, "")
					.putBoolean(PREF_REMEMBER, false)
					.commit();
		}
	}
}
