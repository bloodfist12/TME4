package control;

import tme4.*;

public class PowerOut extends Event {
    public PowerOut(long delayTime) { super(new GreenhouseControls(), delayTime); }
    public void action() throws Controller.ControllerException {
        greenhouseControls.setStatus("poweron", false);
        greenhouseControls.issueOccurred(2, "Greenhouse power went out");
    }
    public String toString() {
        return "Greenhouse power is out";
    }

    @Override
    public void run() {

    }
}