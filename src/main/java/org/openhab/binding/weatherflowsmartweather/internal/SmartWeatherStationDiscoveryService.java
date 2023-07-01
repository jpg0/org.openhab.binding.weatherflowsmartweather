package org.openhab.binding.weatherflowsmartweather.internal;

import static org.openhab.binding.weatherflowsmartweather.WeatherFlowSmartWeatherBindingConstants.PROPERTY_SERIAL_NUMBER;

import java.net.InetAddress;
import java.util.Set;

import org.openhab.binding.weatherflowsmartweather.SmartWeatherEventListener;
import org.openhab.binding.weatherflowsmartweather.WeatherFlowSmartWeatherBindingConstants;
import org.openhab.binding.weatherflowsmartweather.handler.SmartWeatherHubHandler;
import org.openhab.binding.weatherflowsmartweather.model.DeviceStatusMessage;
import org.openhab.binding.weatherflowsmartweather.model.SmartWeatherMessage;
import org.openhab.binding.weatherflowsmartweather.model.StationStatusMessage;
import org.openhab.core.config.discovery.AbstractDiscoveryService;
import org.openhab.core.config.discovery.DiscoveryResult;
import org.openhab.core.config.discovery.DiscoveryResultBuilder;
import org.openhab.core.thing.Thing;
import org.openhab.core.thing.ThingTypeUID;
import org.openhab.core.thing.ThingUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SmartWeatherStationDiscoveryService extends AbstractDiscoveryService implements SmartWeatherEventListener {

    protected final static Logger logger = LoggerFactory.getLogger(SmartWeatherStationDiscoveryService.class);

    protected final static Set<ThingTypeUID> supportedDevices = WeatherFlowSmartWeatherBindingConstants.SUPPORTED_THING_TYPES;

    protected SmartWeatherUDPListenerService udpListener;

    protected SmartWeatherHubHandler hubHandler = null;

    public SmartWeatherStationDiscoveryService(SmartWeatherUDPListenerService udpListener,
            SmartWeatherHubHandler hubHandler) {
        super(supportedDevices, 30, true);
        this.udpListener = udpListener;
        this.hubHandler = hubHandler;
        logger.info("Creating discovery service: " + udpListener);
    }

    public void activate() {
        startScan();
    }

    @Override
    public void deactivate() {
        stopScan();
    }

    @Override
    protected void startScan() {
        logger.info("Starting scan: this: " + this + " udpListener: " + udpListener);
        udpListener.registerListener(this);
    }

    @Override
    protected void stopScan() {
        udpListener.unregisterListener(this);
    }

    @Override
    protected void startBackgroundDiscovery() {
        startScan();
    }

    @Override
    protected void stopBackgroundDiscovery() {
        stopScan();
    }

    @Override
    public void eventReceived(InetAddress source, SmartWeatherMessage data) {
        ThingTypeUID thingType = null;
        String label = null;
        String serial = null;
        String hubSerial = null;

        if (data instanceof StationStatusMessage) {
            StationStatusMessage message = (StationStatusMessage) data;
            serial = message.getSerial_number();
            hubSerial = message.getHub_sn();
        } else if (data instanceof DeviceStatusMessage) {
            DeviceStatusMessage message = (DeviceStatusMessage) data;
            serial = message.getSerial_number();
            hubSerial = message.getHub_sn();
        }

        if (serial == null) {
            logger.trace("Got message without serial number, ignoring: {}", data);
            return;
        }

        if (serial != null && serial.startsWith("AQ")) {
            // we have an Air Quality sensor.
            label = "SmartWeather Air Quality";
            thingType = WeatherFlowSmartWeatherBindingConstants.THING_TYPE_SMART_WEATHER_AIRQUALITY;
        } else if (serial != null && serial.startsWith("AR")) {
            // we have an AIR sensor.
            label = "SmartWeather Air";
            thingType = WeatherFlowSmartWeatherBindingConstants.THING_TYPE_SMART_WEATHER_AIR;
        } else if (serial != null && serial.startsWith("SK")) {
            // we have a SKY sensor.
            label = "SmartWeather Sky";
            thingType = WeatherFlowSmartWeatherBindingConstants.THING_TYPE_SMART_WEATHER_SKY;
        } else if (serial != null && serial.startsWith("ST")) {
            // we have a SKY sensor.
            label = "SmartWeather Tempest";
            thingType = WeatherFlowSmartWeatherBindingConstants.THING_TYPE_SMART_WEATHER_TEMPEST;
        }

        // were we able to determine the thing type?
        if (thingType == null) {
            return;
        }

        // is this sensor attached to this hub?
        if (!hubHandler.getThing().getProperties().get(PROPERTY_SERIAL_NUMBER).equals(hubSerial)) {
            return;
        }

        ThingUID thingUid = new ThingUID(thingType, hubHandler.getThing().getUID(), serial);

        logger.debug("Got discovered device: " + thingUid + ".");

        Thing thing = hubHandler.getThingByUID(thingUid);
        if (thing != null) {
            logger.debug("Already have thing with ID=<" + thingUid + ">");
            return;
        } else {
            logger.debug("Nope. This should trigger a new inbox entry.");
        }

        DiscoveryResult result = DiscoveryResultBuilder.create(thingUid).withLabel(label)
                .withBridge(hubHandler.getThing().getUID()).withRepresentationProperty(PROPERTY_SERIAL_NUMBER)
                .withProperty(PROPERTY_SERIAL_NUMBER, serial).build();
        logger.info("New " + label + " discovered with ID=<" + serial + ">.");
        this.thingDiscovered(result);
    }
}
