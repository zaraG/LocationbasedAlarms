package zarag.locationbasedalarms;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;


/**
 * The LocationService provides all the functionality to update the current location of the user
 * and to call the alarm, if the user is close enough to his/her duration.
 *
 * */
public class LocationService extends Service {

    /** Minimum distance and time.
     * TODO - if time let the user edit this values.
     * Important for different travelling choices,
     * e.g. by train/ car, bike or foot. */

    // 0.5 meters
    public static final int DEFAULT_DISTANCE = 500;

    // 5 minutes
    public static final int DEFAULT_TIME = 5000 * 60;

    // the current location
    private static Location currentLocation;

    private LocationListener locationListener;
    private LocationManager locationManager;

    public LocationService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return new LocationBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        currentLocation = new Location(locationManager.GPS_PROVIDER);

        callLocationListener();

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, DEFAULT_TIME,
                DEFAULT_DISTANCE, locationListener);

        // Little git test

        return START_NOT_STICKY;
    }

    private void callLocationListener() {
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                currentLocation = location;
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                // auto-generated, can not be deleted
            }

            @Override
            public void onProviderEnabled(String provider) {
                // auto-generated, can not be deleted
            }

            @Override
            public void onProviderDisabled(String provider) {
                // auto-generated, can not be deleted
            }
        };
    }

    protected Location getCurrentLocation() {
        return currentLocation;
    }

    public class LocationBinder extends Binder {
        public LocationService getLocationService() {
            return LocationService.this;
        }
    }
}
