package org.openhab.binding.weatherflowsmartweather.model;

import java.lang.reflect.Type;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public class SmartWeatherDeserializer implements JsonDeserializer<SmartWeatherMessage> {

    private static Logger log = LoggerFactory.getLogger(SmartWeatherDeserializer.class);

    @Override
    public SmartWeatherMessage deserialize(JsonElement je, Type type, JsonDeserializationContext jdc)
            throws JsonParseException {
        // Get the "type" element from the parsed JSON
        JsonElement content = je.getAsJsonObject().get("type");
        String messageType = content.getAsString();
        Gson gson = new Gson();
        SmartWeatherMessage message = null;
        switch (messageType) {
            case "obs_air":
                return gson.fromJson(je, ObservationAirMessage.class);
            case "obs_sky":
                // TODO model objects
                return gson.fromJson(je, SmartWeatherMessage.class);
            case "evt_precip":
                // TODO model objects
                return gson.fromJson(je, SmartWeatherMessage.class);
            case "evt_strike":
                return gson.fromJson(je, EventStrikeMessage.class);
            case "rapid_wind":
                // TODO model objects
                return gson.fromJson(je, SmartWeatherMessage.class);
            case "station_status":
                return gson.fromJson(je, StationStatusMessage.class);
            case "device_status":
                return gson.fromJson(je, DeviceStatusMessage.class);
            case "hub-status":
                return gson.fromJson(je, HubStatusMessage.class);
            case "hub_status":
                return gson.fromJson(je, HubStatusV30Message.class);
            default:
                log.error("Received unknown SmartWeather message type: " + messageType);
                break;
        }
        return message;
    }
}
