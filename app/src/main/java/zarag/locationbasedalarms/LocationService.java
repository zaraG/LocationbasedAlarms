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
import android.widget.Toast;

/**
 * Created by zara and javeed on 09.08.2015.
 *
 * The LocationService provides all the functionality to update the current location of the user
 * and to call the alarm, if the user is close enough to his/her duration.
 *
 * */
public class LocationService extends Service {

    // 1 km
    public static final int DEFAULT_DISTANCE = 1000;

    // 3 minutes
    public static final int DEFAULT_TIME = 3000 * 60;

    // 3 km
    public static final int DEFAULT_RADIUS = 3000;

    private final IBinder mBinder;

    private Location currentLocation, destination;

    private LocationListener locationListener;
    private LocationManager locationManager;

    private static boolean alarmOn;
    private boolean startBtnClicked;

    public LocationService() {
        mBinder = new LocationBinder();
        currentLocation = null;
        destination = null;
        startBtnClicked = false;
        alarmOn = false;
    }

    public class LocationBinder extends Binder {
        public LocationService getLocationService() {
            return LocationService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        currentLocation = new Location(LocationManager.GPS_PROVIDER);

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

                if(startBtnClicked) {
                    if (currentLocation != null && destination != null && currentLocation.
                            distanceTo(destination) <= DEFAULT_RADIUS && !alarmOn) {
                        startActivity(new Intent(getApplicationContext(), AlarmActivity.class)
                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        alarmOn = true;
                        startBtnClicked = false;
                    }
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
                Toast.makeText(getApplicationContext(), "Please turn on GPS", Toast.LENGTH_SHORT).show();
            }
        };
    }

    public Location getDestination() {
        return destination;
    }

    public void setDestination(Location destination) {
        this.destination = destination;
    }

    public void setStartBtnClicked(boolean clicked) {
        startBtnClicked = clicked;
    }

    public static void setAlarmOff() {
        alarmOn = false;
    }
}
