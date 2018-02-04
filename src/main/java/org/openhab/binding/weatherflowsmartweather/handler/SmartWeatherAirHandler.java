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
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.eclipse.smarthome.core.library.types.DateTimeType;
import org.eclipse.smarthome.core.library.types.DecimalType;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandler;
import org.eclipse.smarthome.core.types.Command;
import org.eclipse.smarthome.core.types.State;
import org.joda.time.DateTime;
import org.openhab.binding.weatherflowsmartweather.SmartWeatherEventListener;
import org.openhab.binding.weatherflowsmartweather.model.ObservationAirMessage;
import org.openhab.binding.weatherflowsmartweather.model.SmartWeatherMessage;
import org.openhab.binding.weatherflowsmartweather.model.StationStatusMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link SmartWeatherAirHandler} is responsible for handling commands, which are
 * sent to one of the channels.
 *
 * @author William Welliver - Initial contribution
 */

public class SmartWeatherAirHandler extends BaseThingHandler implements SmartWeatherEventListener {

    private final Logger logger = LoggerFactory.getLogger(SmartWeatherAirHandler.class);

    private ScheduledFuture<?> messageTimeout;

    public SmartWeatherAirHandler(Thing thing) {
        super(thing);
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        // if (channelUID.getId().equals(CHANNEL_1)) {
        // TODO: handle command

        // Note: if communication with thing fails for some reason,
        // indicate that by setting the status with detail information
        // updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR,
        // "Could not control device at IP address x.x.x.x");
        // }
    }

    @Override
    public void initialize() {
        // TODO: Initialize the thing. If done set status to ONLINE to indicate proper working.
        // Long running initialization should be done asynchronously in background.
        updateStatus(ThingStatus.ONLINE);

        // Note: When initialization can NOT be done set the status with more details for further
        // analysis. See also class ThingStatusDetail for all available status details.
        // Add a description to give user information to understand why thing does not work
        // as expected. E.g.
        // updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_ERROR,
        // "Can not access device as username and/or password are invalid");
    }

    // wonder if perhaps the refresh rate on this data may be too high by default... do we really need to
    // update this information multiple times a minute?

    @Override
    public void eventReceived(InetAddress source, SmartWeatherMessage data) {
        if (data instanceof StationStatusMessage) {
            logger.debug("got status message message: " + data);

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

            // TODO update station status fields
        } else if (data instanceof ObservationAirMessage) {
            handleObservationMessage((ObservationAirMessage) data);
        } else {
            logger.warn("not handling message: " + data);
        }
    }

    public void handleObservationMessage(ObservationAirMessage data) {
        // logger.warn("Received observation message: " + data);
        List<List> l = data.getObs();
        ThingUID uid = getThing().getUID();

        for (List obs : l) {
            logger.info("parsing observation record: " + obs);

            String[] fields = { CHANNEL_EPOCH, CHANNEL_PRESSURE, CHANNEL_TEMPERATURE, CHANNEL_HUMIDITY,
                    CHANNEL_STRIKE_COUNT, CHANNEL_STRIKE_DISTANCE, CHANNEL_BATTERY_LEVEL };
            int i = 0;
            for (String f : fields) {
                Double val = (Double) obs.get(i++);
                State type;
                if (f.equals(CHANNEL_EPOCH)) {
                    type = new DateTimeType(new DateTime(val.longValue() * 1000).toCalendar(null));
                    logger.debug("posting type = " + type);
                } else {
                    type = new DecimalType(val);
                }
                updateState(new ChannelUID(uid, f), type);
            }
        }
    }

    private void goOnline() {
        this.updateStatus(ThingStatus.ONLINE);
        messageTimeout = null;
    }

    protected void goOffline() {
        this.updateStatus(ThingStatus.OFFLINE);
        messageTimeout = null;
    }
}
