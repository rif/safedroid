package eu.safefleet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

public class WebService {
	private static final String TAG = "WebService";
	private static final String SERVER = "http://portal.safefleet.eu/safefleet/webservice";
	public static final int RESPONSE_OK = 200;
	private DefaultHttpClient httpclient = null;
	private static WebService instance = null;

	private WebService() {
		httpclient = new DefaultHttpClient();
	}

	public static WebService getInstance() {
		if (instance == null) {
			instance = new WebService();
		}
		return instance;
	}

	public boolean login(String user, String pass)
			throws ClientProtocolException, IOException {
		HttpPost httpost = new HttpPost(SERVER + "/authenticate/?username="
				+ user + "&password=" + pass);
		HttpResponse response = httpclient.execute(httpost);
		HttpEntity entity = response.getEntity();

		if (entity != null) {
			entity.consumeContent();
		}
		Log.d(TAG, response.getStatusLine().toString());
		return response.getStatusLine().getStatusCode() == (RESPONSE_OK);
	}

	public List<String> getCars() throws ClientProtocolException, IOException {
		HttpGet httpget = new HttpGet(SERVER + "/get_companies/");

		List<Cookie> cookies = httpclient.getCookieStore().getCookies();
		if (!cookies.isEmpty()) {
			Cookie c = cookies.get(0);
			httpget.setHeader("Cookie", c.getName() + "=" + c.getValue());
		}

		HttpResponse response = httpclient.execute(httpget);
		HttpEntity entity = response.getEntity();

		if (entity != null) {
			entity.consumeContent();
		}
		return new ArrayList<String>();
	}
}
