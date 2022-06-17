package org.openhab.binding.weatherflowsmartweather.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.*;

public class ForecastUtils {
    private static Logger log = LoggerFactory.getLogger(ForecastUtils.class);

    public static String mapWeatherIcon(String icon) {
        switch (icon) {
            case "clear-day":
                return "f7:sun_max_fill";
            case "clear-night":
                return "f7:moon_stars_fill";
            case "partly-cloudy-day":
                return "f7:cloud_sun_fill";
            case "partly-cloudy-night":
                return "f7:cloud_moon_fill";
            case "cloudy":
                return "f7:cloud_fill";
            case "thunderstorm":
                return "f7:cloud_bolt_rain_fill";
            case "rainy":
                return "f7:cloud_heavyrain_fill";
            case "possibly-sleet-day":
                return "iconify:wi:day-sleet";
            case "possibly-sleet-night":
                return "iconify:wi:night-sleet";
            case "possibly-snow-day":
                return "iconify:wi:day-snow";
            case "possibly-snow-night":
                return "iconify:wi:night-snow";
            case "possibly-rainy-day":
                return "f7:cloud_sun_rain_fill";
            case "possibly-rainy-night":
                return "f7:cloud_moon_rain_fill";
            case "possibly-thunderstorm-day":
                return "f7:cloud_sun_bolt_fill";
            case "possibly-thunderstorm-night":
                return "f7:cloud_moon_bolt_fill";
            case "snow":
                return "f7:cloud_snow_fill";
            case "foggy":
                return "f7:cloud_fog_fill";
            case "sleet":
                return "f7:cloud_sleet_fill";
            case "windy":
                return "f7:wind";
            default:
                return "?";
        }
    }
}
