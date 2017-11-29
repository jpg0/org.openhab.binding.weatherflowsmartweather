package org.openhab.binding.weatherflowsmartweather.model;

public class SmartWeatherMessage {
    protected String type;
    protected String serial_number;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSerial_number() {
        return serial_number;
    }

    public void setSerial_number(String serial_number) {
        this.serial_number = serial_number;
    }

    @Override
    public String toString() {
        return "SmartWeatherMessage [type=" + type + ", serial_number=" + serial_number + "]";
    }

}
