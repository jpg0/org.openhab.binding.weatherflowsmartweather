package org.openhab.binding.weatherflowsmartweather.model;

public class HubStatusV30Message extends SmartWeatherMessage {
    private String firmware_revision;
    private int uptime;
    private int rssi;
    private int timestamp;
    private String reset_flags;
    private String stack;
    private int seq;
    private int fs;

    public String getFirmware_revision() {
        return firmware_revision;
    }

    public void setFirmware_revision(String firmware_revision) {
        this.firmware_revision = firmware_revision;
    }

    public int getUptime() {
        return uptime;
    }

    public void setUptime(int uptime) {
        this.uptime = uptime;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public String getReset_flags() {
        return reset_flags;
    }

    public void setReset_flags(String reset_flags) {
        this.reset_flags = reset_flags;
    }

    public String getStack() {
        return stack;
    }

    public void setStack(String stack) {
        this.stack = stack;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public int getFs() {
        return fs;
    }

    public void setFs(int fs) {
        this.fs = fs;
    }

    @Override
    public String toString() {
        return "HubStatusV30Message [firmware_revision=" + firmware_revision + ", uptime=" + uptime + ", rssi=" + rssi
                + ", timestamp=" + timestamp + ", reset_flags=" + reset_flags + ", stack=" + stack + ", seq=" + seq
                + ", fs=" + fs + "]";
    }
}
