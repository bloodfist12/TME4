package control;

import tme4.Event;

public class LightOff extends Event {
    public LightOff(long delayTime) { super(new GreenhouseControls(), delayTime); }
    public void action() {
        // Put hardware control code here to
        // physically turn off the light.
        greenhouseControls.setStatus("light", false);
    }
    public String toString() { return "Light is off"; }

    @Override
    public void run() {

    }
}