package zarag.locationbasedalarms;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * The MainActivity
 *
 * */
public class MainActivity extends Activity {

    private Button addLocation;

    private LocationServiceConnection sConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sConnection = new LocationServiceConnection();

        addLocation = (Button)findViewById(R.id.addLocation);
        addLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MapsActivity.class));
                startService(new Intent(getApplicationContext(), LocationService.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        bindService(new Intent(this, LocationService.class), sConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (sConnection.getSBound()) {
            unbindService(sConnection);
        }
    }
}
