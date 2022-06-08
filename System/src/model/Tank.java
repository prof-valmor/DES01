package model;

public class Tank {
    private double currentVolume;
    private double totalVolume;
    private VolumeSensor sensor;

    public Tank(double totalVolume) {
        this.totalVolume = totalVolume;
        this.currentVolume = 0.0;
    }

    public void add(double volumeToAdd) {
        currentVolume += volumeToAdd;
        this.onVolumeUpdate();
    }

    public void remove(double volumeToRemove) {
        currentVolume -= volumeToRemove;
        this.onVolumeUpdate();
    }

    public void setSensor(VolumeSensor sensor) {
        this.sensor = sensor;
    }

    private void onVolumeUpdate() {
        if(sensor != null) sensor.updateCurrentMeasurement(this.currentVolume);
    }

    public double getTotalVolume() {
        return totalVolume;
    }
}
