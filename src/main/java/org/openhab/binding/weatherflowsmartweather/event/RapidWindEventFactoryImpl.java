package org.openhab.binding.weatherflowsmartweather.event;

import com.google.common.collect.Sets;
import com.google.gson.*;
import org.eclipse.smarthome.core.events.AbstractEventFactory;
import org.eclipse.smarthome.core.events.Event;
import org.eclipse.smarthome.core.library.types.QuantityType;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.openhab.binding.weatherflowsmartweather.model.RapidWindData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;

public class RapidWindEventFactoryImpl extends AbstractEventFactory implements RapidWindEventFactory {

    static final String RAPID_WIND_EVENT_TOPIC = "smarthome/things/{thingUID}/rapidwind";

    private static final Logger log = LoggerFactory.getLogger(RapidWindEventFactoryImpl.class);
    public RapidWindEventFactoryImpl() {
        super(Sets.newHashSet(RapidWindEvent.TYPE));
    }

    public static RapidWindEvent createRapidWindEvent(RapidWindData rapid_wind) {
        String topic = RapidWindEventFactoryImpl.buildTopic(RapidWindEventFactoryImpl.RAPID_WIND_EVENT_TOPIC, rapid_wind.getThingUID());
        log.debug("Topic: " + topic);
        String payload = null;

        try {
            payload = mySerializePayload(rapid_wind);
        } catch (Throwable e) {
            log.error("Error serializing payload.", e);
        }
        log.debug("Payload: " + payload);
        return new RapidWindEvent(topic, payload, rapid_wind);
    }

    @Override
    protected Event createEventByType(String eventType, String topic, String payload, String source) throws Exception {
        log.debug("creating event " + eventType + " topic=" + topic + ", payload=" + payload + ", source=" + source);
        if (RapidWindEvent.TYPE.equals(eventType)) {
            return createRapidWindEvent(topic, payload);
        }
        return null;
    }

    protected static String buildTopic(String topic, String thingUID) {
        return topic.replace("{thingUID}", thingUID);
    }

    private Event createRapidWindEvent(String topic, String payload) {
        RapidWindData rapidWindData = myDeserializePayload(payload, RapidWindData.class);
        return new RapidWindEvent(topic, payload, rapidWindData);
    }

    protected static <T> T myDeserializePayload(String payload, Class<T> classOfPayload) {
        return gsonDateTime().fromJson(payload, classOfPayload);
    }

    protected static String mySerializePayload(RapidWindData payloadObject) {
        return gsonDateTime().toJson(payloadObject);
    }

    public static Gson gsonDateTime() {
        Gson gson = //new Gson();
        new GsonBuilder()
                .registerTypeAdapter(DateTime.class, new JsonSerializer<DateTime>() {
                    @Override
                    public JsonElement serialize(DateTime json, Type typeOfSrc, JsonSerializationContext context) {
                        log.debug("Serializing " + json);
                        JsonPrimitive p = new JsonPrimitive(ISODateTimeFormat.dateTime().print(json));
                        log.debug("Serialized to " + p);
                        return p;
                    }
                })
                .registerTypeAdapter(DateTime.class, new JsonDeserializer<DateTime>() {
                    @Override
                    public DateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                        DateTime dt = ISODateTimeFormat.dateTime().parseDateTime(json.getAsString());
                        return dt;
                    }
                })
                .registerTypeAdapter(QuantityType.class, new JsonSerializer<QuantityType>() {
                    @Override
                    public JsonElement serialize(QuantityType quantityType, Type typeOfSrc, JsonSerializationContext context) {
                        log.debug("Serializing " + quantityType);
                        JsonPrimitive p = new JsonPrimitive(quantityType.toFullString());
                        log.debug("Serialized to " + p);
                        return p;
                    }
                })
                .registerTypeAdapter(QuantityType.class, new JsonDeserializer<QuantityType>() {
                    @Override
                    public QuantityType deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                        log.debug("Deserializing " + json.getAsString());
                        QuantityType qt = new QuantityType(json.getAsString());
                        log.debug("Deserialized to " + qt);

                        return qt;
                    }
                })
                .create();

return gson;
    }
}
