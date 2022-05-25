package control;

import tme4.Controller;
import tme4.Event;

public class WindowMalfunction extends Event {
    public WindowMalfunction(long delayTime) { super(new GreenhouseControls(), delayTime); }
    public void action() throws Controller.ControllerException {
        greenhouseControls.setStatus("windowok", false);
        greenhouseControls.issueOccurred(1,"Greenhouse window is malfunctioning");
    }
    public String toString() {
        return "Greenhouse window is malfunctioning";
    }

    @Override
    public void run() {

    }
}