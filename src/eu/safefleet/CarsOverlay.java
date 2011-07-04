package eu.safefleet;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class CarsOverlay extends ItemizedOverlay {
	private ArrayList<OverlayItem> cars = new ArrayList<OverlayItem>();
	private Context mContext = null;

	public CarsOverlay(Drawable defaultMarker) {
		super(boundCenterBottom(defaultMarker));
	}

	public CarsOverlay(Drawable defaultMarker, Context context) {
		super(boundCenterBottom(defaultMarker));
		mContext = context;
	}

	@Override
	protected OverlayItem createItem(int i) {
		return cars.get(i);
	}

	@Override
	public int size() {
		return cars.size();
	}

	public void addOverlay(OverlayItem overlay) {
		cars.add(overlay);
		populate();
	}

	@Override
	protected boolean onTap(int index) {
		OverlayItem item = cars.get(index);
		AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
		dialog.setTitle(item.getTitle());
		dialog.setMessage(item.getSnippet());
		dialog.show();
		return true;
	}

}
