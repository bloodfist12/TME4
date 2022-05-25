package control;

import tme4.Event;

public class ThermostatDay extends Event {
    public ThermostatDay(long delayTime) {
        super(new GreenhouseControls(), delayTime);
    }
    public void action() {
        // Put hardware control code here.
        greenhouseControls.setThermostatStatus("Day");
    }
    public String toString() {
        return "Thermostat on day setting";
    }

    @Override
    public void run() {

    }
}