package eu.safefleet;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.client.ClientProtocolException;

import utils.Dialogs;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class CarListActivity extends ListActivity {
	private static final String TAG = "ListActivity";
	private static int COUNTER_MAX = 10;
	private Handler handler = null;
	private CarListAdapter carListAdapter = null;
	private ProgressDialog dialog = null;
	private int counter = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ListView lv = getListView();
		lv.setTextFilterEnabled(true);
		carListAdapter = new CarListAdapter(CarListActivity.this,
				new ArrayList<CarInfo>());
		setListAdapter(carListAdapter);
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent mapIntent = new Intent(getApplicationContext(),
						GoogleMapsActivity.class);
				CarInfo ci = (CarInfo) carListAdapter.getItem(position);
				mapIntent.putExtra("vehicle_id", ci.getId());
				mapIntent.putExtra("vehicle_name", ci.getName());
				startActivity(mapIntent);
			}
		});
		handler = new Handler();
	}

	@Override
	protected void onResume() {
		super.onResume();
		dialog = ProgressDialog.show(CarListActivity.this, "", "Loading. Please wait...", true);
		Log.d(TAG, "counter: " + counter);
		if (counter-- == 0 || carListAdapter.isEmpty()) {
			Log.d(TAG, "updating!");
			counter = COUNTER_MAX;
			new Thread(carUpdaterRunnable).start();
		}
	}

	private Runnable carUpdaterRunnable = new Runnable() {
		public void run() {			
			try {				
				final ArrayList<CarInfo> carList = WebService.getInstance()
						.getCars();
				if (carList == null || carList.isEmpty()) {
					// connected to server but could not obtain result: must
					// login
					Intent loginIntent = new Intent(getApplicationContext(),
							LoginActivity.class);
					startActivity(loginIntent);
				} else {
					handler.post(new Runnable() {
						public void run() {
							carListAdapter.setData(carList);
							carListAdapter.notifyDataSetChanged();
						}
					});
				}
			} catch (ClientProtocolException e) {
				handler.post(new Runnable() {
					public void run() {
						Dialogs.showExitRety(CarListActivity.this,
								R.string.servererror);
					}
				});

			} catch (IOException e) {
				handler.post(new Runnable() {
					public void run() {
						Dialogs.showExitRety(CarListActivity.this,
								R.string.nointernet);
					}
				});
			}
			handler.post(new Runnable() {
				public void run() {
					dialog.dismiss();
				}
			});
		}
	};
}
