package model.control;

import model.SimpleSensor;
import model.SystemPlant;
import model.VolumeSensor;

import java.util.Timer;
import java.util.TimerTask;

public class SimpleControl {
    private static SimpleControl instance;
    private Timer timer;
    private TheTask task;

    private SimpleControl() {
        timer = new Timer();
        task = new TheTask(SystemPlant.getInstance());
        timer.scheduleAtFixedRate(task, 0, 250);
    }

    public static SimpleControl getInstance() {
        if(instance == null) instance = new SimpleControl();
        return instance;
    }

    public void defineSetPoints(double low, double high) {
        task.setPointHigh = high;
        task.setPointLow  = low;
    }

    public void turnON() {
        task.isON = true;
    }
    public void turnOff() {
        task.isON = false;
    }

    public boolean isOn() {return task.isON;}

    public String getState() {
        return task.state + "";
    }
}

class TheTask extends TimerTask {
    private SystemPlant plant;
    public boolean isON;
    private boolean isTankFull;
    private boolean isTankEmpty;
    private double currentTankVolume;
    public double setPointHigh;
    public double setPointLow;

    enum STATE {
        UM, DOIS, TRES, QUATRO, CINCO, SEIS
    }
    STATE state;

    public TheTask(SystemPlant plant) {
        this.plant = plant;
        isON = false;
        state = STATE.UM;
        //
        plant.getTheTank().setTankEmptySensor(new SimpleSensor() {
            @Override
            public void onSensorActivated() {
                isTankEmpty = true;
            }

            @Override
            public void onSensorInactivated() {
                isTankEmpty = false;
            }
        });
        plant.getTheTank().setTankFullSensor(new SimpleSensor() {
            @Override
            public void onSensorActivated() {
                isTankFull = true;
            }

            @Override
            public void onSensorInactivated() {
                isTankFull = false;
            }
        });
        plant.getTheTank().addVolumeSensor(new VolumeSensor() {
            @Override
            public void updateCurrentMeasurement(double volume) {
                currentTankVolume = volume;
            }

            @Override
            public double getCurrentMeasurement() {
                return 0;
            }
        });
    }
    @Override
    public void run() {
        if(isON) {
            switch(state) {
                case UM -> {
                    if(currentTankVolume >= setPointHigh) {
                        state = STATE.DOIS;
                    }
                }
                case DOIS -> {
                    plant.getInputValve().closeIt();
                    state = STATE.TRES;
                }
                case TRES -> {
                    plant.getOutputValve().openIt();
                    state = STATE.QUATRO;
                }
                case QUATRO -> {
                    if(currentTankVolume <= setPointLow) state = STATE.CINCO;
                }
                case CINCO -> {
                    plant.getOutputValve().closeIt();
                    state = STATE.SEIS;
                }
                case SEIS -> {
                    plant.getInputValve().openIt();
                    state = STATE.UM;
                }
            }
        }
    }
}