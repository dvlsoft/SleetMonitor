package vladvin.sleetmonitor.sensor_tracker;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.util.concurrent.ConcurrentLinkedQueue;
import vladvin.sleetmonitor.DataExchange.DataBroker;

public class SensorTracker implements SensorEventListener {
    private final static String TAG = "SensorTracker";

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private float accelerometerMaxRange;

    private ConcurrentLinkedQueue<SensorData> measurements;
    private static final long SEND_DATA_TIME_DIFF = 1000L;   // in ms
    private long lastSendingDataTimestamp = 0L;

    private Activity activity;
    private LocationTracker locationTracker;
    private DataBroker dataBroker;

    public SensorTracker(Activity activity, LocationTracker locationTracker, DataBroker dataBroker) {
        this.activity = activity;
        this.sensorManager = (SensorManager)
                activity.getApplicationContext().getSystemService(Context.SENSOR_SERVICE);
        this.accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        this.accelerometerMaxRange = accelerometer.getMaximumRange();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_FASTEST);

        this.locationTracker = locationTracker;
        this.measurements = new ConcurrentLinkedQueue<>();
        this.dataBroker = dataBroker;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (locationTracker == null) {
            return;
        }

        Location lastLocation = locationTracker.getLastKnownLocation();
        if (lastLocation == null) {
            return;
        }

        SensorData sensorData = new SensorData(
                accelerometerMaxRange,
                sensorEvent.values[0],
                sensorEvent.values[1],
                sensorEvent.values[2],
                lastLocation.getLatitude(),
                lastLocation.getLongitude(),
                sensorEvent.timestamp);

        measurements.add(sensorData);

        if (sensorEvent.timestamp - lastSendingDataTimestamp >
                SEND_DATA_TIME_DIFF * 1000000L) {
            if (dataBroker != null) {
                // TODO: Send data to data broker
                for (SensorData data : measurements) {
                    Log.w(this.getClass().getName(), data.toString());
                }
                lastSendingDataTimestamp = sensorEvent.timestamp;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}