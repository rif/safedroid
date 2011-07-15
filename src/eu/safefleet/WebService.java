package eu.safefleet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

	public JSONArray getCars() throws ClientProtocolException, IOException {
		HttpGet httpget = new HttpGet(SERVER + "/get_vehicles/");

		HttpResponse response = httpclient.execute(httpget);
		String dataAsString = getResponseAsString(response);
		// Load the requested page converted to a string into a JSONObject.
		JSONArray result = null;
		try {
			JSONObject respJson = new JSONObject("{'result' :" + dataAsString
					+ "}");

			result = respJson.getJSONArray("result");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return result;
	}

	public JSONObject get_vehicle_dynamic_info(String vehicleId)
			throws ClientProtocolException, IOException, JSONException {
		HttpGet httpget = new HttpGet(SERVER + "/get_vehicle_dynamic_info/?vehicle_id=" + vehicleId);

		HttpResponse response = httpclient.execute(httpget);
		String dataAsString = getResponseAsString(response);
		// Load the requested page converted to a string into a JSONObject.
		return new JSONObject(dataAsString);
	}
	
	private String getResponseAsString(HttpResponse response) throws IOException{
		HttpEntity entity = response.getEntity();

		InputStream inputStream = entity.getContent();
		ByteArrayOutputStream content = new ByteArrayOutputStream();
		int readBytes = 0;
		byte[] sBuffer = new byte[512];
		while ((readBytes = inputStream.read(sBuffer)) != -1) {
			content.write(sBuffer, 0, readBytes);
		}
		// Close the stream.
		inputStream.close();
		if (entity != null) {
			entity.consumeContent();
		}
		
		// Return result from buffered stream
		return new String(content.toByteArray());
	}
}
