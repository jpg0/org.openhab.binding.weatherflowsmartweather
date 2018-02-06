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
package org.openhab.binding.weatherflowsmartweather.handler;

import static org.openhab.binding.weatherflowsmartweather.WeatherFlowSmartWeatherBindingConstants.*;

import java.net.InetAddress;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.eclipse.smarthome.core.library.types.DateTimeType;
import org.eclipse.smarthome.core.library.types.DecimalType;
import org.eclipse.smarthome.core.library.types.StringType;
import org.eclipse.smarthome.core.thing.Bridge;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.ThingStatusDetail;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.eclipse.smarthome.core.thing.binding.BaseBridgeHandler;
import org.eclipse.smarthome.core.types.Command;
import org.joda.time.DateTime;
import org.openhab.binding.weatherflowsmartweather.SmartWeatherEventListener;
import org.openhab.binding.weatherflowsmartweather.WeatherFlowSmartWeatherBindingConstants;
import org.openhab.binding.weatherflowsmartweather.internal.SmartWeatherUDPListenerService;
import org.openhab.binding.weatherflowsmartweather.model.HubStatusMessage;
import org.openhab.binding.weatherflowsmartweather.model.ObservationAirMessage;
import org.openhab.binding.weatherflowsmartweather.model.SmartWeatherMessage;
import org.openhab.binding.weatherflowsmartweather.model.StationStatusMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link SmartWeatherHubHandler} is responsible for handling commands, which are
 * sent to one of the channels.
 *
 * @author William Welliver - Initial contribution
 */

public class SmartWeatherHubHandler extends BaseBridgeHandler implements SmartWeatherEventListener {

    private final Logger logger = LoggerFactory.getLogger(SmartWeatherHubHandler.class);

    protected SmartWeatherUDPListenerService udpListener;

    private ScheduledFuture<?> messageTimeout;

    public SmartWeatherHubHandler(Bridge bridge, SmartWeatherUDPListenerService udpListener) {
        super(bridge);
        this.udpListener = udpListener;
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        // we don't really have any commands to handle.
    }

    @Override
    public void initialize() {
        if (udpListener != null) {
            udpListener.registerListener(this);
            updateStatus(ThingStatus.ONLINE);

        } else {
            updateStatus(ThingStatus.OFFLINE);
        }
    }

    @Override
    public void eventReceived(InetAddress source, SmartWeatherMessage data) {
        String serial = data.getSerial_number();

        if (serial != null && serial.equals(
                this.getThing().getProperties().get(WeatherFlowSmartWeatherBindingConstants.PROPERTY_SERIAL_NUMBER))) {
            // logger.warn("Bridge Got message!");

            if (data instanceof HubStatusMessage) {
                try {
                    if (messageTimeout != null) {
                        messageTimeout.cancel(true);
                    }
                    if (this.getThing().getStatus() == ThingStatus.OFFLINE) {
                        goOnline();
                    }
                    messageTimeout = scheduler.schedule(new Runnable() {
                        @Override
                        public void run() {
                            goOffline();
                        }
                    }, 3, TimeUnit.MINUTES);

                    handleHubStatusMessage((HubStatusMessage) data);
                } catch (Exception e) {
                    logger.warn("Got exception", e);
                }
            }
        } else if (serial != null) { // TODO need a better approach to this
            if (data instanceof ObservationAirMessage) {
                ObservationAirMessage message = (ObservationAirMessage) data;
                String serialNumber = message.getSerial_number();
                ThingUID thingUid = new ThingUID(WeatherFlowSmartWeatherBindingConstants.THING_TYPE_SMART_WEATHER_AIR,
                        getThing().getUID(), serialNumber);

                Thing t = this.getThingByUID(thingUid);
                if (t == null) {
                    logger.warn("air observation but not for us.");
                    return;
                } // not our hub and sensor combo.
                SmartWeatherEventListener handler = (SmartWeatherEventListener) t.getHandler();
                handler.eventReceived(source, message);
            } else if (data instanceof StationStatusMessage) {
                StationStatusMessage message = (StationStatusMessage) data;
                String serialNumber = message.getSerial_number();
                ThingUID thingUid = new ThingUID(WeatherFlowSmartWeatherBindingConstants.THING_TYPE_SMART_WEATHER_AIR,
                        getThing().getUID(), serialNumber);

                Thing t = this.getThingByUID(thingUid);
                if (t == null) {
                    logger.warn("air observation but not for us.");
                    return;
                } // not our hub and sensor combo.
                SmartWeatherEventListener handler = (SmartWeatherEventListener) t.getHandler();
                handler.eventReceived(source, message);
            }

        }

    }

    // wonder if perhaps the refresh rate on this data may be too high by default... do we really need to
    // update this information multiple times a minute?
    private void handleHubStatusMessage(HubStatusMessage data) {
        updateState(new ChannelUID(getThing().getUID(), CHANNEL_RSSI), new DecimalType(data.getRssi()));
        updateState(new ChannelUID(getThing().getUID(), CHANNEL_FIRMWARE_VERSION),
                new StringType(data.getFirmware_version()));
        updateState(new ChannelUID(getThing().getUID(), CHANNEL_LAST_REPORT),
                new DateTimeType(new DateTime(data.getTimestamp() * 1000).toCalendar(null)));
        updateState(new ChannelUID(getThing().getUID(), CHANNEL_UPTIME), new DecimalType(data.getUptime()));
    }

    private void goOnline() {
        this.updateStatus(ThingStatus.ONLINE);
        messageTimeout = null;
    }

    protected void goOffline() {
        this.updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.BRIDGE_OFFLINE);
        messageTimeout = null;
    }
}
