package org.openhab.binding.weatherflowsmartweather.model;

public class HubStatusMessage extends SmartWeatherMessage {
    protected String firmware_revision;
    protected long uptime;
    protected int rssi;
    protected long timestamp;

    public String getFirmware_version() {
        return firmware_revision;
    }

    public void setFirmware_version(String firmware_version) {
        this.firmware_revision = firmware_version;
    }

    public long getUptime() {
        return uptime;
    }

    public void setUptime(long uptime) {
        this.uptime = uptime;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "HubStatusMessage [firmware_version=" + firmware_revision + ", uptime=" + uptime + ", rssi=" + rssi
                + ", timestamp=" + timestamp + ", serial_number=" + serial_number + "]";
    }
}
