package org.openhab.binding.weatherflowsmartweather.model;

import java.util.List;

public class ObservationTempestMessage extends SmartWeatherMessage {
    private String hub_sn;

    private List<List> obs;

    private int firmware_revision;

    public int getFirmware_revision() {
        return firmware_revision;
    }

    public void setFirmware_revision(int firmware_revision) {
        this.firmware_revision = firmware_revision;
    }

    public String getHub_sn() {
        return hub_sn;
    }

    public void setHub_sn(String hub_sn) {
        this.hub_sn = hub_sn;
    }

    public List<List> getObs() {
        return obs;
    }

    public void setObs(List<List> obs) {
        this.obs = obs;
    }

    @Override
    public String toString() {
        return "ObservationTempestMessage{" + "hub_sn='" + hub_sn + '\'' + ", obs=" + obs + ", firmware_revision="
                + firmware_revision + ", serial_number='" + serial_number + '\'' + '}';
    }
}
