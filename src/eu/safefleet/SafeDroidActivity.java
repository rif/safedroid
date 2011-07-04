package eu.safefleet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class SafeDroidActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		TextView tw = (TextView) findViewById(R.id.textview);
		WebService ws = new WebService(tw);

		try {
			ws.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Button startButton = (Button) findViewById(R.id.startButton);
		final Intent listIntent = new Intent(this, CarListActivity.class);
		startButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				startActivityForResult(listIntent, 0);
			}
		});
		// Intent mapIntent = new Intent(this, GoogleMapsActivity.class);
		// startActivity(mapIntent);
	}
}