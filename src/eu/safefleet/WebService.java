package eu.safefleet;

import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;

import android.widget.TextView;

public class WebService {
	private TextView tw = null;

	public WebService(TextView tw) {
		this.tw = tw;
	}

	public void start() throws Exception {
		DefaultHttpClient httpclient = new DefaultHttpClient();

		HttpPost httpost = new HttpPost(
				"http://portal.safefleet.eu/safefleet/webservice/authenticate/?username=demows&password=deadbeef2");

		HttpResponse response = httpclient.execute(httpost);
		HttpEntity entity = response.getEntity();

		tw.append("\nLogin form get: " + response.getStatusLine());
		if (entity != null) {
			entity.consumeContent();
		}
		tw.append("\nCookies:");
		List<Cookie> cookies = httpclient.getCookieStore().getCookies();
		if (cookies.isEmpty()) {
			tw.append("\nNone");
		} else {
			for (int i = 0; i < cookies.size(); i++) {
				tw.append("\n- " + cookies.get(i).toString());
			}
		}

		HttpGet httpget = new HttpGet(
				"http://portal.safefleet.eu/safefleet/webservice/get_companies/");

		Cookie c = httpclient.getCookieStore().getCookies().get(0);
		httpget.setHeader("Cookie", c.getName() + "=" + c.getValue());
		tw.append("\nrequest: " + httpget.getRequestLine());
		tw.append("\nheaders: ");
		for (Header h : httpget.getAllHeaders()) {
			tw.append("\n-" + h.toString());
		}

		response = httpclient.execute(httpget);
		entity = response.getEntity();

		tw.append("\nresponse: " + response.getStatusLine());
		if (entity != null) {
			entity.consumeContent();
		}

		// When HttpClient instance is no longer needed,
		// shut down the connection manager to ensure
		// immediate deallocation of all system resources
		httpclient.getConnectionManager().shutdown();
	}
}
