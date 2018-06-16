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

import java.util.Set;

import org.eclipse.smarthome.core.thing.ThingTypeUID;

import com.google.common.collect.ImmutableSet;

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

    public static final Set<ThingTypeUID> SUPPORTED_THING_TYPES = ImmutableSet.of(THING_TYPE_SMART_WEATHER_HUB,
            THING_TYPE_SMART_WEATHER_AIR, THING_TYPE_SMART_WEATHER_SKY);

    public static final String PROPERTY_SERIAL_NUMBER = "serial_number";
}
