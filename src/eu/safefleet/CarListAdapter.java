package eu.safefleet;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

class CarListAdapter extends BaseAdapter {

	private Activity activity;
	private JSONArray data;
	private static LayoutInflater inflater = null;

	public CarListAdapter(Activity a, JSONArray d) {
		activity = a;
		data = d;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		return data.length();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}
	
	public void setData(JSONArray d) {
		data = d;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		if (convertView == null) {
			vi = inflater.inflate(R.layout.row, null);
		}
		try {
			JSONObject o = data.getJSONObject(position);
			if (o != null) {
				TextView numberView = (TextView) vi
						.findViewById(R.id.carNumber);
				TextView locationView = (TextView) vi
						.findViewById(R.id.carLocation);
				TextView statusView = (TextView) vi
						.findViewById(R.id.carStatus);
				if (numberView != null) {
					numberView.setText(o.getString("name"));
				}

				try {
					JSONObject vehicle_dynamic_info = WebService.getInstance().get_vehicle_dynamic_info(o.getString("vehicle_id"));
					if (locationView != null) {
						locationView.setText("Location: " + vehicle_dynamic_info.getString("lat"));
					}

					if (statusView != null) {
						statusView.setText("Speed: " + vehicle_dynamic_info.getString("speed") + "km/h");
					}
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return vi;
	}
}