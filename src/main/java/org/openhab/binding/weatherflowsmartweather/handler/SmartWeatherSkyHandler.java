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

import org.eclipse.smarthome.core.library.dimension.Intensity;
import org.eclipse.smarthome.core.library.types.DateTimeType;
import org.eclipse.smarthome.core.library.types.DecimalType;
import org.eclipse.smarthome.core.library.types.QuantityType;
import org.eclipse.smarthome.core.library.types.StringType;
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

import javax.measure.quantity.*;
import java.net.InetAddress;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static org.eclipse.smarthome.core.library.unit.MetricPrefix.MILLI;
import static org.openhab.binding.weatherflowsmartweather.WeatherFlowSmartWeatherBindingConstants.*;
import static org.openhab.binding.weatherflowsmartweather.WeatherFlowSmartWeatherBindingConstants.CHANNEL_EPOCH;

/**
 * The {@link SmartWeatherSkyHandler} is responsible for handling commands, which are
 * sent to one of the channels.
 *
 * @author William Welliver - Initial contribution
 */

public class SmartWeatherSkyHandler extends BaseThingHandler implements SmartWeatherEventListener {

    private final Logger logger = LoggerFactory.getLogger(SmartWeatherSkyHandler.class);

    private ScheduledFuture<?> messageTimeout;

    public SmartWeatherSkyHandler(Thing thing) {
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

    @Override
    public void eventReceived(InetAddress source, SmartWeatherMessage data) {
        logger.info("SkyHandler received message " + data);
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
        } else if (data instanceof ObservationSkyMessage) {
            handleObservationMessage((ObservationSkyMessage) data);
        }  else {
            logger.warn("not handling message: " + data);
        }
    }

    public void handleObservationMessage(ObservationSkyMessage data) {
        // logger.warn("Received observation message: " + data);
        List<List> l = data.getObs();
        ThingUID uid = getThing().getUID();

        for (List obs : l) {
            logger.info("parsing observation record: " + obs);

            String[] fields = { CHANNEL_EPOCH, CHANNEL_ILLUMINANCE, CHANNEL_UV, CHANNEL_RAIN_ACCUMULATED,
                    CHANNEL_WIND_LULL, CHANNEL_WIND_AVG, CHANNEL_WIND_GUST,
                    CHANNEL_WIND_DIRECTION, CHANNEL_BATTERY_LEVEL, CHANNEL_REPORT_INTERVAL,
                        CHANNEL_SOLAR_RADIATION, CHANNEL_LOCAL_DAY_RAIN_ACCUMULATION,
                    CHANNEL_PRECIPITATION_TYPE, CHANNEL_WIND_SAMPLE_INTERVAL };
            int i = 0;
            for (String f : fields) {
                Double val = (Double) obs.get(i++);
                State type = null;

                if(val == null)
                    type = new DecimalType(0);
                else {
                    switch (f) {
                        case CHANNEL_EPOCH:
                            type = new DateTimeType(new DateTime(val.longValue() * 1000).toCalendar(null));
                            break;
                        case CHANNEL_ILLUMINANCE:
                            type = new QuantityType<Illuminance>(val, SmartHomeUnits.LUX);
                            break;
                        case CHANNEL_UV:
                            type = new QuantityType<Dimensionless>(val, SmartHomeUnits.ONE);
                            break;
                        case CHANNEL_RAIN_ACCUMULATED:
                            type = new QuantityType<Length>(val, MILLI(SIUnits.METRE));
                            break;
                        case CHANNEL_WIND_LULL:
                            type = new QuantityType<Speed>(val, SmartHomeUnits.METRE_PER_SECOND);
                            break;
                        case CHANNEL_WIND_AVG:
                            type = new QuantityType<Speed>(val, SmartHomeUnits.METRE_PER_SECOND);
                            break;
                        case CHANNEL_WIND_GUST:
                            type = new QuantityType<Speed>(val, SmartHomeUnits.METRE_PER_SECOND);
                            break;
                        case CHANNEL_WIND_DIRECTION:
                            type = new QuantityType<Angle>(val, SmartHomeUnits.DEGREE_ANGLE);
                            break;
                        case CHANNEL_BATTERY_LEVEL:
                            type = new QuantityType<ElectricPotential>(val, SmartHomeUnits.VOLT);
                            break;
                        case CHANNEL_REPORT_INTERVAL:
                            type = new QuantityType<Time>(val, SmartHomeUnits.SECOND);
                            break;
                        case CHANNEL_SOLAR_RADIATION:
                            type = new QuantityType<Intensity>(val, SmartHomeUnits.IRRADIANCE);
                            break;
                        case CHANNEL_LOCAL_DAY_RAIN_ACCUMULATION:
                            type = new QuantityType<Length>(val, MILLI(SIUnits.METRE));
                            break;
                        case CHANNEL_PRECIPITATION_TYPE:
                            type = new StringType("" + val.intValue());
                            break;
                        case CHANNEL_WIND_SAMPLE_INTERVAL:
                            type = new QuantityType<Time>(val, SmartHomeUnits.SECOND);
                            break;
                        default:
                            logger.info("Received unknown field " + f + " with value " + val);
                    }
                }
                logger.debug("posting type = " + type);

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
