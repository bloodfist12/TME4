package control;

import tme4.*;

// An example of an action() that inserts a
// new one of itself into the event list:
public class Bell extends Event {
    int rings;
    public Bell(long delayTime) {
        super(new GreenhouseControls(), delayTime);
        rings = 0;
    }
    public Bell(long delayTime, int rings) {
        super(new GreenhouseControls(), delayTime);
        this.rings = rings;
    }
    public void action() {
        Controller controls = new GreenhouseControls();
        // ring bell "rings" times if "rings" is specified
        if(rings > 0) {
            for (int i = 1; i < rings; i++) {
                controls.addEvent(new Bell(2000L * i, 0));
            }
        }
    }
    public String toString() { return "Bing!"; }

    @Override
    public void run() {

    }
}