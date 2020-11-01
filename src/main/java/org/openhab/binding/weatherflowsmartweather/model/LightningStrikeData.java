package org.openhab.binding.weatherflowsmartweather.model;

import static org.eclipse.smarthome.core.library.unit.MetricPrefix.KILO;

import javax.measure.quantity.Length;

import org.eclipse.smarthome.core.library.types.DecimalType;
import org.eclipse.smarthome.core.library.types.QuantityType;
import org.eclipse.smarthome.core.library.unit.SIUnits;
import org.eclipse.smarthome.core.thing.Thing;
import org.joda.time.DateTime;

public class LightningStrikeData {
    private String bridgeUID;
    private String thingUID;

    private String hubSerialNumber;
    private String serialNumber;

    private DateTime epoch;
    private final DecimalType energy;
    private final QuantityType<Length> distance;

    // private Logger log = LoggerFactory.getLogger(RapidWindData.class);

    public LightningStrikeData(Thing sky, EventStrikeMessage message) {
        bridgeUID = sky.getBridgeUID().getAsString();
        thingUID = sky.getUID().getAsString();

        serialNumber = message.getSerial_number();
        hubSerialNumber = message.getHub_sn();
        Object[] ob = message.getEvt();

        epoch = new DateTime((((Double) ob[0]).intValue() * 1000L));

        distance = new QuantityType<Length>((Double) ob[1], KILO(SIUnits.METRE));
        energy = new DecimalType((Double) ob[2]);
    }

    public DecimalType getEnergy() {
        return energy;
    }

    public QuantityType<Length> getDistance() {
        return distance;
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

    public DateTime getEpoch() {
        return epoch;
    }

    @Override
    public String toString() {
        return "LightningStrikeData{" + "bridgeUID=" + bridgeUID + ", thingUID=" + thingUID + ", epoch=" + epoch
                + ", distance=" + distance + ", energy=" + energy + '}';
    }
}
