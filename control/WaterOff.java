package control;

import tme4.Event;

public class WaterOff extends Event {
    public WaterOff(long delayTime) { super(new GreenhouseControls(), delayTime); }
    public void action() {
        // Put hardware control code here.
        greenhouseControls.setStatus("water", false);
    }
    public String toString() {
        return "Greenhouse water is off";
    }

    @Override
    public void run() {

    }
}