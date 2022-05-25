package control;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import tme4.*;

public class FixWindow extends Event implements Fixable {
    public FixWindow(long eventTime) {
        super(new GreenhouseControls(), eventTime);
    }

    public void log() {  // logs to a text file in the current directory called fix.log, prints to the console, and identify time and nature of the fix
        System.out.println("Crash Information:");
        System.out.println("Error code: " + greenhouseControls.getErrorcode());

        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        Date date = new Date(System.currentTimeMillis());
        System.out.println("Time & date of restoration: " + formatter.format(date));

        System.out.println("Fan status: " + greenhouseControls.getStatus("fans") + "\nLight status: " + greenhouseControls.getStatus("light") +
                "\nWater status: " + greenhouseControls.getStatus("water") + "\nThermostat status: " + greenhouseControls.getThermostatStatus() + "\n------------------------------");

        try {   //writes to fix.log
            String dir = System.getProperty("user.dir");

            File errorLog = new File(dir);
            errorLog.createNewFile();

            FileWriter writer = new FileWriter("fix.log");
            writer.write(formatter.format(date) + "\nError code: " + greenhouseControls.getErrorcode() + "\nFix: Window repaired");
            writer.close();

        } catch (IOException e){
            System.out.println("An error occurred with \"fix.log\"");
        }
    }

    public void fix() { // fix window and zeros out error codes
        greenhouseControls.setStatus("windowok", true);
        greenhouseControls.setErrorcode(0);
    }

    public void action() throws Controller.ControllerException {
        log();
        fix();
    }
}