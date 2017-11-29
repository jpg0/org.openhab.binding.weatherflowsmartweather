package org.openhab.binding.weatherflowsmartweather.model;

public class HubStatusMessage extends SmartWeatherMessage {
    protected String firmware_version;
    private long uptime;
    private int rssi;
    private long timestamp;
    private int reset_flags;
    private String stack;

    public String getFirmware_version() {
        return firmware_version;
    }

    public void setFirmware_version(String firmware_version) {
        this.firmware_version = firmware_version;
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

    public int getReset_flags() {
        return reset_flags;
    }

    public void setReset_flags(int reset_flags) {
        this.reset_flags = reset_flags;
    }

    public String getStack() {
        return stack;
    }

    public void setStack(String stack) {
        this.stack = stack;
    }

    @Override
    public String toString() {
        return "HubStatusMessage [firmware_version=" + firmware_version + ", uptime=" + uptime + ", rssi=" + rssi
                + ", timestamp=" + timestamp + ", reset_flags=" + reset_flags + ", stack=" + stack + ", serial_number="
                + serial_number + "]";
    }

}
