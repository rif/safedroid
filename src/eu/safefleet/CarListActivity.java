package eu.safefleet;

import java.io.IOException;
import java.util.List;

import org.apache.http.client.ClientProtocolException;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class CarListActivity extends ListActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		String[] countries = getResources().getStringArray(
				R.array.countries_array);
		setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item,
				countries));

		ListView lv = getListView();
		lv.setTextFilterEnabled(true);

		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// When clicked, show a toast with the TextView text
				// Toast.makeText(getApplicationContext(), ((TextView)
				// view).getText(), Toast.LENGTH_SHORT).show();
				Intent mapIntent = new Intent(getApplicationContext(),
						GoogleMapsActivity.class);
				startActivity(mapIntent);
			}
		});
		try {
			List<String> carList = WebService.getInstance().getCars();
			if (carList.isEmpty()) {
				Intent mapIntent = new Intent(getApplicationContext(),
						LoginActivity.class);
				startActivity(mapIntent);
			}
		} catch (ClientProtocolException e) {
			Toast.makeText(getApplicationContext(), R.string.servererror,
					Toast.LENGTH_SHORT).show();
		} catch (IOException e) {
			Toast.makeText(getApplicationContext(), R.string.nointernet,
					Toast.LENGTH_SHORT).show();
		}
	}
}
