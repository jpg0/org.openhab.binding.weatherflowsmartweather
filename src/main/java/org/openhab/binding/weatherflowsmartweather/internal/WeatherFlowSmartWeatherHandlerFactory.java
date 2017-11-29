/**
 * Copyright (c) 2014,2017 by the respective copyright holders.
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.weatherflowsmartweather.internal;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import org.eclipse.smarthome.config.discovery.DiscoveryService;
import org.eclipse.smarthome.core.thing.Bridge;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandlerFactory;
import org.eclipse.smarthome.core.thing.binding.ThingHandler;
import org.eclipse.smarthome.core.thing.binding.ThingHandlerFactory;
import org.openhab.binding.weatherflowsmartweather.WeatherFlowSmartWeatherBindingConstants;
import org.openhab.binding.weatherflowsmartweather.handler.SmartWeatherAirHandler;
import org.openhab.binding.weatherflowsmartweather.handler.SmartWeatherHubHandler;
import org.openhab.binding.weatherflowsmartweather.handler.SmartWeatherSkyHandler;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link WeatherFlowSmartWeatherHandlerFactory} is responsible for creating things and thing
 * handlers.
 *
 * @author William Welliver - Initial contribution
 */
@Component(service = ThingHandlerFactory.class, immediate = true, configurationPid = "binding.weatherflowsmartweather")
public class WeatherFlowSmartWeatherHandlerFactory extends BaseThingHandlerFactory {

    protected static final Logger logger = LoggerFactory.getLogger(WeatherFlowSmartWeatherHandlerFactory.class);

    static final Set<ThingTypeUID> SUPPORTED_THING_TYPES_UIDS = WeatherFlowSmartWeatherBindingConstants.SUPPORTED_THING_TYPES;
    private Map<ThingUID, ServiceRegistration<?>> discoveryServiceRegs = new HashMap<>();
    SmartWeatherUDPListenerService udpListener;

    @Reference
    protected void bindUdpListener(SmartWeatherUDPListenerService service) {
        udpListener = service;
    };

    @Override
    public boolean supportsThingType(ThingTypeUID thingTypeUID) {
        return SUPPORTED_THING_TYPES_UIDS.contains(thingTypeUID);
    }

    @Override
    protected ThingHandler createHandler(Thing thing) {
        ThingTypeUID thingTypeUID = thing.getThingTypeUID();

        if (thingTypeUID.equals(WeatherFlowSmartWeatherBindingConstants.THING_TYPE_SMART_WEATHER_HUB)) {
            SmartWeatherHubHandler hubHandler = new SmartWeatherHubHandler((Bridge) thing, udpListener);
            registerDeviceDiscoveryService(hubHandler);
            return hubHandler;
        }

        else if (thingTypeUID.equals(WeatherFlowSmartWeatherBindingConstants.THING_TYPE_SMART_WEATHER_AIR)) {
            return new SmartWeatherAirHandler(thing);
        }

        else if (thingTypeUID.equals(WeatherFlowSmartWeatherBindingConstants.THING_TYPE_SMART_WEATHER_SKY)) {
            return new SmartWeatherSkyHandler(thing);
        }

        else {
            logger.warn("huhh???");
            throw new RuntimeException("whaaaaaa?");
        }
    }

    private synchronized void registerDeviceDiscoveryService(SmartWeatherHubHandler hubHandler) {
        SmartWeatherStationDiscoveryService discoveryService = new SmartWeatherStationDiscoveryService(udpListener,
                hubHandler);
        discoveryService.activate();
        this.discoveryServiceRegs.put(hubHandler.getThing().getUID(), bundleContext
                .registerService(DiscoveryService.class.getName(), discoveryService, new Hashtable<String, Object>()));
    }

    @Override
    protected synchronized void removeHandler(ThingHandler thingHandler) {
        if (thingHandler instanceof SmartWeatherHubHandler) {
            ServiceRegistration<?> serviceReg = this.discoveryServiceRegs.get(thingHandler.getThing().getUID());
            if (serviceReg != null) {
                // remove discovery service, if bridge handler is removed
                SmartWeatherStationDiscoveryService service = (SmartWeatherStationDiscoveryService) bundleContext
                        .getService(serviceReg.getReference());
                service.deactivate();
                serviceReg.unregister();
                discoveryServiceRegs.remove(thingHandler.getThing().getUID());
            }
        }
    }
}
