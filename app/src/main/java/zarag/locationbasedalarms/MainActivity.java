package zarag.locationbasedalarms;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

    private LocationServiceConnection sConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sConnection = new LocationServiceConnection();

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
                // TODO start the application
            }
        });

        startApplicationBtn.setEnabled(false);
        destinationTxt = (TextView)findViewById(R.id.destinationTxt);
        destinationTxt.setText(DEFAULT_TEXT);
    }

    @Override
    protected void onResume() {
        super.onResume();
        bindService(new Intent(this, LocationService.class), sConnection, Context.BIND_AUTO_CREATE);

        if(sConnection.getSBound()
                && LocationServiceConnection.getSService().getDestination() != null) {
            startApplicationBtn.setEnabled(true);

            // TODO change output - try to get the address in clear text
            destinationTxt.setText("Destination: "
                    + LocationServiceConnection.getSService().getDestination().toString()
                    + "\n plaese press start to set up the alarm");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (sConnection.getSBound()) {
            unbindService(sConnection);
        }
    }
}
