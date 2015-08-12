package zarag.locationbasedalarms;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;


/**
 * Created by zara and javeed on 11.08.2015.
 *
 * The AlarmActivity is used for the alarm view, we will not use the AlarmClock class, because it is
 * activating the alarm by a time instant of a specific event. In out case, the user is close enough
 * to his/her destination.
 * */
public class AlarmActivity extends Activity {

    public static final String WAKE_UP_TEXT = "Wake up you are almost at your destination.";

    private TextView wakeUpTxt;

    private MediaPlayer mediaPlayer;

    private Switch turnOffSwtch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        wakeUpTxt = (TextView)findViewById(R.id.wakeUpTxt);
        wakeUpTxt.setText(WAKE_UP_TEXT);

        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.wakemeup);
        mediaPlayer.start();

        turnOffSwtch = (Switch)findViewById(R.id.turnOffSwtch);
        turnOffSwtch.setChecked(true);
        turnOffSwtch.setTextOn("on");
        turnOffSwtch.setTextOff("off");

        turnOffSwtch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    mediaPlayer.stop();

                    startActivity(new Intent(Intent.ACTION_MAIN).
                            addCategory(Intent.CATEGORY_HOME).
                            setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }
            }
        });
    }
}
