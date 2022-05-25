package control;

import tme4.Event;

public class WaterOn extends Event {
    public WaterOn(long delayTime) { super(new GreenhouseControls(), delayTime); }
    public void action() {
        // Put hardware control code here.
        greenhouseControls.setStatus("water", true);
    }
    public String toString() {
        return "Greenhouse water is on";
    }

    @Override
    public void run() {

    }
}
