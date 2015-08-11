package zarag.locationbasedalarms;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
    private Boolean sBound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sService = null;
        sBound = false;

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
                //startActivity(new Intent(getApplicationContext(), AlarmActivity.class));
                // TODO start the application - some how start the alarm
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

            destinationTxt.setText("Destination: "
                    + sService.getDestination().toString()
                    + "\n please press start to set up the alarm!");
        }
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
