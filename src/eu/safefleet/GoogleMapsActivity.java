package eu.safefleet;

import java.io.IOException;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class GoogleMapsActivity extends MapActivity {
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);
		MapView mapView = (MapView) findViewById(R.id.mapview);
		mapView.getController().setZoom(18);
		mapView.setBuiltInZoomControls(true);
		List<Overlay> mapOverlays = mapView.getOverlays();
		Drawable drawable = this.getResources().getDrawable(
				R.drawable.androidmarker);
		CarsOverlay itemizedoverlay = new CarsOverlay(drawable, this);

		String vehicleId = getIntent().getExtras().getString("vehicle_id");
		String vehicleName = getIntent().getExtras().getString("vehicle_name");
		try {
			CarInfo carInfo = WebService.getInstance().getVehicleDynamicInfo(
					vehicleId, vehicleName);
			GeoPoint point = new GeoPoint((int) (carInfo.getLat() * 1E6),
					(int) (carInfo.getLng() * 1E6));
			OverlayItem overlayitem = new OverlayItem(point, "Hola, Mundo!",
					vehicleName);
			mapView.getController().setCenter(point);
			itemizedoverlay.setCarOverlay(overlayitem);
			mapOverlays.add(itemizedoverlay);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}