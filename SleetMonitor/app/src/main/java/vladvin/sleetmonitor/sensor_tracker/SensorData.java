package vladvin.sleetmonitor.sensor_tracker;

public class SensorData {
    private final float accelerometerMaxRange;

    private float x;
    private float y;
    private float z;
    private double latitude;
    private double longitude;
    private long timestamp;

    public SensorData(float accelerometerMaxRange, float x, float y, float z,
                      double latitude, double longitude, long timestamp) {
        if (accelerometerMaxRange != 0.0f) {
            this.accelerometerMaxRange = accelerometerMaxRange;
        } else {
            this.accelerometerMaxRange = 1.0f;
        }
        this.x = normalize(x);
        this.y = normalize(y);
        this.z = normalize(z);
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = timestamp;
    }

    public SensorData(SensorData sensorData) {
        this.accelerometerMaxRange = sensorData.accelerometerMaxRange;
        this.x = sensorData.getX();
        this.y = sensorData.getY();
        this.z = sensorData.getZ();

    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public float getAccelerometerMaxRange() {
        return accelerometerMaxRange;
    }

    private float normalize(float value) {
        return value / accelerometerMaxRange;
    }

    @Override
    public String toString() {
        return "x: " + x + ", " +
                "y: " + y + ", " +
                "z: " + z + ", " +
                "latitude: " + latitude + ", " +
                "longitude: " + longitude + ", " +
                "timestamp: " + timestamp;
    }
}