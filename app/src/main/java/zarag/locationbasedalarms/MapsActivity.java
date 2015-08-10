package zarag.locationbasedalarms;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by zara and javeed on 09.08.2015.
 *
 * The (Google-) MapsActivity class is implemented with the help of the Google Maps Android API v2
 * and provides all functionality to show the Map, to zoom to a specific location and to "safe" it.
 */
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    // the map for itself - might be null if Google Play services APK is not available
    private GoogleMap mMap;

    // marker for specific locations
    private Marker marker;

    private SupportMapFragment mMapFragment;

    private LocationServiceConnection sConnection;

    private LocationService sService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mMapFragment = SupportMapFragment.newInstance();
        mMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mMapFragment.getMapAsync(this);

        sConnection = new LocationServiceConnection();
        sService = sConnection.getSService();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                // delete old marker to avoid multiple selection
                if (marker != null) {
                    marker.remove();
                }
                marker = mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .draggable(true));
                marker.setDraggable(true);
            }
        });
    }
}
