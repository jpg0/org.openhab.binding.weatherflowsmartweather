package org.openhab.binding.weatherflowsmartweather.model;

import static java.time.ZoneOffset.UTC;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.List;

import javax.measure.quantity.Angle;
import javax.measure.quantity.Speed;

import org.openhab.core.library.types.QuantityType;
import org.openhab.core.library.unit.Units;
import org.openhab.core.thing.Thing;

public class RapidWindData {
    private String bridgeUID;
    private String thingUID;

    private String hubSerialNumber;
    private String serialNumber;

    private ZonedDateTime epoch;
    private final QuantityType<Angle> windDirection;
    private final QuantityType<Speed> windSpeed;

    // private Logger log = LoggerFactory.getLogger(RapidWindData.class);

    public RapidWindData(Thing sky, EventRapidWindMessage message) {
        bridgeUID = sky.getBridgeUID().getAsString();
        thingUID = sky.getUID().getAsString();

        serialNumber = message.getSerial_number();
        hubSerialNumber = message.getHub_sn();
        List ob = message.getOb();

        epoch = Instant.ofEpochMilli(((Double) ob.get(0)).intValue() * 1000L).atZone(UTC);

        windSpeed = new QuantityType<Speed>((Double) ob.get(1), Units.METRE_PER_SECOND);
        windDirection = new QuantityType<Angle>((Double) ob.get(2), Units.DEGREE_ANGLE);
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

    public QuantityType<Angle> getWindDirection() {
        return windDirection;
    }

    public QuantityType<Speed> getWindSpeed() {
        return windSpeed;
    }

    @Override
    public String toString() {
        return "RapidWindData{" + "bridgeUID=" + bridgeUID + ", thingUID=" + thingUID + ", epoch=" + epoch
                + ", windDirection=" + windDirection + ", windSpeed=" + windSpeed + '}';
    }
}
