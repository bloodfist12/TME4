package control;

import tme4.Event;

public class FansOff extends Event {
    public FansOff(long delayTime) { super(new GreenhouseControls(), delayTime); }
    public void action() {
        // Put hardware control code here.
        greenhouseControls.setStatus("fans", false);
    }
    public String toString() {
        return "Greenhouse fans are off";
    }

    @Override
    public void run() {

    }
}