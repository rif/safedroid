package eu.safefleet;

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
				if (locationView != null) {
					locationView.setText("Location: " + "Arad");
				}

				if (statusView != null) {
					statusView.setText("Status: " + "Parked");
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return vi;
	}
}