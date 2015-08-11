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
 * Created by zara and javeed on 09.08.2015.
 *
 * The LocationService provides all the functionality to update the current location of the user
 * and to call the alarm, if the user is close enough to his/her duration.
 *
 * */
public class LocationService extends Service {

    /** Minimum distance and time.
     * TODO - if time let the user edit this values.
     * Important for different travelling choices,
     * e.g. by train/ car, bike or foot. */

    // 0.5 km
    public static final int DEFAULT_DISTANCE = 10;

    // 5 minutes
    public static final int DEFAULT_TIME = 1000;//5000 * 60;

    // the current location
    private Location currentLocation, destination;

    private LocationListener locationListener;
    private LocationManager locationManager;

    public LocationService() {


    }

    public class LocationBinder extends Binder {
        public LocationService getLocationService() {
            return LocationService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new LocationBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        currentLocation = new Location(LocationManager.GPS_PROVIDER);
        destination = null;

        callLocationListener();

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, DEFAULT_TIME,
                DEFAULT_DISTANCE, locationListener);

        return START_NOT_STICKY;
    }

    private void callLocationListener() {
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                currentLocation = location;

                // if close by destination start the alarm
                if(currentLocation.distanceTo(destination) <= 500) {
                    startActivity(new Intent(getApplicationContext(), AlarmActivity.class));
                }
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

    public Location getDestination() {
        return destination;
    }

    public void setDestination(Location destination) {
        this.destination = destination;
    }


}
