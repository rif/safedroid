package eu.safefleet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

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

	public ArrayList<CarInfo> getCars() throws ClientProtocolException,
			IOException {
		HttpGet httpget = new HttpGet(SERVER + "/get_vehicles/");

		HttpResponse response = httpclient.execute(httpget);
		String dataAsString = getResponseAsString(response);
		// Load the requested page converted to a string into a JSONObject.
		ArrayList<CarInfo> cars = new ArrayList<CarInfo>();
		try {
			JSONObject respJson = new JSONObject("{'result' :" + dataAsString
					+ "}");

			JSONArray result = respJson.getJSONArray("result");
			for (int i = 0; i < result.length(); i++) {
				CarInfo info = WebService.getInstance().getVehicleDynamicInfo(
						result.getJSONObject(i).getString("vehicle_id"),
						result.getJSONObject(i).getString("name"));
				cars.add(info);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return cars;
	}

	public CarInfo getVehicleDynamicInfo(String vehicleId, String number)
			throws ClientProtocolException, IOException, JSONException {
		HttpGet httpget = new HttpGet(SERVER
				+ "/get_vehicle_dynamic_info/?vehicle_id=" + vehicleId);

		HttpResponse response = httpclient.execute(httpget);
		String dataAsString = getResponseAsString(response);
		// Load the requested page converted to a string into a JSONObject.
		JSONObject object = new JSONObject(dataAsString);
		return new CarInfo(vehicleId,number, object.getDouble("lat"), object.getDouble("lng"), object.getInt("speed"));
	}

	private String getResponseAsString(HttpResponse response)
			throws IOException {
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
