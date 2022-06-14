package application;

import gui.MainWindow;
import model.SystemPlant;
import model.control.SimpleControl;

public class Application {

    public static void main(String[] args) {
        System.out.println(">> Starting it...");
        MainWindow mainWindow = new MainWindow();
        mainWindow.setVisible(true);
        //
        SimpleControl.getInstance().defineSetPoints(200, 250);
    }
}
