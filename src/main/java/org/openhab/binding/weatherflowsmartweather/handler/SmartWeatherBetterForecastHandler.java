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

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.openhab.binding.weatherflowsmartweather.model.*;
import org.openhab.core.i18n.CommunicationException;
import org.openhab.core.library.types.DateTimeType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.thing.*;
import org.openhab.core.thing.binding.BaseThingHandler;
import org.openhab.core.types.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

/**
 * The {@link SmartWeatherBetterForecastHandler} is responsible for handling commands, which are
 * sent to one of the channels.
 *
 * @author William Welliver - Initial contribution
 */

public class SmartWeatherBetterForecastHandler extends BaseThingHandler {

    private final Logger logger = LoggerFactory.getLogger(SmartWeatherBetterForecastHandler.class);

    protected final HttpClient httpClient;

    protected Gson gson = new Gson();

    protected Duration updateInterval = Duration.ofMinutes(15);
    protected ScheduledFuture<?> forecastUpdateTask = null;

    public SmartWeatherBetterForecastHandler(Thing thing) {
        super(thing);
        httpClient = new HttpClient(new SslContextFactory.Client(true));
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
    //
    // @Override
    // public Collection<ConfigStatusMessage> getConfigStatus() {
    // Set<ConfigStatusMessage> status = new HashSet<>();
    //
    // BetterForecastThingConfig config = getConfigAs(BetterForecastThingConfig.class);
    //
    // if (config.getStationId() == 0) {
    // logger.warn("station_id is empty");
    // status.add(ConfigStatusMessage.Builder.error(CONFIG_STATION_ID).withMessageKeySuffix(EMPTY_INVALID)
    // .withArguments(CONFIG_STATION_ID).build());
    // }
    //
    // if (config.getToken() == null || config.getToken().isEmpty()) {
    // logger.warn("token is empty");
    // status.add(ConfigStatusMessage.Builder.error(CONFIG_TOKEN).withMessageKeySuffix(EMPTY_INVALID)
    // .withArguments(CONFIG_TOKEN).build());
    // }
    //
    // return status;
    // }

    @Override
    public void initialize() {
        // TODO: Initialize the thing. If done set status to ONLINE to indicate proper working.
        // Long running initialization should be done asynchronously in background.

        BetterForecastThingConfig config = getConfigAs(BetterForecastThingConfig.class);

        if (config.getStationId() == 0) {
            logger.warn("station_id is empty");
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_ERROR, "Invalid Station ID");
            return;
        }

        if (config.getToken() == null || config.getToken().isEmpty()) {
            logger.warn("token is empty");
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_ERROR, "Invalid Authorization Token");
            return;
        }

        goOnline();

        // Note: When initialization can NOT be done set the status with more details for further
        // analysis. See also class ThingStatusDetail for all available status details.
        // Add a description to give user information to understand why thing does not work
        // as expected. E.g.
        // updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_ERROR,
        // "Can not access device as username and/or password are invalid");
    }

    private void goOnline() {
        this.updateStatus(ThingStatus.ONLINE);
        logger.info("Scheduling Forecast Fetch with interval of {}", updateInterval);
        forecastUpdateTask = scheduler.scheduleAtFixedRate(this::fetchForecast, 5000, updateInterval.toMillis(),
                TimeUnit.MILLISECONDS);
    }

    protected void goOffline() {
        if (forecastUpdateTask != null)
            forecastUpdateTask.cancel(true);
        forecastUpdateTask = null;
        this.updateStatus(ThingStatus.OFFLINE);
    }

    protected void fetchForecast() {

        BetterForecastThingConfig config = getConfigAs(BetterForecastThingConfig.class);
        try {
            URL baseURL = new URL(FORECAST_URL);
            if (!httpClient.isStarted()) {
                httpClient.start();
            }

            Request request = httpClient.newRequest(baseURL.toURI()).timeout(10, TimeUnit.SECONDS);

            request.param(CONFIG_STATION_ID, String.valueOf(config.getStationId()));
            request.param(CONFIG_TOKEN, config.getToken());

            // TODO make advanced option to select each individually
            if (!SYSTEM_OF_MEASUREMENT_METRIC.equals(config.getSystem_of_measurement())) {
                request.param("units_temp", "f");
                request.param("units_wind", "mph");
                request.param("units_pressure", "inhg");
                request.param("units_precip", "in");
                request.param("units_distance", "mi");
            }

            logger.trace("fetchForecast: requesting {}", request.getURI());
            ContentResponse response = request.send();
            logger.trace("Response code {}", response.getStatus());

            if (response.getStatus() == 401) {
                // authorization failure
                goOffline();
                updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_ERROR,
                        "Authorization Failed, invalid token or station_id?");
            }

            if (response.getStatus() != 200) {
                throw new CommunicationException(
                        "Error communicating with WeatherFlow web service. Error Code: " + response.getStatus());
            }
            String content = response.getContentAsString();
            logger.trace("fetchForecast: response {}", content);
            updateState(CHANNEL_FORECAST_RAW, new StringType(content));

            BetterForecast forecast = gson.fromJson(content, BetterForecast.class);

            forecast.enrich(config.getKeep_hourly(), config.getKeep_daily());

            String enrichedForecast = gson.toJson(forecast);
            logger.trace("fetchForecast: enrichedForecast {}", enrichedForecast);

            updateState(CHANNEL_FORECAST_ENRICHED, new StringType(enrichedForecast));

            updateState(CHANNEL_STATION_NAME, new StringType(forecast.getLocation_name()));

            Instant i = Instant.ofEpochSecond(forecast.getCurrentConditions().getTime());
            ZonedDateTime z = ZonedDateTime.ofInstant(i, ZoneOffset.UTC);
            updateState(CHANNEL_EPOCH, new DateTimeType(z));
        } catch (MalformedURLException e) {
            logger.warn("Bad url: ", e);
        } catch (Exception e) {
            logger.warn("Error occurred while fetching forecast", e);
        }
    }

    @Override
    public void dispose() {
        if (forecastUpdateTask != null)
            forecastUpdateTask.cancel(true);
        forecastUpdateTask = null;

        super.dispose();
    }
}
