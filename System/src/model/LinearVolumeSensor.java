package model;

public class LinearVolumeSensor implements VolumeSensor{
    private double currentVolumeReading;

    public LinearVolumeSensor() {
        this.currentVolumeReading = 0;
    }
    @Override
    public void updateCurrentMeasurement(double volume) {
        this.currentVolumeReading += volume;
    }

    @Override
    public double getCurrentMeasurement() {
        return currentVolumeReading;
    }

    public double getCurrentVolumeReading() {
        return currentVolumeReading;
    }
}
