package eu.safefleet;

import android.app.Activity;
import android.os.Bundle;
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}