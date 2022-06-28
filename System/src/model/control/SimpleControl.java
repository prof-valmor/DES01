package model.control;

import model.plant.SimpleSensor;
import model.plant.SystemPlant;
import model.plant.VolumeSensor;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class SimpleControl {
    private static SimpleControl instance;
    private Timer timer;
    private The1Task task;

    private SimpleControl() {

    }
    public void initialize(SystemPlant plant) {
        if(timer == null) {
            timer = new Timer("SimpleControl");
            task = new The1Task(plant);
            timer.scheduleAtFixedRate(task, 0, 250);
        }
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

    public enum Event {
        InOpen, InClose, OutOpen, OutClose, HSup, HInf, HMax
    }
    public void addListener(ControlListener listener) {
        task.addListener(listener);
    }
}

class The1Task extends TimerTask {
    private SystemPlant plant;
    public boolean isON;
    private boolean isTankFull;
    private boolean isTankEmpty;
    private double currentTankVolume;
    public double setPointHigh;
    public double setPointLow;
    private ArrayList<ControlListener> listenerArrayList;

    public void addListener(ControlListener listener) {
        if(!listenerArrayList.contains(listener)) listenerArrayList.add(listener);
    }

    private void updateListeners(SimpleControl.Event event) {
        for(ControlListener l : listenerArrayList)
            l.onEvent(event);
    }

    enum STATE {
        UM, DOIS, TRES, QUATRO, CINCO, SEIS
    }
    STATE state;

    public The1Task(SystemPlant plant) {
        this.plant = plant;
        isON = false;
        state = STATE.UM;
        listenerArrayList = new ArrayList<>(1);
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
    }
    @Override
    public void run() {
        if(isON) {
            double currentTankVolume = plant.getTheSensor().getCurrentMeasurement();
            // generates the event...
            if(currentTankVolume == Double.MAX_VALUE)
                updateListeners(SimpleControl.Event.HMax);
            // state machine.
            switch(state) {
                case UM -> {
                    plant.getInputValve().openIt();
                    state = STATE.DOIS;
                    updateListeners(SimpleControl.Event.InOpen);
                }
                case DOIS -> {
                    if(currentTankVolume >= setPointHigh) {
                        state = STATE.TRES;
                        updateListeners(SimpleControl.Event.HSup);
                    }
                }
                case TRES -> {
                    plant.getInputValve().closeIt();
                    state = STATE.QUATRO;
                    updateListeners(SimpleControl.Event.InClose);
                }
                case QUATRO -> {
                    plant.getOutputValve().openIt();
                    state = STATE.CINCO;
                    updateListeners(SimpleControl.Event.OutOpen);
                }
                case CINCO -> {
                    if(currentTankVolume <= setPointLow) {
                        state = STATE.SEIS;
                        updateListeners(SimpleControl.Event.HInf);
                    }
                }
                case SEIS -> {
                    plant.getOutputValve().closeIt();
                    state = STATE.UM;
                    updateListeners(SimpleControl.Event.OutClose);
                }
            }
        }
    }
}