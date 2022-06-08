package model;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Represents a Tank which has an input and an output valves.
 * A sensor can be added to the tank.
 */
public class SystemPlant {
    private Tank  theTank;
    private Valve inputValve;
    private Valve outputValve;
    private VolumeSensor theSensor;
    private boolean isOn;
    //
    private Timer timer;
    private OnPlantUpdateListener listener;

    public SystemPlant() {
        theTank     = new Tank(300.0);
        inputValve  = new Valve(10.0);
        outputValve = new Valve(20.0);
        theSensor   = new LinearVolumeSensor();
        theTank.setSensor(theSensor);
        //
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if(isOn) {
                    inputValve.onTimePass(1);
                    outputValve.onTimePass(1);
                    //
                    if(listener != null) listener.onPlantUpdate();
                }
            }
        }, 0, 1000);
        //
        inputValve.setListener(volumeDispensed -> {
            theTank.add(volumeDispensed);
        });
        outputValve.setListener((volumeDispensed -> {
            theTank.remove(volumeDispensed);
        }));
    }

    public VolumeSensor getTheSensor() {
        return theSensor;
    }

    public Tank getTheTank() {
        return theTank;
    }

    public void turnOn() {
        this.isOn = true;
    }
    public void turnOff() {
        this.isOn = false;
    }

    public Valve getInputValve() {
        return inputValve;
    }

    public Valve getOutputValve() {
        return outputValve;
    }

    public void setListener(OnPlantUpdateListener listener) {
        this.listener = listener;
    }

    public double getVolumePercentage() {
        double percentage = theSensor.getCurrentMeasurement();
        percentage /= theTank.getTotalVolume();

        return percentage;
    }
}
