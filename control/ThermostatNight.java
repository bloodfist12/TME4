package control;

import tme4.Event;

public class ThermostatNight extends Event {
    public ThermostatNight(long delayTime) {
        super(new GreenhouseControls(), delayTime);
    }
    public void action() {
        // Put hardware control code here.
        greenhouseControls.setThermostatStatus("Night");
    }
    public String toString() {
        return "Thermostat on night setting";
    }

    @Override
    public void run() {

    }
}
