package zarag.locationbasedalarms;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.io.IOException;
import java.util.List;

/**
 * Created by zara and javeed on 09.08.2015.
 *
 * The MainActivity
 *
 * */
public class MainActivity extends Activity {

    public static final String DEFAULT_TEXT = "Please add a location to run the application!";

    private Button addLocationBtn, startApplicationBtn;

    private TextView destinationTxt;

    private LocationService sService;
    private boolean sBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sService = null;

        addLocationBtn = (Button)findViewById(R.id.addLocation);
        addLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MapsActivity.class));
                startService(new Intent(getApplicationContext(), LocationService.class));
            }
        });

        startApplicationBtn = (Button)findViewById(R.id.startBtn);
        startApplicationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sService.setStartBtnClicked(true);
                startActivity(new Intent(Intent.ACTION_MAIN).
                        addCategory(Intent.CATEGORY_HOME).
                        setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });

        startApplicationBtn.setEnabled(false);
        destinationTxt = (TextView)findViewById(R.id.destinationTxt);
        destinationTxt.setText(DEFAULT_TEXT);
    }

    @Override
    protected void onResume() {
        super.onResume();
        bindService(new Intent(this, LocationService.class), serviceConnection, Context.BIND_AUTO_CREATE);

        if(sBound && sService.getDestination() != null) {
            startApplicationBtn.setEnabled(true);

            destinationTxt.setText(sService.getDestination().getProvider()
                    + " " + locationToAdress()
                    + "\nPlease press start to set up the alarm!");
        }
    }

    private String locationToAdress() {
        Geocoder geo = new Geocoder(this);
        List<Address> l;
        String address = "";
        try {
            l = geo.getFromLocation(sService.getDestination().getLatitude(),
                    sService.getDestination().getLongitude(), 1);
            for(int i = 0; i <  l.get(0).getMaxAddressLineIndex(); i++) {
                address += (l.get(0).getAddressLine(i) + " ");
            }
        } catch (IOException e) {
            Log.e("GeocoderError: ", "For some reason the geocoder conversion did not worked properly. "
                    + "Please check your internet connection.", e);
        }

        // in case it can´t find the specific address
        if(address.isEmpty()) {
            address = String.valueOf(sService.getDestination().getLatitude())
                    + String.valueOf(sService.getDestination().getLongitude());
        }

        return address;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (sBound) {
            unbindService(serviceConnection);
        }
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
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
    };
}
