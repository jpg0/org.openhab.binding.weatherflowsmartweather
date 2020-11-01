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

import static org.eclipse.smarthome.core.library.unit.MetricPrefix.HECTO;
import static org.openhab.binding.weatherflowsmartweather.WeatherFlowSmartWeatherBindingConstants.*;

import java.net.InetAddress;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.measure.quantity.*;

import org.eclipse.smarthome.core.library.types.DateTimeType;
import org.eclipse.smarthome.core.library.types.DecimalType;
import org.eclipse.smarthome.core.library.types.QuantityType;
import org.eclipse.smarthome.core.library.unit.SIUnits;
import org.eclipse.smarthome.core.library.unit.SmartHomeUnits;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandler;
import org.eclipse.smarthome.core.types.Command;
import org.eclipse.smarthome.core.types.State;
import org.joda.time.DateTime;
import org.openhab.binding.weatherflowsmartweather.SmartWeatherEventListener;
import org.openhab.binding.weatherflowsmartweather.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

/**
 * The {@link SmartWeatherAirHandler} is responsible for handling commands, which are
 * sent to one of the channels.
 *
 * @author William Welliver - Initial contribution
 */

public class SmartWeatherAirHandler extends BaseThingHandler implements SmartWeatherEventListener {

    private final Logger logger = LoggerFactory.getLogger(SmartWeatherAirHandler.class);

    private Gson gson = new Gson();
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
        if (data instanceof StationStatusMessage || data instanceof DeviceStatusMessage) {
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
        } else if (data instanceof EventStrikeMessage) {
            logger.debug("Received Strike Message.");
            handleEventStrikeMessage((EventStrikeMessage) data);
        } else {
            logger.warn("not handling message: " + data);
        }
    }
    /*
     * private void handleEventStrikeMessage(EventStrikeMessage data) {
     * ThingUID uid = getThing().getUID();
     * String s = gson.toJson(data);
     * updateState(new ChannelUID(uid, CHANNEL_STRIKE_EVENTS), new RawType(s.getBytes(), MIME_TYPE_JSON));
     * }
     */

    private void handleEventStrikeMessage(EventStrikeMessage data) {
        ThingUID uid = getThing().getUID();
        LightningStrikeData lightningStrikeData = new LightningStrikeData(getThing(), data);
        logger.debug("handling lightning strike record: " + lightningStrikeData);
        // Event event = precipitationEventFactory.createPrecipitationEvent(precipitationStartedData);
        // logger.debug("publisher: " + eventPublisher + ", event: " + event);
        // eventPublisher.post(event);
    }

    public void handleObservationMessage(ObservationAirMessage data) {
        // logger.warn("Received observation message: " + data);
        List<List> l = data.getObs();
        ThingUID uid = getThing().getUID();

        for (List obs : l) {
            // logger.info("parsing observation record: " + obs);

            String[] fields = { CHANNEL_EPOCH, CHANNEL_PRESSURE, CHANNEL_TEMPERATURE, CHANNEL_HUMIDITY,
                    CHANNEL_STRIKE_COUNT, CHANNEL_STRIKE_DISTANCE, CHANNEL_BATTERY_LEVEL };
            int i = 0;
            for (String f : fields) {
                Double val = (Double) obs.get(i++);
                State type = null;
                switch (f) {
                    case CHANNEL_EPOCH:
                        type = new DateTimeType(new DateTime(val.longValue() * 1000).toCalendar(null));
                        break;
                    case CHANNEL_PRESSURE:
                        type = new QuantityType<Pressure>(val, HECTO(SIUnits.PASCAL));
                        break;
                    case CHANNEL_TEMPERATURE:
                        type = new QuantityType<Temperature>(val, SIUnits.CELSIUS);
                        break;
                    case CHANNEL_HUMIDITY:
                        type = new QuantityType<Dimensionless>(val, SmartHomeUnits.PERCENT);
                        break;
                    case CHANNEL_STRIKE_COUNT:
                        type = new DecimalType(val);
                        break;
                    case CHANNEL_STRIKE_DISTANCE:
                        type = new QuantityType<Length>(val, SIUnits.METRE.multiply(1000.0));
                        break;
                    case CHANNEL_BATTERY_LEVEL:
                        type = new QuantityType<ElectricPotential>(val, SmartHomeUnits.VOLT);
                        break;
                    default:
                        logger.info("Received unknown field " + f + " with value " + val);
                }

                // logger.warn("posting field = " + f + ", type = " + type.getClass() + ", value = " + type);
                // logger.warn("thing handler callback: " + this.getCallback());
                this.updateState(new ChannelUID(this.getThing().getUID(), f), type);
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
