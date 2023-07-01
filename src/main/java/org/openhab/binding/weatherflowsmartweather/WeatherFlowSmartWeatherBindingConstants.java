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
package org.openhab.binding.weatherflowsmartweather;

import java.util.HashSet;
import java.util.Set;

import org.openhab.core.thing.ThingTypeUID;

/**
 * The {@link WeatherFlowSmartWeatherBindingConstants} class defines common constants, which are
 * used across the whole binding.
 *
 * @author William Welliver - Initial contribution
 */

public class WeatherFlowSmartWeatherBindingConstants {

    private static final String BINDING_ID = "weatherflowsmartweather";

    // List of all Thing Type UIDs
    public static final ThingTypeUID THING_TYPE_SMART_WEATHER_HUB = new ThingTypeUID(BINDING_ID, "hub");
    public static final ThingTypeUID THING_TYPE_SMART_WEATHER_AIR = new ThingTypeUID(BINDING_ID, "air");
    public static final ThingTypeUID THING_TYPE_SMART_WEATHER_SKY = new ThingTypeUID(BINDING_ID, "sky");
    public static final ThingTypeUID THING_TYPE_SMART_WEATHER_TEMPEST = new ThingTypeUID(BINDING_ID, "tempest");
    public static final ThingTypeUID THING_TYPE_SMART_WEATHER_AIRQUALITY = new ThingTypeUID(BINDING_ID, "quality");
    public static final ThingTypeUID THING_TYPE_SMART_WEATHER_BETTER_FORECAST = new ThingTypeUID(BINDING_ID,
            "better-forecast");

    // List of all Hub Channel ids
    public static final String CHANNEL_RSSI = "rssi";
    public static final String CHANNEL_FIRMWARE_VERSION = "firmware_version";
    public static final String CHANNEL_UPTIME = "uptime";
    public static final String CHANNEL_LAST_REPORT = "lastReport";

    // List of all Air Channel ids
    public static final String CHANNEL_EPOCH = "epoch";
    public static final String CHANNEL_TEMPERATURE = "temperature";
    public static final String CHANNEL_PRESSURE = "pressure";
    public static final String CHANNEL_HUMIDITY = "humidity";
    public static final String CHANNEL_STRIKE_COUNT = "strike_count";
    public static final String CHANNEL_STRIKE_DISTANCE = "strike_distance";
    public static final String CHANNEL_BATTERY_LEVEL = "battery_level";

    // List of all Sky Channel ids
    public static final String CHANNEL_ILLUMINANCE = "illuminance";
    public static final String CHANNEL_UV = "uv";
    public static final String CHANNEL_RAIN_ACCUMULATED = "rain_accumulated";
    public static final String CHANNEL_WIND_LULL = "wind_lull";
    public static final String CHANNEL_WIND_AVG = "wind_avg";
    public static final String CHANNEL_WIND_GUST = "wind_gust";
    public static final String CHANNEL_WIND_DIRECTION = "wind_direction";
    public static final String CHANNEL_REPORT_INTERVAL = "report_interval";
    public static final String CHANNEL_SOLAR_RADIATION = "solar_radiation";
    public static final String CHANNEL_LOCAL_DAY_RAIN_ACCUMULATION = "local_day_rain_accumulation";
    public static final String CHANNEL_PRECIPITATION_TYPE = "precipitation_type";
    public static final String CHANNEL_WIND_SAMPLE_INTERVAL = "wind_sample_interval";

    // List of all Forecast Channel ids
    public static final String CHANNEL_STATION_NAME = "station_name";
    public static final String CHANNEL_FORECAST_RAW = "forecast_raw";
    public static final String CHANNEL_FORECAST_ENRICHED = "forecast_enriched";

    // Air quality channel ids
    public static final String CHANNEL_PM10 = "pm10";
    public static final String CHANNEL_PM25 = "pm25";
    public static final String CHANNEL_PM100 = "pm100";
    public static final String CHANNEL_PARTICLE_3UM = "particles_03um";
    public static final String CHANNEL_PARTICLE_5UM = "particles_05um";
    public static final String CHANNEL_PARTICLE_10UM = "particles_10um";
    public static final String CHANNEL_PARTICLE_25UM = "particles_25um";
    public static final String CHANNEL_PARTICLE_50UM = "particles_50um";
    public static final String CHANNEL_PARTICLE_100UM = "particles_100um";

    public static final String SKIP = "SKIP";

    // Event Channel ids
    public static final String CHANNEL_STRIKE_EVENTS = "strike_events";
    public static final String CHANNEL_RAPID_WIND_EVENTS = "rapid_wind_events";
    public static final String CHANNEL_PRECIPITATION_START_EVENTS = "precipitation_start_events";

    public static final String FORECAST_URL = "https://swd.weatherflow.com/swd/rest/better_forecast";

    public static final String CONFIG_TOKEN = "token";
    public static final String CONFIG_STATION_ID = "station_id";
    public static final String CONFIG_SYSTEM_OF_MEASUREMENT = "system_of_measurement";
    public static final String CONFIG_KEEP_HOURLY = "keep_hourly";
    public static final String CONFIG_KEEP_DAILY = "keep_daily";

    public static final String SYSTEM_OF_MEASUREMENT_METRIC = "Metric";

    public static final String EMPTY_INVALID = "empty or invalid";

    public static final Set<ThingTypeUID> SUPPORTED_THING_TYPES;
    static {
        Set<ThingTypeUID> thingTypes = new HashSet<>();
        thingTypes.add(THING_TYPE_SMART_WEATHER_HUB);
        thingTypes.add(THING_TYPE_SMART_WEATHER_AIR);
        thingTypes.add(THING_TYPE_SMART_WEATHER_SKY);
        thingTypes.add(THING_TYPE_SMART_WEATHER_TEMPEST);
        thingTypes.add(THING_TYPE_SMART_WEATHER_BETTER_FORECAST);
        thingTypes.add(THING_TYPE_SMART_WEATHER_AIRQUALITY);
        SUPPORTED_THING_TYPES = Set.copyOf(thingTypes);
    }
    public static final String PROPERTY_SERIAL_NUMBER = "serial_number";

    public static final String MIME_TYPE_JSON = "application/json";
}
