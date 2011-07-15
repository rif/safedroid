package eu.safefleet;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;

import utils.Dialogs;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class CarListActivity extends ListActivity {
	private Handler handler = null;
	private CarListAdapter carListAdapter = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ListView lv = getListView();
		lv.setTextFilterEnabled(true);
		carListAdapter = new CarListAdapter(CarListActivity.this, new JSONArray());
		setListAdapter(carListAdapter);
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent mapIntent = new Intent(getApplicationContext(),
						GoogleMapsActivity.class);
				startActivity(mapIntent);
			}
		});
		handler = new Handler();
	}

	@Override
	protected void onResume() {
		super.onResume();
		final ProgressDialog dialog = ProgressDialog.show(CarListActivity.this,
				"", "Loading. Please wait...", true);
		Runnable runnable = new Runnable() {
			public void run() {
				try {
					final JSONArray carList = WebService.getInstance()
							.getCars();
					if (carList == null || carList.length() == 0) {
						// connected to server but could not obtain result: must
						// login
						Intent loginIntent = new Intent(
								getApplicationContext(), LoginActivity.class);
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
		new Thread(runnable).start();
	}
}
