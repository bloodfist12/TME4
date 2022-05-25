package control;

import tme4.*;
import java.io.Console;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Restart extends Event {
    private final String eventsFile;

    public Restart(long delayTime, String filename) {
        super(greenhouseControls, delayTime); //cant create object using stuff from within object
        eventsFile = filename;
    }

    public void action() {
        Controller controls = new GreenhouseControls();
        try {
            File file = new File(eventsFile);
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()) {    //goes through file to add events from text file
                String data = reader.nextLine();

                if(!data.contains("Bell")) {
                    String eventToCall = data.substring(data.lastIndexOf("Event=") + 6, data.indexOf(","));
                    int delayToUse = Integer.parseInt(data.substring(data.lastIndexOf("time=") + 5));

                    switch(eventToCall) {
                        case "FansOff":
                            controls.addEvent(new FansOff(delayToUse));
                            break;
                        case "FansOn":
                            controls.addEvent(new FansOn(delayToUse));
                            break;
                        case "LightOff":
                            controls.addEvent(new LightOff(delayToUse));
                            break;
                        case "LightOn":
                            controls.addEvent(new LightOn(delayToUse));
                            break;
                        case "Terminate":
                            controls.addEvent(new Terminate(delayToUse));
                            break;
                        case "ThermostatDay":
                            controls.addEvent(new ThermostatDay(delayToUse));
                            break;
                        case "ThermostatNight":
                            controls.addEvent(new ThermostatNight(delayToUse));
                            break;
                        case "WaterOff":
                            controls.addEvent(new WaterOff(delayToUse));
                            break;
                        case "WaterOn":
                            controls.addEvent(new WaterOn(delayToUse));
                            break;
                        case "WindowMalfunction":
                            controls.addEvent(new WindowMalfunction(delayToUse));
                            break;
                        case "PowerOut":
                            controls.addEvent(new PowerOut(delayToUse));
                            break;
                        default:
                            System.out.println("Something went wrong.");
                            break;
                    }

                }
                else { //if event is bell
                    if(data.contains("rings=")) {
                        int delayToUse = Integer.parseInt(data.substring(data.lastIndexOf("time=") + 5, data.indexOf(",rings=")));
                        int ringsToDo = Integer.parseInt(data.substring(data.lastIndexOf("rings=") + 6));

                        controls.addEvent(new Bell(delayToUse, ringsToDo));
                    }
                    else {
                        int delayToUse = Integer.parseInt(data.substring(data.lastIndexOf("time=") + 5));

                        controls.addEvent(new Bell(delayToUse));
                    }
                }
            }
            reader.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("Unable to find \"" + eventsFile + "\".\nCheck capitalization, spelling, and ensure file name ends with \".txt\"");
            controls.addEvent(new Terminate(0));
        }
    }

    public String toString() {
        return "Restarting system";
    }
}