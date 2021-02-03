package org.openhab.binding.weatherflowsmartweather.model;

import static java.time.ZoneOffset.UTC;

import java.time.Instant;
import java.time.ZonedDateTime;

import org.openhab.core.thing.Thing;

public class PrecipitationStartedData {
    private String bridgeUID;
    private String thingUID;

    private String hubSerialNumber;
    private String serialNumber;

    private ZonedDateTime epoch;

    // private Logger log = LoggerFactory.getLogger(RapidWindData.class);

    public PrecipitationStartedData(Thing sky, EventPrecipitationMessage message) {
        bridgeUID = sky.getBridgeUID().getAsString();
        thingUID = sky.getUID().getAsString();

        serialNumber = message.getSerial_number();
        hubSerialNumber = message.getHub_sn();
        Object[] ob = message.getEvt();

        epoch = Instant.ofEpochMilli(((Double) ob[0]).intValue() * 1000L).atZone(UTC);
    }

    public String getBridgeUID() {
        return bridgeUID;
    }

    public String getThingUID() {
        return thingUID;
    }

    public String getHubSerialNumber() {
        return hubSerialNumber;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public ZonedDateTime getEpoch() {
        return epoch;
    }

    @Override
    public String toString() {
        return "PrecipitationStartedData{" + "bridgeUID=" + bridgeUID + ", thingUID=" + thingUID + ", epoch=" + epoch
                + '}';
    }
}
