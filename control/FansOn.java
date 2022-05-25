package control;

import tme4.Event;

public class FansOn extends Event {
    public FansOn(long delayTime) { super(new GreenhouseControls(), delayTime); }
    public void action() {
        // Put hardware control code here.
        greenhouseControls.setStatus("fans", true);
    }
    public String toString() {
        return "Greenhouse fans are on";
    }

    @Override
    public void run() {

    }
}
