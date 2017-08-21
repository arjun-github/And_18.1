package com.acadgild.assignment_181;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.widget.TextView;

/**
 * The MainActivity extends Activity and instantiates the necessary arrtibutes
 */
public class MainActivity extends Activity {

    TextView batteryPercent, ChargingState;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        batteryPercent = (TextView) findViewById(R.id.tv_percentage);
        ChargingState = (TextView) findViewById(R.id.tv_state);
        getBatteryPercentage();
        if(isPhonePluggedIn(getApplicationContext()).compareToIgnoreCase("yes")== 0)
        {
            ChargingState.setText("yes");
        }
        else
            ChargingState.setText("no");

    }

    //This method will get the Battery Percentage
    private void getBatteryPercentage() {
        BroadcastReceiver batteryLevel = new BroadcastReceiver() {

            public void onReceive(Context context, Intent intent) {
                context.unregisterReceiver(this);
                int currentLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
                int level= -1;
                if (currentLevel >= 0 && scale > 0) {
                    level = (currentLevel * 100) / scale;
                }
                batteryPercent.setText(level + "%");
            }
        };

        IntentFilter batteryLevelFilter = new IntentFilter(
                Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(batteryLevel, batteryLevelFilter);

    }

    //This method will return whether ot not the phone is plugged into the powerplug
    //using BatteryManager
    public static String isPhonePluggedIn(Context context) {
        boolean charging = false;
        String result = "No";
        final Intent batteryIntent = context.registerReceiver(null,
                new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int status = batteryIntent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean batteryCharge = status == BatteryManager.BATTERY_STATUS_CHARGING;

        int chargePlug = batteryIntent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
        boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;

        if (batteryCharge)
            charging = true;
        if (usbCharge)
            charging = true;
        if (acCharge)
            charging = true;

        if (charging){
            result = "Yes";

        }
        return result;
    }
}