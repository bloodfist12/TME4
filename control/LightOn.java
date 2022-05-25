package control;

import tme4.Event;

public class LightOn extends Event {
    public LightOn(long delayTime) { super(new GreenhouseControls(), delayTime); }
    public void action() {
        // Put hardware control code here to
        // physically turn on the light.
        greenhouseControls.setStatus("light", true);
    }
    public String toString() { return "Light is on"; }

    @Override
    public void run() {

    }
}
