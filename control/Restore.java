package control;

import tme4.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Restore extends Event {
    private final String eventsFile;
    public Restore(long delayTime, String filename) {
        super(new GreenhouseControls(), delayTime);
        eventsFile = filename;
    }

    public void run() {

    }

    public void action() throws Controller.ControllerException {
        Controller controls = new GreenhouseControls();

        Fixable fix = greenhouseControls.getFixable();
        fix.log();
        fix.fix();

        int timePassed = 0;

        try {
            File file = new File(eventsFile);
            Scanner reader = new Scanner(file);

            while (reader.hasNextLine()) {  //finds the time to begin from when restoring
                String data = reader.nextLine();
                if(data.contains("WindowMalfunction") || data.contains("PowerOut")) {
                    timePassed = Integer.parseInt(data.substring(data.lastIndexOf("time=") + 5));
                }
            }
            reader.close();

            Scanner reReader = new Scanner(file);

            while (reReader.hasNextLine()) {    //goes through file to add events from text file
                String data = reReader.nextLine();

                if(!data.contains("Bell")) {
                    String eventToCall = data.substring(data.lastIndexOf("Event=") + 6, data.indexOf(","));
                    int delayToUse = Integer.parseInt(data.substring(data.lastIndexOf("time=") + 5));
                    delayToUse = delayToUse - timePassed;

                    if (delayToUse > 0) {
                        switch (eventToCall) {
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
                                System.out.println("Event not found");
                                break;
                        }
                    }
                }
                else { //if data contains bell
                    if(data.contains("rings=")) {
                        int delayToUse = Integer.parseInt(data.substring(data.lastIndexOf("time=") + 5, data.indexOf(",rings=")));
                        int ringsToDo = Integer.parseInt(data.substring(data.lastIndexOf("rings=") + 6));

                        if (delayToUse <= timePassed) {
                            while (delayToUse <= timePassed) {  //add delay and removes rings so bell continues from last position
                                delayToUse += 2000;
                                ringsToDo -= 1;
                            }
                        }
                        delayToUse -= timePassed;

                        controls.addEvent(new Bell(delayToUse, ringsToDo));
                    }
                    else {
                        int delayToUse = Integer.parseInt(data.substring(data.lastIndexOf("time=") + 5));
                        delayToUse = delayToUse - timePassed;

                        controls.addEvent(new Bell(delayToUse));
                    }
                }
            }
            reReader.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("Unable to find \"" + eventsFile + "\".\nCheck capitalization, spelling, and ensure file name ends with \".txt\" (or \".out\" if using -d)");
            controls.addEvent(new Terminate(0));
        }
    }
}