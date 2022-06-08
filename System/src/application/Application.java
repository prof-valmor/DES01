package application;

import model.OnPlantUpdateListener;
import model.SystemPlant;
import model.ValveState;

import java.util.Timer;
import java.util.TimerTask;

public class Application {

    public static void main(String[] args) {
        System.out.println(">> Starting it...");
        //
        SystemPlant plant = new SystemPlant();

        plant.setListener(new OnPlantUpdateListener() {
            @Override
            public void onPlantUpdate() {
                double volume = plant.getTheSensor().getCurrentMeasurement();
                System.out.println(">> Volume: " + volume);
                System.out.println(">> Volume %: " + plant.getVolumePercentage());
            }
        });

        plant.getInputValve().setState(ValveState.OPEN);
        plant.turnOn();

    }
}
