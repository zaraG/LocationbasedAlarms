package zarag.locationbasedalarms;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

/**
 * Created by zara on 09.08.2015.
 *
 * The LocationServiceConnection class is responsible for binding the LocationService with the
 * Activities.
 *
 */
public class LocationServiceConnection implements ServiceConnection {

    private boolean sBound = false;
    private  LocationService sService = new LocationService();

    public LocationServiceConnection() {

    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        LocationService.LocationBinder binder = (LocationService.LocationBinder) service;
        sService = binder.getLocationService();
        sBound = true;
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        sBound = false;
        sService = null;
    }

    protected boolean getSBound() {
        return sBound;
    }

    protected LocationService getSService() {
        return sService;
    }
}
