package org.openhab.binding.weatherflowsmartweather.model;

public class BetterForecastThingConfig {
    int station_id;
    String token;
    String system_of_measurement;
    int keep_hourly;
    int keep_daily;

    public int getKeep_hourly() {
        return keep_hourly;
    }

    public void setKeep_hourly(int keep_hourly) {
        this.keep_hourly = keep_hourly;
    }

    public int getKeep_daily() {
        return keep_daily;
    }

    public void setKeep_daily(int keep_daily) {
        this.keep_daily = keep_daily;
    }

    public String getSystem_of_measurement() {
        return system_of_measurement;
    }

    public void setSystem_of_measurement(String system_of_measurement) {
        this.system_of_measurement = system_of_measurement;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getStationId() {
        return station_id;
    }

    public void setStationId(int stationId) {
        this.station_id = stationId;
    }
}
