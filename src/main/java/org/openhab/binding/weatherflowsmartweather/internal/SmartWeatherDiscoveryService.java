package org.openhab.binding.weatherflowsmartweather.internal;

import java.net.InetAddress;
import java.util.Set;

import org.eclipse.smarthome.config.discovery.AbstractDiscoveryService;
import org.eclipse.smarthome.config.discovery.DiscoveryResult;
import org.eclipse.smarthome.config.discovery.DiscoveryResultBuilder;
import org.eclipse.smarthome.config.discovery.DiscoveryService;
import org.eclipse.smarthome.config.discovery.DiscoveryServiceCallback;
import org.eclipse.smarthome.config.discovery.ExtendedDiscoveryService;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.openhab.binding.weatherflowsmartweather.SmartWeatherEventListener;
import org.openhab.binding.weatherflowsmartweather.WeatherFlowSmartWeatherBindingConstants;
import org.openhab.binding.weatherflowsmartweather.model.HubStatusMessage;
import org.openhab.binding.weatherflowsmartweather.model.SmartWeatherMessage;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = DiscoveryService.class, immediate = true, configurationPid = "binding.weatherflowsmartweather")
public class SmartWeatherDiscoveryService extends AbstractDiscoveryService
        implements ExtendedDiscoveryService, SmartWeatherEventListener {

    protected final static Logger logger = LoggerFactory.getLogger(SmartWeatherDiscoveryService.class);

    protected final static Set<ThingTypeUID> supportedDevices = WeatherFlowSmartWeatherBindingConstants.SUPPORTED_THING_TYPES;

    protected SmartWeatherUDPListenerService udpListener;

    protected DiscoveryServiceCallback discoveryServiceCallback = null;

    @Reference()
    protected void bindUdpListener(SmartWeatherUDPListenerService service) {
        udpListener = service;
    };

    protected void unbindUdpListener(SmartWeatherUDPListenerService service) {
        udpListener = null;
    };

    public SmartWeatherDiscoveryService() {
        super(supportedDevices, 30, true);
        logger.info("Creating discovery service");
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
        if (data instanceof HubStatusMessage) {
            HubStatusMessage message = (HubStatusMessage) data;
            String serial = message.getSerial_number();

            ThingUID thingUid = new ThingUID(WeatherFlowSmartWeatherBindingConstants.THING_TYPE_SMART_WEATHER_HUB,
                    serial);

            logger.debug("Got discovered device.");
            if (getDiscoveryServiceCallback() != null) {
                logger.debug("Looking to see if this thing exists already.");
                Thing thing = getDiscoveryServiceCallback().getExistingThing(thingUid);
                if (thing != null) {
                    logger.debug("Already have thing with ID=<" + thingUid + ">");
                    return;
                } else {
                    logger.debug("Nope. This should trigger a new inbox entry.");
                }
            } else {
                logger.warn("DiscoveryServiceCallback not set. This shouldn't happen!");
                return;
            }

            DiscoveryResult result = DiscoveryResultBuilder.create(thingUid).withLabel("SmartWeather Hub")
                    .withRepresentationProperty(serial).withProperty("serial_number", serial).build();
            logger.info("New SmartWeather Hub discovered with ID=<" + serial + ">.");
            this.thingDiscovered(result);

        }
    }

    @Override
    public void setDiscoveryServiceCallback(DiscoveryServiceCallback discoveryServiceCallback) {
        this.discoveryServiceCallback = discoveryServiceCallback;
    }

    public DiscoveryServiceCallback getDiscoveryServiceCallback() {
        return discoveryServiceCallback;
    }

}
