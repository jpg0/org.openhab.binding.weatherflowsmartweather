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

    private static Gson gson = null;

    static class ZonedDateTimeAdapter implements JsonSerializer<ZonedDateTime>, JsonDeserializer<ZonedDateTime> {
        @Override
        public JsonElement serialize(ZonedDateTime json, Type typeOfSrc, JsonSerializationContext context) {
            log.debug("Serializing " + json);
            JsonPrimitive p = new JsonPrimitive(json.format(DateTimeFormatter.ISO_DATE_TIME));
            log.debug("Serialized to " + p);
            return p;
        }

        @Override
        public ZonedDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            ZonedDateTime dt = ZonedDateTime.parse(json.getAsString(), DateTimeFormatter.ISO_DATE_TIME);
            return dt;
        }
    }

    static class QuantityTypeAdapter implements JsonSerializer<QuantityType>, JsonDeserializer<QuantityType> {
        @Override
        public JsonElement serialize(QuantityType quantityType, Type typeOfSrc, JsonSerializationContext context) {
            log.debug("Serializing " + quantityType);
            JsonPrimitive p = new JsonPrimitive(quantityType.toFullString());
            log.debug("Serialized to " + p);
            return p;
        }

        @Override
        public QuantityType deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            log.debug("Deserializing " + json.getAsString());
            QuantityType qt = new QuantityType(json.getAsString());
            log.debug("Deserialized to " + qt);

            return qt;
        }
    }

    public static Gson gsonDateTime() {
        if (gson == null)
            gson = // new Gson();
                    new GsonBuilder().registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeAdapter())
                            .registerTypeAdapter(QuantityType.class, new QuantityTypeAdapter()).create();

        return gson;
    }
}
