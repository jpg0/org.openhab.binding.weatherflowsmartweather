package org.openhab.binding.weatherflowsmartweather.internal;

import java.net.InetAddress;
import java.util.Set;

import org.eclipse.smarthome.config.discovery.AbstractDiscoveryService;
import org.eclipse.smarthome.config.discovery.DiscoveryResult;
import org.eclipse.smarthome.config.discovery.DiscoveryResultBuilder;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.openhab.binding.weatherflowsmartweather.SmartWeatherEventListener;
import org.openhab.binding.weatherflowsmartweather.WeatherFlowSmartWeatherBindingConstants;
import org.openhab.binding.weatherflowsmartweather.handler.SmartWeatherHubHandler;
import org.openhab.binding.weatherflowsmartweather.model.SmartWeatherMessage;
import org.openhab.binding.weatherflowsmartweather.model.StationStatusMessage;
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
        logger.warn("Creating discovery service");
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
        if (data instanceof StationStatusMessage) {
            StationStatusMessage message = (StationStatusMessage) data;
            String serial = message.getSerial_number();
            ThingTypeUID thingType = null;
            String label = null;
            if (message.getSerial_number().startsWith("AR")) {
                // we have an AIR sensor.
                label = "SmartWeather Air";
                thingType = WeatherFlowSmartWeatherBindingConstants.THING_TYPE_SMART_WEATHER_AIR;
            }

            // were we able to determine the thing type?
            if (thingType == null) {
                return;
            }

            // is this sensor attached to this hub?
            if (!hubHandler.getThing().getProperties()
                    .get(WeatherFlowSmartWeatherBindingConstants.PROPERTY_SERIAL_NUMBER).equals(message.getHub_sn())) {
                return;
            }

            ThingUID thingUid = new ThingUID(thingType, hubHandler.getThing().getUID(), serial);

            logger.debug("Got discovered device.");

            Thing thing = hubHandler.getThingByUID(thingUid);
            if (thing != null) {
                logger.warn("Already have thing with ID=<" + thingUid + ">");
                return;
            } else {
                logger.debug("Nope. This should trigger a new inbox entry.");
            }

            DiscoveryResult result = DiscoveryResultBuilder.create(thingUid).withLabel(label)
                    .withBridge(hubHandler.getThing().getUID()).withRepresentationProperty(serial)
                    .withProperty("serial_number", serial).build();
            logger.info("New " + label + " discovered with ID=<" + serial + ">.");
            this.thingDiscovered(result);

        }
    }
}
