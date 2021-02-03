package org.openhab.binding.weatherflowsmartweather.util;

import java.lang.reflect.Type;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.openhab.core.library.types.QuantityType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.*;

public class GsonUtils {
    private static Logger log = LoggerFactory.getLogger(GsonUtils.class);

    public static Gson gsonDateTime() {
        Gson gson = // new Gson();
                new GsonBuilder().registerTypeAdapter(ZonedDateTime.class, new JsonSerializer<ZonedDateTime>() {
                    @Override
                    public JsonElement serialize(ZonedDateTime json, Type typeOfSrc, JsonSerializationContext context) {
                        log.debug("Serializing " + json);
                        JsonPrimitive p = new JsonPrimitive(json.format(DateTimeFormatter.ISO_DATE_TIME));
                        log.debug("Serialized to " + p);
                        return p;
                    }
                }).registerTypeAdapter(ZonedDateTime.class, new JsonDeserializer<ZonedDateTime>() {
                    @Override
                    public ZonedDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                            throws JsonParseException {
                        ZonedDateTime dt = ZonedDateTime.parse(json.getAsString(), DateTimeFormatter.ISO_DATE_TIME);
                        return dt;
                    }
                }).registerTypeAdapter(QuantityType.class, new JsonSerializer<QuantityType>() {
                    @Override
                    public JsonElement serialize(QuantityType quantityType, Type typeOfSrc,
                            JsonSerializationContext context) {
                        log.debug("Serializing " + quantityType);
                        JsonPrimitive p = new JsonPrimitive(quantityType.toFullString());
                        log.debug("Serialized to " + p);
                        return p;
                    }
                }).registerTypeAdapter(QuantityType.class, new JsonDeserializer<QuantityType>() {
                    @Override
                    public QuantityType deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                            throws JsonParseException {
                        log.debug("Deserializing " + json.getAsString());
                        QuantityType qt = new QuantityType(json.getAsString());
                        log.debug("Deserialized to " + qt);

                        return qt;
                    }
                }).create();

        return gson;
    }
}
