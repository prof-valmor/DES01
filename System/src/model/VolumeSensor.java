package model;

/**
 * Sensor that observes the volume of a tank.
 */
public interface VolumeSensor {
    void updateCurrentMeasurement(double volume);
    double getCurrentMeasurement();
}
