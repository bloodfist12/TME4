package control;

import tme4.Event;

public class Terminate extends Event {
    public Terminate(long delayTime) { super(new GreenhouseControls(), delayTime); }
    public void action() { System.exit(0); }
    public String toString() { return "Terminating";  }
}